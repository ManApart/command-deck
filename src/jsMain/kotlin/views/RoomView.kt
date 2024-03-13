package views

import GameState
import GameState.currentView
import GameState.players
import Hazard
import Room
import el
import frames.RepairFrame
import frames.RoomUpdate
import kotlinx.html.*
import kotlinx.html.js.div
import kotlinx.html.js.h1
import kotlinx.html.js.img
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
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
            h1 { +room.name }
            div("room") {
                id = "room-${room.name}"
                div {
                    id = "room-status"
                    p {
                        id = "room-players"
                        +"Current Crew:"
                    }
                    println("Room: ${room.players}, Game: ${GameState.players.keys}")
                    room.players.mapNotNull { players[it] }.forEach { player ->
                        div(classes = "crew-role-select") {
                            id = "select-${player.role.name}"
                            style = "background-color: ${player.role.color};"
                            img(classes = "role-select-img") {
                                src = "assets/icons/${player.role.name.lowercase()}.svg"
                            }
                            +"${player.role.cleanName()}: ${player.name}"
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
                        +"Structural Integrity: ${room.health}/100"
                        button {
                            id = "health-repair"
                            hidden = room.health == 100
                            +"Repair"
                            onClickFunction = {
                                wsSend(RepairFrame(room.name))
                            }
                        }
                    }
                    div("room-repair-line") {
                        id = "room-hazard-breach"
                        hidden = room.breach == 0
                        img(classes = "room-icon") {
                            src = "assets/icons/none.svg"
                        }
                        +"Breach: ${room.breach}"
                        button {
                            +"Repair"
                            onClickFunction = {
                                wsSend(RepairFrame(room.name, Hazard.BREACH))
                            }
                        }
                    }
                    div("room-repair-line") {
                        id = "room-hazard-fire"
                        hidden = room.fire == 0
                        img(classes = "room-icon") {
                            src = "assets/icons/fire.svg"
                        }
                        +"Fire: ${room.fire}"
                        button {
                            +"Repair"
                            onClickFunction = {
                                wsSend(RepairFrame(room.name, Hazard.FIRE))
                            }
                        }
                    }
                }

            }

        }
    }
}

fun roomUpdate(room: Room) {
    el("room-health").innerText = "Structural Integrity: ${room.health}/100"
    el("health-repair").hidden = room.health == 100
    el("room-hazard-breach").apply {
        innerText = "Breach: ${room.breach}"
        hidden = room.breach == 0
    }
    el("room-hazard-fire").apply {
        innerText = "Fire: ${room.fire}"
        hidden = room.fire == 0
    }
}