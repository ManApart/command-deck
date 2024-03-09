import frames.FrameType
import frames.FrameWrapper
import frames.MessageFrame
import frames.messageFrame
import io.ktor.server.websocket.*

suspend fun FrameWrapper.parse(connection: Connection) {
    println("Parsing frame $this")
    when (type) {
        FrameType.MESSAGE -> (data as MessageFrame).receive(connection)
        else -> {}
    }
}

private suspend fun MessageFrame.receive(connection: Connection) {
    val textWithUsername = "[${connection.name}]: Pinged with message: $message"
    println(textWithUsername)
    connections.forEach {
        it.session.sendSerialized(messageFrame(textWithUsername))
    }
}