package views

import frames.MessageFrame
import kotlinx.html.id
import kotlinx.html.js.*
import replaceElement
import wsSend
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
                    wsSend(MessageFrame("Test Ping at ${Date().getMilliseconds()}!"))
                }
            }
        }
    }
}