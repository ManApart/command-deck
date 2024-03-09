package frames

import kotlinx.serialization.Serializable

@Serializable
sealed interface WSFrame

@Serializable
data class ServerInfoFrame(val url: String, val playerId: String, val playerCount: Int) : WSFrame

@Serializable
data class MessageFrame(val message: String) : WSFrame

@Serializable
data class UserLoginFrame(val name: String, val role: CrewRole) : WSFrame

data class ReadyRoomUpdate(val roles: Map<String, CrewRole>) : WSFrame
