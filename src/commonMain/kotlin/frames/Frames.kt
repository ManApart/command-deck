package frames

import kotlinx.serialization.Serializable

@Serializable
sealed interface WSFrame

@Serializable
data class ServerInfoFrame(val ipAddress: String, val playerCount: Int): WSFrame

@Serializable
data class MessageFrame(val message: String): WSFrame
