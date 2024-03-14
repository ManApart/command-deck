package views

import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import playerState
import views.storyTeller.manageRoomsView

fun TagConsumer<HTMLElement>.nav() {
    div {
        id = "nav"
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
        p { +"${playerState.role.title} ${playerState.name}" }
    }
}

private fun TagConsumer<HTMLElement>.storyTellerViews(){
    button(classes = "nav-button") {
        +"Room Manager"
        onClickFunction = { manageRoomsView() }
    }
}