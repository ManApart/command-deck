package frames

enum class CrewRole(val color: String) {
    CAPTAIN("#b7a132"),
    COMMS("#559987"),
    ENGINEERING("#de7837"),
    HELM("#71614a"),
    MEDICAL("#1f2f5c"),
    SCIENCE("#5475b8"),
    SECURITY("#91432f"),
    CREWMAN("#b4bed0"),
    STORY_TELLER("#5e2787"),
    ;
    fun cleanName() = name.split("_").joinToString(" ") { it.lowercase().capitalize() }
}