data class PlayerState(
    var id: String = "0",
    var name: String = "",
    var role: CrewRole = CrewRole.CREWMAN,
    var focused: Boolean = false
)