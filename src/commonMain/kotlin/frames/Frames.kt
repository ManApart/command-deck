package frames

import CrewRole
import Hazard
import Player
import Room
import kotlinx.serialization.Serializable

@Serializable
sealed interface WSFrame

@Serializable
data class ServerInfoFrame(val url: String, val playerId: String, val playerCount: Int) : WSFrame

@Serializable
data class MessageFrame(val message: String, val alert: Boolean = false) : WSFrame

@Serializable
data class UserLoginFrame(val name: String, val role: CrewRole) : WSFrame

@Serializable
data class ReadyRoomUpdate(val crew: Map<String, Player>) : WSFrame

@Serializable
data class GameStart(val shipName: String, val players: Map<String, Player>, val rooms: Map<String, Room>, val forceStart: Boolean = false) : WSFrame

@Serializable
data class TravelFrame(val playerId: String, val destination: String) : WSFrame

@Serializable
data class RepairFrame(val roomId: String, val hazard: Hazard = Hazard.NONE, val amount: Int = 1) : WSFrame

@Serializable
data class RoomUpdate(val roomId: String, val health: Int, val breach: Int = 0, val fire: Int = 0) : WSFrame

@Serializable
data class Promotion(val playerId: String, val role: CrewRole = CrewRole.CREWMAN) : WSFrame