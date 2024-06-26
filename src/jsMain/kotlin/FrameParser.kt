import GameState.currentView
import GameState.players
import frames.*
import kotlinx.browser.window
import kotlinx.html.div
import kotlinx.html.unsafe
import org.w3c.dom.HTMLElement
import views.*
import views.storyTeller.manageRoomsView
import views.storyTeller.roomManagerRoomUpdate
import views.storyTeller.roomManagerTravelUpdate

fun WSFrame.parse() {
    println("Parsing frame $this")
    when (this) {
        is CaptainFocus -> receive()
        is DatabaseSearchResult -> receive()
        is DatabaseTopics -> receive()
        is GameStart -> receive()
        is MessageUpdate -> receive()
        is Promotion -> receive()
        is PowerUpdate -> receive()
        is ReadyRoomUpdate -> updatedReadyRoom(this)
        is RoomUpdate -> receive()
        is ServerInfoUpdate -> receive()
        is ShipPositionUpdate -> receive()
        is ShieldsUpdate -> receive()
        is TravelUpdate -> receive()
        else -> {
            println("Did not recognize $this")
        }
    }
}

private fun MessageUpdate.receive() {
    if (alert) window.alert(message)
    println("Server said: $message")
    if (currentView == View.SCIENCE) receiveMessage(message)
}

private fun ServerInfoUpdate.receive() {
    playerState.id = playerId
    if (currentView == View.READY_ROOM) {
        el<HTMLElement>("ip-display").innerText = url
        replaceElement("qr-code-wrapper") {
            div {
                unsafe {
                    +"""<qr-code id="qr-code" contents="$url"></qr-code>"""
                }
            }
        }
    }
}

private fun GameStart.receive() {
    GameState.shipName = shipName
    GameState.rooms = rooms
    GameState.players = players
    GameState.power = power.toMutableMap()
    if (playerState.role == CrewRole.STORY_TELLER) {
        manageRoomsView()
    } else turboLiftView()
}

private fun DatabaseTopics.receive() {
    if (currentView == View.SCIENCE) {
        updateTopics(topics)
    }
}

private fun DatabaseSearchResult.receive() {
    if (currentView == View.SCIENCE) {
        searchResults(topic)
    }
}

private fun TravelUpdate.receive() {
    GameState.updateRooms(playerId, destination)
    val room = GameState.rooms[destination]
    val iWasTraveler = playerId == playerState.id
    when {
        currentView == View.TURBO_LIFT && iWasTraveler -> roomView()
        currentView == View.TURBO_LIFT -> arrive()
        currentView == View.LIFE_SIGNS -> lifeSignsView()
        currentView == View.ROOM_MANAGER -> roomManagerTravelUpdate()
        room != null && currentView == View.ROOM && room.players.contains(playerState.id) -> roomUpdate(room)
    }
}

private fun RoomUpdate.receive() {
    val room = GameState.rooms[roomId]
    if (room != null) {
        val healthChange = health - room.health
        val breachChange = breach - room.breach
        val fireChange = fire - room.fire
        room.health = health
        room.breach = breach
        room.fire = fire
        if (currentView == View.ROOM && room.players.contains(playerState.id)) {
            roomUpdate(room)
        } else if (currentView == View.ROOM_MANAGER) {
            roomManagerRoomUpdate(room)
        }
        currentView.updateHealthFX(healthChange, breachChange, fireChange)
    }
}

private fun Promotion.receive() {
    GameState.players[playerId]?.role = role
    if (playerState.id == playerId) {
        roomView()
    } else if (currentView == View.CREW) {
        crewView()
    }
}

private fun CaptainFocus.receive() {
    val wasFocused = playerState.focused
    playerState.focused = playerState.id == playerId
    GameState.players.values.forEach { it.focused = false }
    GameState.players[playerId]?.focused = true

    if (currentView == View.CREW) crewView()

    if (playerState.focused != wasFocused) {
        //TODO - update crew views
    }
}

private fun ShipPositionUpdate.receive() {
    GameState.shipPosition = position
    if (currentView == View.HELM) {
        positionUpdate()
    }
}

private fun PowerUpdate.receive() {
    GameState.totalPower = totalPower
    GameState.power = power.toMutableMap()
    println("Receive power $currentView")
    when (currentView) {
        View.SHIELDS -> updateShields()
        View.ENGINEERING -> updatePower()
        else -> {}
    }
}

private fun ShieldsUpdate.receive() {
    GameState.shields = shields
    when (currentView) {
        View.SHIELDS -> updateShields()
        else -> {}
    }
}