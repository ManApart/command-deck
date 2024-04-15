package views

import GameState
import ShipSystem
import View
import Wave
import components.graphSection
import el
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import poweredSystems
import replaceElement
import sparks

fun targetingShake() = listOf("targeting", "room-targeting")

fun targetingView() {
    GameState.setCurrent(View.TARGETING)
    replaceElement {
        nav()
        sparks()
        div {
            h1 { +"Targeting" }
            crewmanTitle()
            div("security") {
                targeting()
                roomTargeting()
            }
        }
    }
}

private fun DIV.targeting() {
    div {
        id = "targeting"
        h2 { +"Ship Targets" }
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

private fun TagConsumer<HTMLElement>.roomTargeting() {
    div {
        id = "room-targeting"
        h2 { +"System Targets" }

        //TODO - only show when lock present
        div {
            id = "system-targets"
            poweredSystems().forEach { system ->
                div("system-target") {
                    id = "target-${system.name}"
                    img(classes = "system-power-icon") {
                        src = "assets/icons/${system.iconName}.svg"
                    }
                    +system.name
                    onClickFunction = { targetSystem(system) }
                }
            }
        }
    }
}

private fun targetSquare(x: Int, y: Int) {
    el("targeted-square").innerText = "Targeting Relative ($x,$y)"
}

private fun targetSystem(system: ShipSystem) {
    poweredSystems().forEach { el("target-${it.name}").removeClass("targeted") }
    el("target-${system.name}").addClass("targeted")
}