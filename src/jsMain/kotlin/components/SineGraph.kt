package components

import Wave
import el
import frames.ShieldsUpdate
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.js.div
import onElementExists
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import wsSend
import kotlin.math.PI
import kotlin.math.sin

private const val amount: Int = 20
private const val distance: Int = 10
private const val radius: Int = 10
private const val heightScale: Int = 5

data class SineGraph(
    val id: String,
    var amplitude: Int = 10,
    var frequency: Int = 1,
    var w: Double = 0.0,
    var h: Double = 0.0,
    var width: Int = 0,
    var arr: Array<GraphPoint> = arrayOf(),
    val span: Double = PI * 2.25,
) {
    lateinit var c: CanvasRenderingContext2D
}

data class GraphPoint(var a: Double, var x: Int, var c: String = "hsl(th, 75%, 55%)")


fun TagConsumer<HTMLElement>.graphSection(wave: Wave, name: String, title: String, initialOffset: Int): SineGraph {
    lateinit var graph: SineGraph
    div("graph-section") {
        id = "$name-graph-frequency-section"

        h3("graph-header") { +title }

        div("graph-top-section") {
            //TODO - make analog and maybe make both sliders drag on canvas
            div("graph-amplitude") {
                p { +"Amplitude" }
                slider("$name-amplitude", 0, 10, wave.amplitude, { 10 - it }, vertical = true) {
                    graph.amplitude = it
                    wave.amplitude = it
                    wsSend(ShieldsUpdate(GameState.shields))
                }
            }
            graph = sineGraph("$name-graph-frequency", "graph-frequency-wrapper", wave.amplitude, wave.frequency, initialOffset)
        }
        div("graph-frequency") {
            p { +"Frequency" }
            slider("$name-frequency", 1, 5, wave.frequency) {
                graph.frequency = it
                wave.frequency = it
                wsSend(ShieldsUpdate(GameState.shields))
            }
        }
    }
    return graph
}

fun TagConsumer<HTMLElement>.sineGraph(id: String, wrapperClasses: String = "", amplitude: Int = 5, frequency: Int= 1, offset: Int = 1): SineGraph {
    div(wrapperClasses) {
        canvas {
            this.id = id
        }
    }
    val graph = SineGraph(id, amplitude, frequency)
    onElementExists(id, callback = { startGraph(graph, offset) })
    return graph
}


private fun startGraph(graph: SineGraph, offset: Int) {
    with(graph) {
        val canvas = el<HTMLCanvasElement>(id)
        c = canvas.getContext("2d") as CanvasRenderingContext2D
        w = canvas.width.toDouble()
        h = canvas.height.toDouble()
        width = amount * (radius * 2 + distance)
        arr = (0 until amount).mapIndexed { i, _ ->
            GraphPoint(span / amount * (i+offset), i * (radius * 2 + distance))
        }.toTypedArray()
    }
    window.requestAnimationFrame { loop(it, graph) }
}

private fun loop(time: Double, graph: SineGraph) {
    with(graph) {
        c.fillStyle = "#222"
        c.fillRect(0.0, 0.0, w, h)

        arr.forEach {
            it.a += PI / 180 * frequency
            c.beginPath()
            c.arc(it.x - width / 2 + w / 2, sin(it.a) * (amplitude*heightScale) + h / 2, radius.toDouble(), 0.0, PI * 2)
            c.closePath()
            c.fillStyle = it.c.replace("th", "${it.a * 20}")
            c.fill()
        }

        if (el<HTMLCanvasElement?>(id) != null) {
            window.requestAnimationFrame { loop(it, graph) }
        }
    }
}