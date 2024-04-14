package views

import GameState
import GameState.players
import View
import kotlinx.html.*
import kotlinx.html.js.div
import replaceElement
import sparks

fun lifeSignsShake() = GameState.rooms.values.map { "room-${it.name}" }

fun lifeSignsView() {
    GameState.setCurrent(View.LIFE_SIGNS)
    replaceElement {
        nav()
        sparks()
        div {
            viewTitle("The ${GameState.shipName}")
            crewmanTitle()
            div {
                id = "rooms"
                GameState.rooms.values.forEach { room ->
                    div("room") {
                        id = "room-${room.name}"
                        img(classes = "room-icon") {
                            src = "assets/icons/${room.system.iconName}.svg"
                        }
                        +room.name
                        div {
                            id = "room-status"
                            room.players.mapNotNull { players[it] }.forEach { player ->
                                div(classes = "crew-role-select") {
                                    id = "player-${player.id}"
                                    style = "background-color: ${player.role.color};"
                                    img(classes = "role-select-img") {
                                        src = "assets/icons/${player.role.name.lowercase()}.svg"
                                    }
                                    +"${player.role.title} ${player.name}"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
//TODO - show warning if player has  condition