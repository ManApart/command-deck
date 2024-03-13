package views.storyTeller

import CrewRole
import GameState
import Room
import components.progressBar
import el
import frames.TravelFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.hidden
import kotlinx.html.id
import kotlinx.html.js.div
import kotlinx.html.js.h1
import kotlinx.html.js.img
import kotlinx.html.js.onClickFunction
import playerState
import replaceElement
import views.nav
import views.roomView
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

                        onClickFunction = {
                            //TODO - edit room stuffs
                        }
                    }
                }
            }
        }
    }
}

//TODO - travel and room updates
fun arrive() {
    GameState.rooms.values.forEach { room ->
        println("Room ${room.name} has crew ${room.players}")
        val show = playerState.role == CrewRole.MEDICAL || room.players.contains(playerState.id)
        el("room-${room.name}-present-icon").hidden = !show
    }
    el("progress-bar").hidden = true
    el("progress-bar-inner").removeClass("progress-full")
}