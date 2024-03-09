import frames.MessageFrame
import frames.ReadyRoomUpdate
import frames.ServerInfoFrame
import frames.WSFrame
import org.w3c.dom.HTMLElement
import views.updatedReadyRoom

fun WSFrame.parse() {
    println("Parsing frame $this")
    when (this) {
        is MessageFrame -> this.receive()
        is ServerInfoFrame -> this.receive()
        is ReadyRoomUpdate -> updatedReadyRoom(this)
        else -> {
            println("Did not recognize $this")
        }
    }
}

private fun MessageFrame.receive() {
    println("Server said: $message")
}

private fun ServerInfoFrame.receive() {
    playerState.id = playerId
    el<HTMLElement>("ip-display").innerText = url
}
