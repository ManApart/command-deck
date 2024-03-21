package views

import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.js.div
import replaceElement

fun shieldsView() {
    GameState.currentView = View.SHIELDS
    replaceElement {
        nav()
        div {
            h1 {
                    +"Shields"
            }
            crewmanTitle()
            div("shields") {

            }
        }
    }
}