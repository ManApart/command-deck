package views

import Direction
import GameState
import View
import components.SineGraph
import components.sineGraph
import components.slider
import el
import kotlinx.html.*
import kotlinx.html.js.div
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import replaceElement

private val graphs = mutableMapOf<Direction, SineGraph>()

//Use amplitude for power
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
                p {
                    id = "power-display"
                    +"Power: ${GameState.shields.values.sumOf { it.amplitude }}/${GameState.power[ShipSystem.SHIELDS] ?: 0}"
                }
            }
            div {
                id = "shield-controls"
                shieldSection(Direction.FORE, 0)
                shieldSection(Direction.AFT, 10)
                shieldSection(Direction.PORT, 20)
                shieldSection(Direction.STARBOARD, 30)
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.shieldSection(direction: Direction, offset: Int) {
    lateinit var graph: SineGraph
    val shield = GameState.shields[direction]!!
    div("shield-section") {
        val directionName = direction.name.lowercase()
        id = "$directionName-shield-frequency-section"
        //TODO - get from game state

        h3("shield-header") { +"${directionName.capitalize()} Shields" }

        div("shield-top-section") {
            //TODO - make analog and maybe make both sliders drag on canvas
            div("shield-amplitude") {
                p { +"Amplitude" }
                slider("$directionName-amplitude", 0, 10, shield.amplitude, { 10 - it }, vertical = true) {
                    graph.amplitude = it
                }
            }
            graph = sineGraph("$directionName-shield-frequency", "shield-frequency-wrapper", shield.amplitude, shield.frequency, offset)
        }
        div("shield-frequency") {
            p { +"Frequency" }
            slider("$directionName-frequency", 1, 5, shield.frequency) {
                graph.frequency = it
            }
        }
    }
    graphs[direction] = graph
}

fun updateShields() {
    el("power-display").innerText = "Power: ${GameState.shields.values.sumOf { it.amplitude }}/${GameState.power[ShipSystem.SHIELDS] ?: 0}"
    Direction.entries.forEach { direction ->
        val directionName = direction.name.lowercase()
        val graph = graphs[direction]!!
        val shield = GameState.shields[direction]!!
        graph.amplitude = shield.amplitude
        graph.frequency = shield.frequency
        el<HTMLInputElement>("$directionName-amplitude").value = shield.amplitude.toString()
        el<HTMLInputElement>("$directionName-frequency").value = shield.frequency.toString()
    }
}