package views

import el
import frames.CrewRole
import frames.GameStart
import frames.ReadyRoomUpdate
import frames.UserLoginFrame
import kotlinx.browser.window
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
            h3 {
                +"Your crew can join by scanning or going to "
                code {
                    id = "ip-display"
                    +"127.0.0.1"
                }
            }
            div {
                id = "qr-code-wrapper"
                unsafe {
                    +"""<qr-code id="qr-code" contents="https://bitjson.com"></qr-code>"""
                }
            }
            div {
                span { +"Designation: " }
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
                        style = "background-color: ${role.color};"
                        img(classes = "role-select-img") {
                            src = "assets/icons/${role.name.lowercase()}.svg"
                        }

                        +role.cleanName()
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

            div("hidden") {
                id = "captain-options"
                div {
                    span { +"Ship Designation: " }
                    input {
                        id = "ship-name"
                        placeholder = "Ship's Name"
                        value = playerState.name
                    }
                }
                div {
                    button {
                        id = "start-mission"
                        +"Start Mission"
                        onClickFunction = { startGame() }
                    }
                    span { +"Drop unready Players: " }
                    input(InputType.checkBox) {
                        id = "force-start"
                    }
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
    if (playerState.name.isBlank()) {
        window.alert("You must have a name to ready up!")
        unReady()
    } else {
        wsSend(UserLoginFrame(playerState.name, playerState.role))
    }
}

private fun startGame() {
    val shipName = el<HTMLInputElement>("ship-name").value
    val forceStart = el<HTMLInputElement>("force-start").checked
    if (shipName.isBlank()) {
        window.alert("Your ship must be named before disembark")
    } else {
        wsSend(GameStart(shipName, mapOf(), mapOf(), forceStart))
    }
}

fun updatedReadyRoom(update: ReadyRoomUpdate) {
    val updatedPlayer = update.crew[playerState.id]
    if (updatedPlayer != null) {
        if (playerState.role != updatedPlayer.role) {
            playerState.role = updatedPlayer.role
            unReady()
        }
    }

    val buttons = CrewRole.entries.associateWith { el<HTMLButtonElement>("select-${it.name}") }

    buttons.entries.forEach { (role, button) ->
        button.apply {
            removeClass("confirmed")
            removeClass("selected")
            removeClass("unavailable")
            disabled = false
            innerText = role.cleanName()
        }
    }

    update.crew.values.forEach { player ->
        val button = el<HTMLButtonElement>("select-${player.role.name}")
        button.innerText = "${player.role.cleanName()}: ${player.name}"
        if (playerState.id != player.id) {
            button.addClass("unavailable")
            button.disabled = true
        } else {
            button.addClass("confirmed")
        }
    }

    if (playerState.role != CrewRole.CAPTAIN) {
        el<HTMLButtonElement>("captain-options").addClass("hidden")
    } else {
        el<HTMLButtonElement>("captain-options").removeClass("hidden")
    }

}