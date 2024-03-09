package views

import kotlinx.html.js.*
import replaceElement
import webSocket
import kotlin.js.Date

fun welcomeView(){
    replaceElement {
        div {
            h1 { +"Welcome to Command Deck" }
            p { +"To join on your phone, navigate to "
                code { +"127.0.0.1" }
            }
            button { +"Ready Room" }
            button {
                +"Send Ping"
                onClickFunction = {
                    webSocket.send("Test Ping at ${Date().getMilliseconds()}!")
                }
            }
        }
    }
}