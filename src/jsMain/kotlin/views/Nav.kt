package views

import CrewRole
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
            CrewRole.CAPTAIN -> navButton("Crew", View.CREW) { crewView() }
            CrewRole.ENGINEERING -> {
                navButton("Engineering", View.ENGINEERING) { engineeringView() }
                navButton("Damage Control", View.DAMAGE_CONTROL) { damageControlView() }
            }

            CrewRole.HELM -> navButton("Helm", View.HELM) { helmView() }
            CrewRole.MEDICAL -> {
                navButton("Life Signs", View.LIFE_SIGNS) { lifeSignsView() }
            }

            CrewRole.SCIENCE -> {
                navButton("Science", View.SCIENCE) { scienceView() }
                navButton("Shields", View.SHIELDS) { shieldsView() }
            }

            CrewRole.SECURITY -> {
                navButton("Targeting", View.TARGETING) { targetingView() }
                navButton("Weapons", View.WEAPONS) { weaponsView() }
            }

            CrewRole.STORY_TELLER -> storyTellerViews()
            else -> {}
        }

        if (playerState.role != CrewRole.STORY_TELLER) {
            navButton("Turbo Lift", View.TURBO_LIFT) { turboLiftView() }
            navButton("Room", View.ROOM) { roomView() }
        }
    }
}

private fun View.matchClass(other: View) = if (this == other) "current-nav-view" else ""

private fun TagConsumer<HTMLElement>.navButton(name: String, view: View, nav: () -> Unit) {
    button(classes = "nav-button ${currentView.matchClass(view)}") {
        +name
        onClickFunction = { nav() }
    }
}

private fun TagConsumer<HTMLElement>.storyTellerViews() {
    button(classes = "nav-button ${currentView.matchClass(View.ROOM_MANAGER)}") {
        +"Room Manager"
        onClickFunction = { manageRoomsView() }
    }
}

fun TagConsumer<HTMLElement>.crewmanTitle() {
    p {
        id = "crewman-title"
        style = "background-color: ${playerState.role.color};"
        +"${playerState.role.title} ${playerState.name}"
    }
}

fun TagConsumer<HTMLElement>.viewTitle(title: String) {
    h1 {
        id = "view-title"
        +title
    }
}