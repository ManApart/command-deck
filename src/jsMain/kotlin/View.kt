import views.engineeringShake

enum class View(val system: ShipSystem, val getShakeables: () -> List<String> = { emptyList() }) {
    CREW(ShipSystem.BRIDGE),
    COMMS(ShipSystem.SENSORS),
    DAMAGE_CONTROL(ShipSystem.ENGINES, ::engineeringShake),
    ENGINEERING(ShipSystem.ENGINES, ::engineeringShake),
    HELM(ShipSystem.WARP_CORE),
    MED_BAY(ShipSystem.MED_BAY),
    WEAPONS(ShipSystem.WEAPONS),
    SCIENCE(ShipSystem.SENSORS),
    SHIELDS(ShipSystem.SHIELDS),
    READY_ROOM(ShipSystem.NONE),
    ROOM(ShipSystem.NONE),
    ROOM_MANAGER(ShipSystem.NONE),
    TURBO_LIFT(ShipSystem.NONE),
}