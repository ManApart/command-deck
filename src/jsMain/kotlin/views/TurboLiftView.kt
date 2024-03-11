package views

import GameState
import kotlinx.html.js.div
import kotlinx.html.js.h1
import replaceElement

fun turboLiftView(){
    replaceElement {
        nav()
        div {
            h1 { +"The ${GameState.shipName}" }

        }
    }
}