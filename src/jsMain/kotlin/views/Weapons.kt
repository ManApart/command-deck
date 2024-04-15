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

fun weaponsShake() = listOf("helm-readout", "helm-controls", "ship-position-display")

fun weaponsView() {
    GameState.setCurrent(View.WEAPONS)
    replaceElement {
        nav()
        sparks()
        div {
            h1 { +"Weapons" }
            crewmanTitle()
            div("security") {
                targeting()
                weapons()
            }
        }
    }
}

private fun DIV.targeting() {
    div {
        id = "targeting"
        h2 { +"Targeting" }
        p {
            id = "targeted-square"
            +"Targeting (0,0)"
        }
        p {
            id = "targeted-lock"
            +"NO LOCK"
        }
        table {
            id = "targeting-table"
            val max = 7
            val offset = 4
            (0..max).forEach { y ->
                tr("helm-table-row") {
                    (0..max).forEach { x ->
                        when {
                            x + y == 0 -> td { }
                            x == 0 -> td("helm-table-heading") { +"${y - offset}" }
                            y == 0 -> td("helm-table-heading") { +"${x - offset}" }
                            x == offset && y == offset -> {
                                td {
                                    img(classes = "role-select-img") {
                                        src = "assets/icons/helm.svg"
                                    }
                                }
                            }

                            else -> td("helm-table-col") {
                                onClickFunction = { targetSquare(x - offset, y - offset) }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.weapons() {
    div {
        id = "weapons"
        h2 { +"Phasors" }
        val phasor = Wave()
        graphSection(phasor, "phasors", "Phasors", 0)
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

private fun targetSquare(x: Int, y: Int) {
    el("targeted-square").innerText = "Targeting ($x,$y)"
}