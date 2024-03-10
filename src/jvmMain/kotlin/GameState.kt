import frames.CrewRole
import frames.Player

object GameState {
    val players: MutableMap<String, Player> = mutableMapOf()
//    val roles: MutableMap<CrewRole, String> = mutableMapOf()

    fun roleOccupied(role: CrewRole) = players.values.any { it.role == role }
}

