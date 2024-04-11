import views.*

enum class View(val system: ShipSystem, val getShakeables: () -> List<String> = { emptyList() }) {
    CREW(ShipSystem.BRIDGE, ::crewShake),
    COMMS(ShipSystem.SENSORS),
    DAMAGE_CONTROL(ShipSystem.ENGINES, ::damageControlShake),
    ENGINEERING(ShipSystem.ENGINES, ::engineeringShake),
    HELM(ShipSystem.WARP_CORE, ::helmShake),
    LIFE_SIGNS(ShipSystem.MED_BAY, ::lifeSignsShake),
    MED_BAY(ShipSystem.MED_BAY),
    WEAPONS(ShipSystem.WEAPONS),
    SCIENCE(ShipSystem.SENSORS, ::scienceShake),
    SHIELDS(ShipSystem.SHIELDS, ::shieldsShake),
    READY_ROOM(ShipSystem.NONE),
    ROOM(ShipSystem.NONE, ::roomShake),
    ROOM_MANAGER(ShipSystem.NONE),
    TURBO_LIFT(ShipSystem.NONE, ::turboLiftShake),
}