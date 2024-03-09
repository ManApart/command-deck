package frames

import kotlinx.serialization.Serializable

@Serializable
sealed interface Frame

enum class FrameType {
    SERVER_INFO,
    MESSAGE,
}

@Serializable
data class FrameWrapper(val type: FrameType, val data: Frame)

@Serializable
data class ServerInfoFrame(val ipAddress: String, val playerCount: Int): Frame

fun serverInfoFrame(ipAddress: String, playerCount: Int) = FrameWrapper(FrameType.SERVER_INFO, ServerInfoFrame(ipAddress, playerCount))

@Serializable
data class MessageFrame(val message: String): Frame

fun messageFrame(message: String) = FrameWrapper(FrameType.MESSAGE, MessageFrame(message))
