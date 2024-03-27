object GameState {
    var shipName = "Prometheus"
    val players = mutableMapOf<String, Player>()
    val rooms = mutableMapOf<String, Room>()
    val power = mutableMapOf<ShipSystem, Int>()
    var shields = mapOf<Direction, Shield>()
    var position = ShipPosition()
    var velocity = 0
    var warpEngaged = false

    init {
        //Eventually load these from a ship file etc
        rooms.clear()
        initialRooms().forEach { rooms[it.name] = it }
    }

    fun roleOccupied(role: CrewRole) = players.values.any { it.role == role }

    fun getPower(system: ShipSystem) = power[system] ?: 0

}

