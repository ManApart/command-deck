package views.storyTeller

import GameState
import Room
import View
import el
import frames.RoomUpdate
import kotlinx.html.*
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import replaceElement
import views.nav
import wsSend

fun manageRoomsView() {
    GameState.currentView = View.ROOM_MANAGER
    replaceElement {
        nav()
        div {
            h1 { +"The ${GameState.shipName}" }
            div {
                id = "rooms"
                GameState.rooms.values.forEach { room ->
                    div("turbo-lift-room") {
                        id = "room-${room.name}"
                        img(classes = "room-icon") {
                            src = "assets/icons/${room.system.iconName}.svg"
                        }
                        +room.name
                        img(classes = "room-icon") {
                            id = "room-${room.name}-present-icon"
                            src = "assets/icons/crewman.svg"
                            hidden = room.players.isEmpty()
                        }
                        div("room-edits") {

                            span("edit-room-repair") {
                                +"Integrity: "
                                input(InputType.number) {
                                    id = "edit-room-health"
                                    value = room.health.toString()
                                }
                            }
                            span("edit-room-breach") {
                                +"Breach: "
                                input(InputType.number) {
                                    id = "edit-room-breach"
                                    value = room.breach.toString()
                                }
                            }
                            span("edit-room-fire") {
                                +"Fire: "
                                input(InputType.number) {
                                    id = "edit-room-fire"
                                    value = room.fire.toString()
                                }
                            }

                            button {
                                +"Update"
                                onClickFunction = { sendRoomUpdate(room) }
                            }
                        }

                    }
                }
            }
        }
    }
}

private fun sendRoomUpdate(room: Room) {
    val health = el<HTMLInputElement>("edit-room-health").value.toIntOrNull() ?: 0
    val breach = el<HTMLInputElement>("edit-room-breach").value.toIntOrNull() ?: 0
    val fire = el<HTMLInputElement>("edit-room-fire").value.toIntOrNull() ?: 0
    wsSend(RoomUpdate(room.name, health, breach, fire))
}

fun roomManagerTravelUpdate() {
    GameState.rooms.values.forEach { room ->
        el("room-${room.name}-present-icon").hidden = room.players.isEmpty()
    }
}

fun roomManagerRoomUpdate(room: Room) {
    el<HTMLInputElement>("edit-room-health").value = room.health.toString()
    el<HTMLInputElement>("edit-room-breach").value = room.breach.toString()
    el<HTMLInputElement>("edit-room-fire").value = room.fire.toString()
}