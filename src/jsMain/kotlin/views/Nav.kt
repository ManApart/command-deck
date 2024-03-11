package views

import kotlinx.html.TagConsumer
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.id
import org.w3c.dom.HTMLElement
import playerState

fun TagConsumer<HTMLElement>.nav() {
    div {
        id = "nav"
        //Role specific views
        when(playerState.role){
            else -> {}
        }
        button(classes = "nav-button") {
            +"Turbo Lift"
        }
        button(classes = "nav-button") {
            +"Room"
        }

    }
}