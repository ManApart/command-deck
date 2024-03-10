import frames.*
import kotlinx.browser.window
import kotlinx.html.div
import kotlinx.html.unsafe
import org.w3c.dom.HTMLElement
import views.turboLiftView
import views.updatedReadyRoom

fun WSFrame.parse() {
    println("Parsing frame $this")
    when (this) {
        is MessageFrame -> receive()
        is ServerInfoFrame -> receive()
        is ReadyRoomUpdate -> updatedReadyRoom(this)
        is GameStart -> receive()
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
    turboLiftView()
}