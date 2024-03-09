import frames.CrewRole as CrewRole

data class GameState(
    var playerRoles: Map<String, CrewRole> = mapOf()
)

data class PlayerState(
    var id: String = "",
    var name: String = "",
    var role: CrewRole = CrewRole.CREWMAN
)