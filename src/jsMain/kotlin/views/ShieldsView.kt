package views

import GameState
import View
import components.sineGraph
import components.slider
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.id
import kotlinx.html.js.div
import kotlinx.html.p
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
                id = "fore-shield-frequency"
                val foreAmplitude = 5
                val foreFrequency = 1
                div("helm-slider-wrapper") {
                    p { +"Amplitude" }
                    slider("fore-amplitude", 0, 10, foreAmplitude, vertical = true) {
                    }
                }
                div("helm-slider-wrapper") {
                    p { +"Frequency" }
                    slider("fore-frequency", 0, 10, foreFrequency) {
                    }
                }

                sineGraph("shield-frequency")
            }
        }
    }
}