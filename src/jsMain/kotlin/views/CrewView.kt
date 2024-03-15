package views

import CrewRole
import GameState
import GameState.players
import View
import el
import frames.Promotion
import kotlinx.html.*
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLOptionElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.get
import playerState
import replaceElement
import wsSend

//TODO - click a name to apply focus
fun crewView() {
    GameState.currentView = View.CREW
    replaceElement {
        nav()
        div {
            h1 { +"Crew" }
            div("crew") {
                players.values.filter { it.id != playerState.id }.forEach { player ->
                    div(classes = "crew-row") {
                        div(classes = "crew-role-select crew-block") {
                            id = "select-${player.role.name}"
                            style = "background-color: ${player.role.color};"
                            img(classes = "role-select-img") {
                                src = "assets/icons/${player.role.name.lowercase()}.svg"
                            }
                            +"${player.role.title} ${player.name}"
                        }
                        if (player.role == CrewRole.CREWMAN) {
                            val available = CrewRole.entries - players.values.map { it.role }.toSet()
                            select {
                                id = "promote-${player.id}"
                                available.forEach { role ->
                                    option {
                                        value = role.name
                                        +role.title
                                    }
                                }
                            }
                            button {
                                +"Promote"
                                onClickFunction = {
                                    val select = el<HTMLSelectElement>("promote-${player.id}")
                                    val newRoleId = (select.options[select.selectedIndex] as HTMLOptionElement).value
                                    val role = CrewRole.valueOf(newRoleId)
                                    wsSend(Promotion(player.id, role))
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