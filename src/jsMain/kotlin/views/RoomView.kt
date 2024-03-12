package views

import GameState
import GameState.players
import kotlinx.html.button
import kotlinx.html.id
import kotlinx.html.js.div
import kotlinx.html.js.h1
import kotlinx.html.js.img
import kotlinx.html.p
import playerState
import replaceElement

fun roomView() {
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
                p {
                    id = "room-health"
                    +"Room is ${room.health}/100"
                }
                p {
                    id = "room-players"
                    +"In this room: ${room.players.map { players[it]?.name }}"
                }
                //disabled if full health
                button {
                    +"Repair"
                }
            }

        }
    }
}