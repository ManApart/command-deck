package views

import Config
import GameState
import View
import components.slider
import el
import frames.HelmUpdate
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.js.div
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import replaceElement
import wsSend

private var velocity = 0
private var heading = 0
private var warpEngaged = false
private const val squareSize = 40
private const val sectorWidth = Config.sectorDivisor * squareSize

fun helmShake() = listOf("helm-readout", "helm-controls", "ship-position-display")

fun helmView() {
    GameState.setCurrent(View.HELM)
    replaceElement {
        nav()
        div {
            h1 {
                id = "helm-readout"
                with(GameState.shipPosition) {
                    +"Sector ($sectorX, $sectorY)"
                }
            }
            crewmanTitle()
            div("helm") {
                div {
                    id = "helm-controls"
                    div("helm-slider-wrapper") {
                        p { +"Heading" }
                        slider("heading-slider", 0, 4, heading + 180 / 90, { it * 90 - 180 }) {
                            heading = it * 90 - 180
                            helmControlsUpdate()
                        }
                    }
                    div("helm-slider-wrapper") {
                        p { +"Velocity" }
                        slider("velocity-slider", 0, 10, velocity, {10-it}, true){
                            helmControlsUpdate()
                        }
                    }
                    div("warp-off") {
                        id = "warp-button"
                        +"Engage Warp"
                        onClickFunction = {
                            warpEngaged = !warpEngaged
                            el("warp-button").apply {
                                innerText = if (warpEngaged) {
                                    removeClass("warp-off")
                                    addClass("warp-on")
                                    "Disengage Warp"
                                } else {
                                    removeClass("warp-on")
                                    addClass("warp-off")
                                    "Engage Warp"
                                }
                            }
                            helmControlsUpdate()
                        }
                    }
                }
                div {
                    id = "ship-position-display"
                    table {
                        id = "helm-table"
                        (0..10).forEach { y ->
                            tr("helm-table-row") {
                                (0..10).forEach { x ->
                                    when {
                                        x + y == 0 -> td { }
                                        x == 0 -> td("helm-table-heading") { +"$y" }
                                        y == 0 -> td("helm-table-heading") { +"$x" }
                                        else -> td("helm-table-col") { }
                                    }
                                }
                            }
                        }
                    }
                    img(classes = "role-select-img") {
                        with(GameState.shipPosition) {
                            id = "ship-indicator"
                            src = "assets/icons/helm.svg"
                            style = "top: ${x.toScreen()}px; left: ${y.toScreen()}px; transform: rotate(${heading}deg);"
                        }
                    }
                }
            }
        }
    }
}

private fun helmControlsUpdate() {
    wsSend(HelmUpdate(heading, velocity, warpEngaged))
}

fun positionUpdate() {
    with(GameState.shipPosition) {
        el("helm-readout").innerText = "Sector ($sectorX, $sectorY)"
        el("ship-indicator").style.apply {
            top = "${sectorWidth - y.toScreen()}px"
            left = "${x.toScreen()}px"
            transform = "rotate(${heading}deg)"
        }
    }
}

private fun Int.toScreen() = this / Config.sectorDivisor * squareSize