import GameState.shipName
import frames.GameStart

const val testing = false

suspend fun onWebsocketConnect(connection: Connection){
    if (testing && connections.size == 1){
        println("Setting up first connection for testing")
        GameState.players.putIfAbsent(connection.playerId, Player(connection.playerId, "user0"))
        GameState.players[connection.playerId]?.name = "Kirk"
        GameState.players[connection.playerId]?.role = CrewRole.CAPTAIN
        GameState.rooms["Bridge"]?.players?.add(connection.playerId)
        GameState.shipName = "Star Eagle"
        sendAll(GameStart(shipName, GameState.players, GameState.rooms))
    }
}