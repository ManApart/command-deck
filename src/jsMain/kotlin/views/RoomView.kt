package views

import GameState
import GameState.players
import Hazard
import Room
import View
import el
import elExists
import frames.RepairUpdate
import kotlinx.html.*
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import playerState
import replaceElement
import wsSend

fun roomView() {
    GameState.currentView = View.ROOM
    val room = GameState.rooms.values.firstOrNull { it.players.contains(playerState.id) }!!
    replaceElement {
        nav()
        div {
            img(classes = "room-icon") {
                src = "assets/icons/${room.system.iconName}.svg"
            }
            h1 {
                style ="display: inline-block;"
                +room.name
            }
            crewmanTitle()
            div("room") {
                id = "room-${room.name}"
                div {
                    id = "room-status"
                    p {
                        id = "room-players"
                        +"Current Crew:"
                    }
                    room.players.mapNotNull { players[it] }.forEach { player ->
                        div(classes = "crew-role-select") {
                            id = "select-${player.role.name}"
                            style = "background-color: ${player.role.color};"
                            img(classes = "role-select-img") {
                                src = "assets/icons/${player.role.name.lowercase()}.svg"
                            }
                            +"${player.role.title} ${player.name}"
                        }
                    }
                }
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
                        button {
                            id = "health-repair"
                            hidden = room.health == 100
                            +"Repair"
                            onClickFunction = {
                                wsSend(RepairUpdate(room.name))
                            }
                        }
                    }
                    div("room-repair-line") {
                        id = "room-hazard-breach"
                        hidden = room.breach == 0
                        img(classes = "room-icon") {
                            src = "assets/icons/none.svg"
                        }
                        span {
                            id = "room-hazard-breach-text"
                            +"Breach: ${room.breach}"
                        }
                        button {
                            +"Repair"
                            onClickFunction = {
                                wsSend(RepairUpdate(room.name, Hazard.BREACH))
                            }
                        }
                    }
                    div("room-repair-line") {
                        id = "room-hazard-fire"
                        hidden = room.fire == 0
                        img(classes = "room-icon") {
                            src = "assets/icons/fire.svg"
                        }
                        span {
                            id = "room-hazard-fire-text"
                            +"Fire: ${room.fire}"
                        }
                        button {
                            +"Repair"
                            onClickFunction = {
                                wsSend(RepairUpdate(room.name, Hazard.FIRE))
                            }
                        }
                    }
                }

            }

        }
    }
}

fun roomUpdate(room: Room) {
    if (elExists("room-health")) {
        el("room-health-text").innerText = "Structural Integrity: ${room.health}/100"
        el("health-repair").hidden = room.health == 100
        el("room-hazard-breach").hidden= room.breach == 0
        el("room-hazard-breach-text").innerText = "Breach: ${room.breach}"
        el("room-hazard-fire").hidden = room.fire == 0
        el("room-hazard-fire-text").innerText = "Fire: ${room.fire}"
    }
}