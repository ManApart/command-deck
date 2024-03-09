package frames

import kotlinx.serialization.Serializable

@Serializable
sealed class Frame()

@Serializable
data class ServerInfoFrame(val ipAddress: String, val playerCount: Int): Frame()