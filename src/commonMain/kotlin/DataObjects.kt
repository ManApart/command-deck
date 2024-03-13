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

enum class Hazard { BREACH, FIRE, NONE }

//Eventually room hazards like fire or breach that have different repair minigames
@Serializable
data class Room(
    val name: String,
    val system: System = System.NONE,
    var health: Int = 100,
    var fire: Int = 0,
    var breach: Int = 0,
    val players: MutableList<String> = mutableListOf()
)