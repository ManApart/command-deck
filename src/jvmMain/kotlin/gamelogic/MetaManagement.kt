package gamelogic

import Connection
import Player
import connections
import frames.GameStart
import frames.MessageFrame
import frames.ReadyRoomUpdate
import frames.UserLoginFrame
import sendAll


suspend fun UserLoginFrame.receive(connection: Connection) {
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

suspend fun GameStart.receive(connection: Connection) {
    if (!forceStart && connections.any { GameState.players[it.playerId] == null }) {
        connection.send(MessageFrame("Not all players are ready!", true))
    } else {
        GameState.shipName = shipName
        sendAll(GameStart(shipName, GameState.players, GameState.rooms))
    }
}
