package views

import el
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.js.div
import onElementExists
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import replaceElement
import kotlin.math.PI
import kotlin.math.sin

private var w = 0.0
private var h = 0.0
private var width = 0
private var arr = arrayOf<GraphPoint>()
private lateinit var c: CanvasRenderingContext2D

private val amount = 20
private val distance = 10
private val radius = 10
private val height = 60
private val span = PI * 2.25

private data class GraphPoint(var a: Double, var x: Int, var c: String = "hsl(th, 75%, 55%)")

//TODO - make graph a component so it can be used by weapons and comms
fun shieldsView() {
    GameState.currentView = View.SHIELDS
    replaceElement {
        nav()
        div {
            h1 {
                +"Shields"
            }
            crewmanTitle()
            div {
                id = "shield-power-distribution"
            }
            div {
                id = "shield-frequency"
                canvas {
                    id = "frequency-canvas"
                }
            }
        }
    }
    onElementExists("frequency-canvas", callback = ::startGraph)
}

private fun startGraph() {
    val canvas = el<HTMLCanvasElement>("frequency-canvas")
    c = canvas.getContext("2d") as CanvasRenderingContext2D
    w = canvas.width.toDouble()
    h = canvas.height.toDouble()
    width = amount*(radius*2 + distance)
    arr = (0 until amount).mapIndexed { i, _ ->
        GraphPoint(span/ amount*i, i*(radius*2+ distance))
    }.toTypedArray()

    window.requestAnimationFrame(::loop)
}

private fun loop(time: Double) {
    c.fillStyle = "#222"
    c.fillRect(0.0, 0.0, w, h)

    arr.forEach {
        it.a += PI/180*4
        c.beginPath()
        c.arc(it.x - width/2 + w/2, sin(it.a)* height + h/2, radius.toDouble(), 0.0, PI*2)
        c.closePath()
        c.fillStyle = it.c.replace("th", "${it.a*20}")
        c.fill()
    }

    window.requestAnimationFrame(::loop)
}