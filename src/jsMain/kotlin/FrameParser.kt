import GameState.currentView
import frames.*
import kotlinx.browser.window
import kotlinx.html.div
import kotlinx.html.unsafe
import org.w3c.dom.HTMLElement
import views.arrive
import views.roomUpdate
import views.storyTeller.manageRoomsView
import views.storyTeller.roomManagerRoomUpdate
import views.storyTeller.roomManagerTravelUpdate
import views.turboLiftView
import views.updatedReadyRoom

fun WSFrame.parse() {
    println("Parsing frame $this")
    when (this) {
        is GameStart -> receive()
        is MessageFrame -> receive()
        is ReadyRoomUpdate -> updatedReadyRoom(this)
        is RoomUpdate -> receive()
        is ServerInfoFrame -> receive()
        is TravelFrame -> receive()
        else -> {
            println("Did not recognize $this")
        }
    }
}

private fun MessageFrame.receive() {
    if (alert) window.alert(message)
    println("Server said: $message")
}

private fun ServerInfoFrame.receive() {
    playerState.id = playerId
    el<HTMLElement>("ip-display").innerText = url
    replaceElement("qr-code-wrapper") {
        div {
            unsafe {
                +"""<qr-code id="qr-code" contents="$url"></qr-code>"""
            }
        }
    }
}

private fun GameStart.receive() {
    GameState.shipName = shipName
    GameState.rooms = rooms
    GameState.players = players
    if (playerState.role == CrewRole.STORY_TELLER) {
        manageRoomsView()
    } else turboLiftView()
}

private fun TravelFrame.receive() {
    GameState.updateRooms(playerId, destination)
    if (currentView == View.TURBO_LIFT) {
        arrive()
    } else if (currentView == View.ROOM_MANAGER) {
        roomManagerTravelUpdate()
    }
}

private fun RoomUpdate.receive() {
    val room = GameState.rooms[roomId]
    if (room != null) {
        room.health = health
        room.breach = breach
        room.fire = fire
        if (currentView == View.ROOM && room.players.contains(playerState.id)) {
            roomUpdate(room)
        } else if (currentView == View.ROOM_MANAGER) {
            roomManagerRoomUpdate(room)
        }
    }
}