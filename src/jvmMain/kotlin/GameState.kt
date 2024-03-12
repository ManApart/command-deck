object GameState {
    var shipName = "Prometheus"
    val players = mutableMapOf<String, Player>()
    val rooms = mutableMapOf<String, Room>()

    init {
        //Eventually load these from a ship file etc
        rooms.clear()
        initialRooms().forEach { rooms[it.name] = it }
    }

    fun roleOccupied(role: CrewRole) = players.values.any { it.role == role }

}

