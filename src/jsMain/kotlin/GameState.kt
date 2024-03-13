val playerState = PlayerState()

object GameState {
    var shipName = "Prometheus"
    var players = mapOf<String, Player>()
    var rooms = mapOf<String, Room>()
    var currentView = View.TURBO_LIFT

    init {
        rooms = initialRooms().associateBy { it.name }
        initializeTestingData()
    }

    fun updateRooms(playerId: String, destination: String) {
        rooms.values.forEach { it.players.remove(playerId) }
        rooms[destination]?.players?.add(playerId)
    }

    private fun initializeTestingData() {
        if (testing) {
            with(playerState) {
                id = "0"
                name = "Kirk"
                role = CrewRole.STORY_TELLER
                players = mapOf(id to Player(id, name, role))
                rooms["Bridge"]?.players?.add("0")
            }
        }
    }
}

enum class View {
    TURBO_LIFT,
    READY_ROOM,
    ROOM,
    ROOM_MANAGER,
}