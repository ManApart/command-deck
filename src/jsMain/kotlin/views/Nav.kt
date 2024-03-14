package views

import kotlinx.html.TagConsumer
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import playerState
import views.storyTeller.manageRoomsView

fun TagConsumer<HTMLElement>.nav() {
    div {
        id = "nav"
        //TODO - show name and title
        //Role specific views
        when (playerState.role) {
            CrewRole.STORY_TELLER -> storyTellerViews()
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

private fun TagConsumer<HTMLElement>.storyTellerViews(){
    button(classes = "nav-button") {
        +"Room Manager"
        onClickFunction = { manageRoomsView() }
    }
}