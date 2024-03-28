
fun initialRooms() = listOf(
    Room("Bridge", ShipSystem.BRIDGE),
    Room("Sensors", ShipSystem.SENSORS),
    Room("Warp Core", ShipSystem.WARP_CORE),
    Room("Med Bay", ShipSystem.MED_BAY),
    Room("Weapons", ShipSystem.WEAPONS),
    Room("Engineering", ShipSystem.ENGINES),
    Room("Shield Generator", ShipSystem.SHIELDS),
)

fun initialShields() = Direction.entries.associateWith { Shield() }