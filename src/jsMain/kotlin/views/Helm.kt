package views

import Config
import GameState
import View
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

fun helmView() {
    GameState.currentView = View.HELM
    replaceElement {
        nav()
        div {
            p {
                id = "helm-readout"
                with(GameState.shipPosition) {
                    +"Sector ($sectorX, $sectorY)"
                }
            }
            div("helm") {
                div {
                    id = "helm-controls"
                    div("helm-slider-wrapper") {
                        p { +"Heading" }
                        input(InputType.range) {
                            id = "heading-slider"
                            min = "0"
                            max = "4"
                            value = ((heading + 180 / 90)).toString()
                            list = "heading-ticks"
                            onChangeFunction = {
                                val tickValue = el<HTMLInputElement>("heading-slider").value.toIntOrNull() ?: 0
                                heading = tickValue * 90 - 180
                                helmControlsUpdate()
                            }
                        }
                        dataList {
                            id = "heading-ticks"
                            (0..4).forEach {
                                option {
                                    value = "$it"
                                    label = "${it * 90 - 180}"
                                }
                            }
                        }
                    }
                    div("helm-slider-wrapper") {
                        p { +"Velocity" }
                        input(InputType.range) {
                            id = "velocity-slider"
                            min = "0"
                            max = "10"
                            value = velocity.toString()
                            list = "velocity-ticks"
                            attributes["orient"] = "vertical"
                            onChangeFunction = {
                                velocity = el<HTMLInputElement>("velocity-slider").value.toIntOrNull() ?: 0
                                helmControlsUpdate()
                            }
                        }
                        dataList {
                            id = "velocity-ticks"
                            (0..10).forEach {
                                option {
                                    value = it.toString()
                                    label = "${10 - it}"
                                }
                            }
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
                    //TODO - add labels 1-10
                    table {
                        id = "helm-table"
                        (0..10).forEach { _ ->
                            tr("helm-table-row") {
                                (0..10).forEach { _ ->
                                    td("helm-table-col") {
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