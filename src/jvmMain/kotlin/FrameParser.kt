import frames.MessageFrame
import frames.WSFrame
import io.ktor.server.websocket.*

suspend fun WSFrame.parse(connection: Connection) {
    println("Parsing frame $this")
    when (this) {
        is MessageFrame -> receive(connection)
        else -> {
            println("Did not recognize $this")
        }
    }
}

private suspend fun MessageFrame.receive(connection: Connection) {
    val textWithUsername = "[${connection.name}]: Pinged with message: $message"
    println(textWithUsername)
    connections.forEach {
        it.session.sendSerialized(MessageFrame(textWithUsername))
    }
}