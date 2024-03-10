package frames

import kotlinx.serialization.Serializable

@Serializable
data class Player(val id: String, var name: String, var role: CrewRole = CrewRole.CREWMAN)

enum class System {
    SHIELDS,
    ENGINES,
    NONE
}

@Serializable
data class Room(
    val name: String,
    val system: System = System.NONE,
    var health: Int = 100,
    val players: MutableList<String> = mutableListOf()
)