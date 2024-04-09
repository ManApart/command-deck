package views

import GameState
import View
import kotlinx.html.*
import kotlinx.html.js.div
import replaceElement

fun damageControlView() {
    GameState.setCurrent(View.DAMAGE_CONTROL)
    replaceElement {
        nav()
        div {
            h1 { +"Damage Control" }
            crewmanTitle()
            div("engineering") {
                GameState.rooms.values.forEach { room ->
                    div("turbo-lift-room") {
                        id = "room-${room.name}"
                        img(classes = "room-icon") {
                            src = "assets/icons/${room.system.iconName}.svg"
                        }
                        +room.name

                        div {
                            id = "room-repairs"
                            div("room-repair-line") {
                                id = "room-health"
                                img(classes = "room-icon") {
                                    src = "assets/icons/medical.svg"
                                }
                                span {
                                    id = "room-health-text"
                                    +"Structural Integrity: ${room.health}/100"
                                }
                            }
                            div("room-repair-line") {
                                id = "room-hazard-breach"
                                img(classes = "room-icon") {
                                    src = "assets/icons/none.svg"
                                }
                                span {
                                    id = "room-hazard-breach-text"
                                    +"Breach: ${room.breach}"
                                }
                            }
                            div("room-repair-line") {
                                id = "room-hazard-fire"
                                img(classes = "room-icon") {
                                    src = "assets/icons/fire.svg"
                                }
                                span {
                                    id = "room-hazard-fire-text"
                                    +"Fire: ${room.fire}"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
