package views

import GameState.currentView
import View
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

            button(classes = "nav-button ${currentView.matchClass(View.TURBO_LIFT)}") {
                +"Turbo Lift"
                onClickFunction = { turboLiftView() }
            }
            button(classes = "nav-button ${currentView.matchClass(View.ROOM)}") {
                +"Room"
                onClickFunction = { roomView() }
            }
        }
        p { +"${playerState.role.title} ${playerState.name}" }
    }
}

private fun View.matchClass(other: View) = if(this == other) "current-nav-view" else ""

private fun TagConsumer<HTMLElement>.storyTellerViews( ){
    button(classes = "nav-button ${currentView.matchClass(View.ROOM_MANAGER)}") {
        +"Room Manager"
        onClickFunction = { manageRoomsView() }
    }
}