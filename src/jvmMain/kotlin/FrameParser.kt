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
    if (role != CrewRole.CREWMAN && GameState.roleOccupied(role)) {
        connection.session.sendSerialized(ReadyRoomUpdate(GameState.players))
        return
    }
    GameState.players.putIfAbsent(connection.playerId, Player(connection.playerId, name))
    GameState.players[connection.playerId]?.name = name
    GameState.players[connection.playerId]?.role = role

    sendAll(ReadyRoomUpdate(GameState.players))
}