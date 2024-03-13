package gamelogic

import Connection
import clamp
import frames.MessageFrame
import frames.RepairFrame
import frames.RoomUpdate
import frames.TravelFrame
import kotlinx.coroutines.delay
import sendAll


suspend fun TravelFrame.receive(connection: Connection) {
    val player = GameState.players[playerId]
    val room = GameState.rooms[destination]
    if (player != null && room != null) {
        delay(Config.travelTime)
        GameState.rooms.values.firstOrNull { it.players.contains(playerId) }?.players?.remove(playerId)
        room.players.add(playerId)
        sendAll(TravelFrame(playerId, destination))
    } else {
        println("Player: $player, Room: $room, players: ${GameState.players.keys}")
        connection.send(MessageFrame("Could Not find player $playerId or room $destination."))
    }
}

suspend fun RepairFrame.receive() {
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