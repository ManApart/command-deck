package views

import frames.CrewRole
import gameState
import kotlinx.html.*
import kotlinx.html.js.div
import playerState
import replaceElement

fun readyRoomView(){
    replaceElement {
        div {
            h1 { +"Welcome to Command Deck" }
            p { +"Your crew can join by going to "
                code {
                    id = "ip-display"
                    +"127.0.0.1"
                }
            }
//            button {
//                +"Send Ping"
//                onClickFunction = {
//                    wsSend(MessageFrame("Test Ping at ${Date().getMilliseconds()}!"))
//                }
//            }
            div {
                input {
                    id = "username"
                    placeholder = "Your Name"
                    value = playerState.name
                }
            }
            div {
                id = "role-select"
                CrewRole.entries.forEach { role ->
                    val taken = gameState.playerRoles.containsValue(role) && gameState.playerRoles[playerState.id] != role
                    val takenClass = if(taken) "unavailable" else ""
                    div("crew-role-select $takenClass") {
                        button {
                            +role.name.split("_").joinToString(" ") { it.lowercase().capitalize() }
                            disabled = taken
                        }
                    }
                }
            }
            div {
                span { +"Ready Up" }
                label("switch") {
                    input(InputType.checkBox) {
                        //TODO - force unchecked if invalid role
                        id = "ready-up-input"
                    }
                    span("slider") {  }
                }
            }
        }
    }
}