import kotlin.random.Random

val playerState = PlayerState()
val random = Random(0)


object GameState {
    var shipName = "Prometheus"
    var players = mapOf<String, Player>()
    var rooms = mapOf<String, Room>()
    var totalPower = 40
    var power = mutableMapOf<ShipSystem, Int>()
    var currentView = View.TURBO_LIFT
        private set
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

    fun currentRoom(): Room {
        return rooms.values.firstOrNull { it.players.contains(playerState.id) } ?: rooms.values.first()
    }

    fun systemRoom(system: ShipSystem): Room? {
        return rooms.values.firstOrNull { it.system == system }
    }

    fun setCurrent(view: View) {
        currentView = view
        view.updateHealthFX()
    }

}