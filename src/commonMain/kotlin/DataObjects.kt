import kotlinx.serialization.Serializable

@Serializable
data class Player(val id: String, var name: String, var role: CrewRole = CrewRole.CREWMAN, var focused: Boolean = false)

enum class ShipSystem(val iconName: String, val powered: Boolean = true) {
    BRIDGE("captain", false),
    SENSORS("comms"),
    WARP_CORE("helm"),
    THRUSTERS("helm"),
    MED_BAY("medical"),
    WEAPONS("security"),
    SHIELDS("science"),
    ENGINES("engineering", false),
    NONE("none", false),
}

fun poweredSystems() = ShipSystem.entries.filter { it.powered }

enum class Hazard { BREACH, FIRE, NONE }

@Serializable
data class Room(
    val name: String,
    val system: ShipSystem = ShipSystem.NONE,
    var health: Int = 100,
    var fire: Int = 0,
    var breach: Int = 0,
    val players: MutableList<String> = mutableListOf()
)

@Serializable
data class ShipPosition(
    var x: Int = 50,
    var y: Int = 50,
    var heading: Int = 0,
    var desiredHeading: Int = 0,
    var sectorX: Int = 5,
    var sectorY: Int = 5,
)

@Serializable
data class Topic(val name: String, val data: String)

enum class Direction { FORE, AFT, PORT, STARBOARD }

@Serializable
data class Shield(var amplitude: Int = 0, var frequency: Int = 1)