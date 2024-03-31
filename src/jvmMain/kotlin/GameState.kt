object GameState {
    var shipName = "Prometheus"
    val players = mutableMapOf<String, Player>()
    val rooms = mutableMapOf<String, Room>()
    var totalPower = 40
    val power = mutableMapOf<ShipSystem, Int>()
    var shields = mapOf<Direction, Shield>()
    var position = ShipPosition()
    var velocity = 0
    var warpEngaged = false

    init {
        //Eventually load these from a ship file etc
        rooms.clear()
        shields = initialShields()
        initialRooms().forEach { rooms[it.name] = it }
        val perSystem = totalPower / ShipSystem.entries.size
        ShipSystem.entries.forEach { power[it] = perSystem }
    }

    fun roleOccupied(role: CrewRole) = players.values.any { it.role == role }

    fun getPower(system: ShipSystem) = power[system] ?: 0

}

