import kotlinx.serialization.Serializable

@Serializable
data class Player(val id: String, var name: String, var role: CrewRole = CrewRole.CREWMAN)

enum class System(val iconName: String) {
    BRIDGE("captain"),
    SENSORS("comms"),
    WARP_CORE("helm"),
    MED_BAY("medical"),
    WEAPONS("security"),
    SHIELDS("science"),
    ENGINES("engineering"),
    NONE("none"),
}

@Serializable
data class Room(
    val name: String,
    val system: System = System.NONE,
    var health: Int = 100,
    val players: MutableList<String> = mutableListOf()
)