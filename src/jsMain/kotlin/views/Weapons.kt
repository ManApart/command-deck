package views

import GameState
import View
import Wave
import components.graphSection
import el
import kotlinx.html.*
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import replaceElement
import sparks

fun weaponsShake() = listOf("phasors", "torpedoes")

fun weaponsView() {
    GameState.setCurrent(View.WEAPONS)
    replaceElement {
        nav()
        sparks()
        div {
            h1 { +"Weapons" }
            crewmanTitle()
            div("security") {
                p {
                    id = "targeted-lock"
                    +"NO LOCK"
                }
                weapons()
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.weapons() {
    div {
        id = "weapons"
        div {
            id = "phasors"
            h2 { +"Phasors" }
            val phasor = Wave()
            graphSection(phasor, "phasors", "Phasors", 0)
        }
        div {
            id = "torpedoes"
            h2 { +"Torpedoes" }
            p {
                id = "torpedo-display"
                +"Ammo: 10/10"
            }
            button {
                +"FIRE"
            }
        }
    }
}