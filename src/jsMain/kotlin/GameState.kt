import frames.CrewRole
import frames.Player
import frames.Room
import frames.initialRooms

object GameState {
    var shipName = "Prometheus"
    var players = mapOf<String, Player>()
    var rooms = mapOf<String, Room>()

    init {
        initialRooms().associateBy {it.name}
    }

    fun roleOccupied(role: CrewRole) = players.values.any { it.role == role }

}

