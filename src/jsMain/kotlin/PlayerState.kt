import frames.CrewRole as CrewRole

data class PlayerState(
    var id: String = "",
    var name: String = "",
    var role: CrewRole = CrewRole.CREWMAN
)