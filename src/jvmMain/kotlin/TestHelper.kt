import GameState.shipName
import frames.GameStart
import frames.ReadyRoomUpdate

const val testing = true

suspend fun onWebsocketConnect(connection: Connection){
    if (testing && connections.size == 1){
        println("Setting up first connection for testing")
        GameState.players.putIfAbsent(connection.playerId, Player(connection.playerId, "user0"))
        GameState.players[connection.playerId]?.name = "Kirk"
        GameState.players[connection.playerId]?.role = CrewRole.HELM
        GameState.rooms["Bridge"]?.players?.add(connection.playerId)
        GameState.shipName = "Star Eagle"
        connection.send(ReadyRoomUpdate(GameState.players))
        sendAll(GameStart(shipName, GameState.players, GameState.rooms))

        //TODO actual power management
        ShipSystem.entries.forEach { GameState.power[it] = 10 }
    }
}