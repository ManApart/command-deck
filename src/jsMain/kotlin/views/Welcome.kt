package views

import frames.messageFrame
import jsonMapper
import kotlinx.html.id
import kotlinx.html.js.*
import kotlinx.serialization.encodeToString
import replaceElement
import webSocket
import kotlin.js.Date

fun welcomeView(){
    replaceElement {
        div {
            h1 { +"Welcome to Command Deck" }
            p { +"To join on your phone, navigate to "
                code {
                    id = "ip-display"
                    +"127.0.0.1"
                }
            }
            button { +"Ready Room" }
            button {
                +"Send Ping"
                onClickFunction = {
                    val data = jsonMapper.encodeToString(messageFrame("Test Ping at ${Date().getMilliseconds()}!"))
                    println(data)
                    webSocket.send(data)
                }
            }
        }
    }
}