import kotlinx.serialization.Serializable

@Serializable
data class Player(val id: String, var name: String, var role: CrewRole = CrewRole.CREWMAN, var focused: Boolean = false)

enum class ShipSystem(val iconName: String) {
    BRIDGE("captain"),
    SENSORS("comms"),
    WARP_CORE("helm"),
    THRUSTERS("helm"),
    MED_BAY("medical"),
    WEAPONS("security"),
    SHIELDS("science"),
    ENGINES("engineering"),
    NONE("none"),
}

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
    var sectorX: Int = 5,
    var sectorY: Int = 5,
)