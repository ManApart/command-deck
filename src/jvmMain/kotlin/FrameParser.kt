import frames.*
import io.ktor.server.websocket.*

suspend fun WSFrame.parse(connection: Connection) {
    println("Parsing frame $this")
    when (this) {
        is MessageFrame -> receive(connection)
        is UserLoginFrame -> receive(connection)
        else -> {
            println("Did not recognize $this")
        }
    }
}

private suspend fun MessageFrame.receive(connection: Connection) {
    val textWithUsername = "[${connection.playerId}]: Pinged with message: $message"
    println(textWithUsername)
    sendAll(MessageFrame(textWithUsername))
}

private suspend fun UserLoginFrame.receive(connection: Connection) {
    if (role != CrewRole.CREWMAN && GameState.roles.containsKey(role)) {
        connection.session.sendSerialized(ReadyRoomUpdate(GameState.roles))
        return
    }
    connection.playerName = name
    GameState.roles.entries.filter { it.value == connection.playerId }.forEach { old ->
        GameState.roles.remove(old.key)
    }
    GameState.roles[role] = connection.playerId
    sendAll(ReadyRoomUpdate(GameState.roles))
}