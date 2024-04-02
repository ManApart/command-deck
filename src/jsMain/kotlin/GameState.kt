val playerState = PlayerState()

object GameState {
    var shipName = "Prometheus"
    var players = mapOf<String, Player>()
    var rooms = mapOf<String, Room>()
    var totalPower = 40
    var power = mutableMapOf<ShipSystem, Int>()
    var currentView = View.TURBO_LIFT
    var shipPosition = ShipPosition()
    var shields = mapOf<Direction, Shield>()

    init {
        rooms = initialRooms().associateBy { it.name }
        shields = initialShields()
        initializeTestingData()
        val perSystem = totalPower / poweredSystems().size
        poweredSystems().forEach { power[it] = perSystem }
    }

    fun updateRooms(playerId: String, destination: String) {
        rooms.values.forEach { it.players.remove(playerId) }
        rooms[destination]?.players?.add(playerId)
    }

}

enum class View {
    CREW,
    COMMS,
    DAMAGE_CONTROL,
    ENGINEERING,
    HELM,
    MED_BAY,
    WEAPONS,
    SCIENCE,
    SHIELDS,
    READY_ROOM,
    ROOM,
    ROOM_MANAGER,
    TURBO_LIFT,
}