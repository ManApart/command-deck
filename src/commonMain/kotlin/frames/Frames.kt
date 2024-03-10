package frames

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
