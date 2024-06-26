package frames

import CrewRole
import Direction
import Hazard
import Player
import Room
import Wave
import ShipPosition
import ShipSystem
import Topic
import kotlinx.serialization.Serializable

@Serializable
sealed interface WSFrame

@Serializable
data class ServerInfoUpdate(val url: String, val playerId: String, val playerCount: Int) : WSFrame

@Serializable
data class MessageUpdate(val message: String, val alert: Boolean = false) : WSFrame

@Serializable
data class UserLogin(val name: String, val role: CrewRole) : WSFrame

@Serializable
data class ReadyRoomUpdate(val crew: Map<String, Player>) : WSFrame

@Serializable
data class GameStart(val shipName: String, val players: Map<String, Player>, val rooms: Map<String, Room>, val power: Map<ShipSystem, Int>, val forceStart: Boolean = false) : WSFrame

@Serializable
data class TravelUpdate(val playerId: String, val destination: String) : WSFrame

@Serializable
data class RepairUpdate(val roomId: String, val hazard: Hazard = Hazard.NONE, val amount: Int = 1) : WSFrame

@Serializable
data class RoomUpdate(val roomId: String, val health: Int, val breach: Int = 0, val fire: Int = 0) : WSFrame

@Serializable
data class Promotion(val playerId: String, val role: CrewRole = CrewRole.CREWMAN) : WSFrame

@Serializable
data class CaptainFocus(val playerId: String) : WSFrame

@Serializable
data class HelmUpdate(val heading: Int, val velocity: Int, val warpEngaged: Boolean) : WSFrame

@Serializable
data class ShipPositionUpdate(val position: ShipPosition) : WSFrame

@Serializable
data class Scan(val x: Int, val y: Int) : WSFrame

@Serializable
data class DatabaseSearch(val topic: String) : WSFrame

@Serializable
data class DatabaseSearchResult(val topic: Topic) : WSFrame

@Serializable
data class DatabaseTopics(val topics: List<String>) : WSFrame

@Serializable
data class PowerUpdate(val totalPower: Int, val power: Map<ShipSystem, Int>) : WSFrame

@Serializable
data class ShieldsUpdate(val shields: Map<Direction, Wave>) : WSFrame