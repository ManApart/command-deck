object GameState {
    var shipName = "Prometheus"
    var players = mapOf<String, Player>()
    var rooms = mapOf<String, Room>()

    init {
        rooms = initialRooms().associateBy { it.name }
        rooms["Bridge"]?.players?.add("0")
    }

    fun updateRooms(playerId: String, destination: String){
        rooms.values.forEach { it.players.remove(playerId) }
        rooms[destination]?.players?.add(playerId)
    }
}

