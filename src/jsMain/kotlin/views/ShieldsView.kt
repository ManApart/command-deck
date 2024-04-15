package views

import Direction
import GameState
import View
import components.SineGraph
import components.graphSection
import components.sineGraph
import components.slider
import el
import frames.ShieldsUpdate
import kotlinx.html.*
import kotlinx.html.js.div
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import replaceElement
import sparks
import wsSend

fun shieldsShake() = listOf("shield-power-distribution") + Direction.entries.map { "${it.name.lowercase()}-shield-frequency-section" }

private val graphs = mutableMapOf<Direction, SineGraph>()

//Use amplitude for power
fun shieldsView() {
    GameState.setCurrent(View.SHIELDS)
    replaceElement {
        nav()
        sparks()
        div {
            viewTitle("Shields")
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

fun TagConsumer<HTMLElement>.shieldSection(direction: Direction, offset: Int) {
    val shield = GameState.shields[direction]!!
    val name = direction.name.lowercase()
    graphs[direction] = graphSection(shield, name, "${name.capitalize()} Shields", offset)
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