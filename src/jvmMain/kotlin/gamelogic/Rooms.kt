package gamelogic

import Connection
import clamp
import frames.MessageUpdate
import frames.RepairUpdate
import frames.RoomUpdate
import frames.TravelUpdate
import kotlinx.coroutines.delay
import sendAll


suspend fun TravelUpdate.receive(connection: Connection) {
    val player = GameState.players[playerId]
    val room = GameState.rooms[destination]
    if (player != null && room != null) {
        delay(Config.travelTime)
        GameState.rooms.values.firstOrNull { it.players.contains(playerId) }?.players?.remove(playerId)
        room.players.add(playerId)
        sendAll(TravelUpdate(playerId, destination))
    } else {
        println("Player: $player, Room: $room, players: ${GameState.players.keys}")
        connection.send(MessageUpdate("Could Not find player $playerId or room $destination."))
    }
}

suspend fun RepairUpdate.receive() {
    val room = GameState.rooms[roomId]
    if (room != null) {
        when(hazard){
            Hazard.BREACH -> room.breach -=amount
            Hazard.FIRE -> room.fire -=amount
            Hazard.NONE -> room.health +=amount
        }
        room.breach = room.breach.clamp()
        room.fire = room.fire.clamp()
        room.health = room.health.clamp()

        sendAll(RoomUpdate(room.name, room.health, room.breach, room.fire))
    }

}

suspend fun RoomUpdate.receive() {
    val room = GameState.rooms[roomId]
    if (room != null){
        room.breach = breach.clamp()
        room.fire = fire.clamp()
        room.health = health.clamp()

        sendAll(RoomUpdate(room.name, room.health, room.breach, room.fire))
    }
}