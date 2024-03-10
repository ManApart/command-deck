package frames

import kotlinx.serialization.Serializable

@Serializable
data class Player(val id: String, var name: String, var role: CrewRole = CrewRole.CREWMAN)