package views

import kotlinx.html.TagConsumer
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import playerState

fun TagConsumer<HTMLElement>.nav() {
    div {
        id = "nav"
        //Role specific views
        when (playerState.role) {
            else -> {}
        }
        if (playerState.role != CrewRole.STORY_TELLER) {
            button(classes = "nav-button") {
                +"Turbo Lift"
                onClickFunction = { turboLiftView() }
            }
            button(classes = "nav-button") {
                +"Room"
                onClickFunction = { roomView() }
            }
        }
    }
}