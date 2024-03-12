package views

import CrewRole
import GameState
import Room
import el
import frames.TravelFrame
import kotlinx.browser.window
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
import wsSend

fun turboLiftView() {
    replaceElement {
        nav()
        div {
            h1 { +"The ${GameState.shipName}" }
            GameState.rooms.values.forEach { room ->
                div("room") {
                    id = "room-${room.name}"
                    img(classes = "room-icon") {
                        src = "assets/icons/${room.system.iconName}.svg"
                    }
                    +room.name
                    img(classes = "room-icon") {
                        id = "room-${room.name}-present-icon"
                        src = "assets/icons/crewman.svg"
                        hidden = !room.players.contains(playerState.id)
                    }

                    onClickFunction = {
                        if (GameState.rooms[room.name]?.players?.contains(playerState.id) == true) {
                            window.alert("You're already in the ${room.name}!")
                        } else {
                            CoroutineScope(Dispatchers.Default).launch {
                                startTraveling(room)
                            }
                        }
                    }
                }
            }
            div {
                id = "progress-bar"
                hidden = true
                div("progress-empty") {
                    id = "progress-bar-inner"
                }
            }
        }
    }
}

private suspend fun startTraveling(room: Room) {
    wsSend(TravelFrame(playerState.id, room.name))
    val inner = el("progress-bar-inner")
    inner.removeClass("progress-full")
    el("progress-bar").hidden = false
    delay(100)
    inner.addClass("progress-full")
}

fun arrive() {
    GameState.rooms.values.forEach { room ->
        println("Room ${room.name} has crew ${room.players}")
        val show = playerState.role == CrewRole.MEDICAL || room.players.contains(playerState.id)
        el("room-${room.name}-present-icon").hidden = !show
    }
    el("progress-bar").hidden = true
    el("progress-bar-inner").removeClass("progress-full")
}