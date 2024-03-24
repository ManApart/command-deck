package views

import GameState
import View
import components.SineGraph
import components.sineGraph
import components.slider
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.id
import kotlinx.html.js.div
import kotlinx.html.p
import replaceElement

private lateinit var foreGraph: SineGraph

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
                id = "fore-shield-frequency"
                //TODO - get from game state
                val foreAmplitude = 5
                val foreFrequency = 1

                //TODO - make analog and maybe make both sliders drag on canvas
                div("helm-slider-wrapper") {
                    p { +"Amplitude" }
                    slider("fore-amplitude", 0, 10, foreAmplitude, { 10 - it }, vertical = true) {
                        foreGraph.amplitude = it
                    }
                }
                div("helm-slider-wrapper") {
                    p { +"Frequency" }
                    slider("fore-frequency", 1, 5, foreFrequency) {
                        foreGraph.frequency = it
                    }
                }

                foreGraph = sineGraph("shield-frequency")
            }
        }
    }
}