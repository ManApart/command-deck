package views

import el
import frames.CrewRole
import frames.ReadyRoomUpdate
import frames.UserLoginFrame
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.js.div
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLInputElement
import playerState
import replaceElement
import wsSend

fun readyRoomView() {
    replaceElement {
        div {
            h1 { +"Welcome to Command Deck" }
            p {
                +"Your crew can join by going to "
                code {
                    id = "ip-display"
                    +"127.0.0.1"
                }
            }
            div {
                input {
                    id = "username"
                    placeholder = "Your Name"
                    value = playerState.name
                    onChangeFunction = { unReady() }
                }
            }
            div {
                id = "role-select"
                CrewRole.entries.forEach { role ->
                    button(classes = "crew-role-select") {
                        id = "select-${role.name}"
                        +role.name.split("_").joinToString(" ") { it.lowercase().capitalize() }
                        onClickFunction = {
                            if (role != playerState.role) {
                                playerState.role = role
                                CrewRole.entries.forEach { r ->
                                    el("select-${r.name}").removeClass("selected")
                                }
                                el("select-${role.name}").addClass("selected")

                                unReady()
                            }
                        }
                    }
                }
            }
            div {
                span { +"Ready Up" }
                label("switch") {
                    input(InputType.checkBox) {
                        id = "ready-up-input"
                        onChangeFunction = {
                            if (el<HTMLInputElement>("ready-up-input").checked) {
                                updateRole()
                            }
                        }
                    }
                    span("slider") { }
                }
            }
            div {
                button {
                    id = "start-mission"
                    hidden = true
                    +"Start Mission"
                }
            }
        }
    }
}


private fun unReady() {
    el<HTMLInputElement>("ready-up-input").checked = false
}

private fun updateRole() {
    playerState.name = el<HTMLInputElement>("username").value
    wsSend(UserLoginFrame(playerState.name, playerState.role))
}

fun updatedReadyRoom(update: ReadyRoomUpdate) {
    val newRole = update.roles.entries.firstOrNull { it.value == playerState.id }?.key ?: CrewRole.CREWMAN
    if (playerState.role != newRole) {
        playerState.role = newRole
        unReady()
    }

    val buttons = CrewRole.entries.associateWith { el<HTMLButtonElement>("select-${it.name}") }

    buttons.values.forEach { button ->
        button.removeClass("confirmed")
        button.removeClass("selected")
        button.removeClass("unavailable")
        button.disabled = false
    }

    update.roles.entries.forEach { (role, id) ->
        val button = el<HTMLButtonElement>("select-${role.name}")
        if (playerState.id != id) {
            button.addClass("unavailable")
            button.disabled = true
        } else {
            button.addClass("confirmed")
        }
    }
    el<HTMLButtonElement>("start-mission").hidden = (playerState.role != CrewRole.CAPTAIN)

}