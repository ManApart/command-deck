package views

import Direction
import GameState
import View
import components.SineGraph
import components.sineGraph
import components.slider
import kotlinx.html.*
import kotlinx.html.js.div
import org.w3c.dom.HTMLElement
import replaceElement



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
            }
            div {
                id = "shield-controls"
                shieldSection(Direction.FORE)
                shieldSection(Direction.AFT)
                shieldSection(Direction.PORT)
                shieldSection(Direction.STARBOARD)
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.shieldSection(direction: Direction){
    lateinit var graph: SineGraph
    div("shield-section") {
        val directionName = direction.name.lowercase()
        id = "$directionName-shield-frequency-section"
        //TODO - get from game state
        val foreAmplitude = 5
        val foreFrequency = 1

        h3("shield-header") { +"${directionName.capitalize()} Shields" }

        div("shield-top-section") {
            //TODO - make analog and maybe make both sliders drag on canvas
            div("shield-amplitude") {
                p { +"Amplitude" }
                slider("$directionName-amplitude", 0, 10, foreAmplitude, { 10 - it }, vertical = true) {
                    graph.amplitude = it
                }
            }
            graph = sineGraph("$directionName-shield-frequency", "shield-frequency-wrapper")
        }
        div("shield-frequency") {
            p { +"Frequency" }
            slider("$directionName-frequency", 1, 5, foreFrequency) {
                graph.frequency = it
            }
        }

    }
}