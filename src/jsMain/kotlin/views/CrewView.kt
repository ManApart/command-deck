package views

import CrewRole
import GameState
import GameState.players
import View
import frames.CaptainFocus
import frames.Promotion
import kotlinx.html.*
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import playerState
import replaceElement
import sparks
import wsSend

fun crewShake() = players.values.map { "player-${it.id}" }

fun crewView() {
    GameState.setCurrent(View.CREW)
    replaceElement {
        nav()
        sparks()
        div {
            viewTitle("Crew")
            div("crew") {
                players.values.filter { it.id != playerState.id }.forEach { player ->
                    div("crew-row") {
                        id = "player-${player.id}"
                        val focusedClass = if(player.focused) "selected" else ""
                        div("crew-role-select crew-block $focusedClass") {
                            id = "select-${player.role.name}"
                            style = "background-color: ${player.role.color};"
                            img(classes = "role-select-img") {
                                src = "assets/icons/${player.role.name.lowercase()}.svg"
                            }
                            +"${player.role.title} ${player.name}"
                            onClickFunction = {
                                wsSend(CaptainFocus(player.id))
                            }
                        }
                        if (player.role == CrewRole.CREWMAN) {
                            div {
                                p { +"Promote to:" }
                                val available = CrewRole.entries - players.values.map { it.role }.toSet()
                                available.forEach { role ->
                                    button {
                                        +role.title
                                        onClickFunction = { wsSend(Promotion(player.id, role)) }
                                    }
                                }
                            }
                        } else {
                            button {
                                +"Demote"
                                onClickFunction = { wsSend(Promotion(player.id)) }
                            }
                        }
                    }
                }
            }
        }
    }
}