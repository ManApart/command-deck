import frames.*
import kotlinx.coroutines.delay

suspend fun WSFrame.parse(connection: Connection) {
    println("Parsing frame $this")
    when (this) {
        is MessageFrame -> receive(connection)
        is UserLoginFrame -> receive(connection)
        is GameStart -> receive(connection)
        is TravelFrame -> receive(connection)
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
        connection.send(ReadyRoomUpdate(GameState.players))
        return
    }
    GameState.players.putIfAbsent(connection.playerId, Player(connection.playerId, name))
    GameState.players[connection.playerId]?.name = name
    GameState.players[connection.playerId]?.role = role
    GameState.rooms["Bridge"]?.players?.add(connection.playerId)

    sendAll(ReadyRoomUpdate(GameState.players))
}

private suspend fun GameStart.receive(connection: Connection) {
    if (!forceStart && connections.any { GameState.players[it.playerId] == null }) {
        connection.send(MessageFrame("Not all players are ready!", true))
    } else {
        GameState.shipName = shipName
        sendAll(GameStart(shipName, GameState.players, GameState.rooms))
    }
}

private suspend fun TravelFrame.receive(connection: Connection) {
    val player = GameState.players[playerId]
    val room = GameState.rooms[destination]
    if (player != null && room != null){
        delay(Config.travelTime)
        sendAll(TravelFrame(playerId, destination))
    } else {
        println("Player: $player, Room: $room, players: ${GameState.players.keys}")
        connection.send(MessageFrame("Could Not find player $playerId or room $destination."))
    }
}
