enum class CrewRole(val title: String, val color: String) {
    CAPTAIN("Captain", "#b7a132"),
    COMMS("Comms Officer", "#559987"),
    ENGINEERING("Chief Engineer", "#de7837"),
    HELM("Pilot", "#71614a"),
    MEDICAL("Chief Medical Officer", "#1f2f5c"),
    SCIENCE("Science Officer", "#5475b8"),
    SECURITY("Security Officer", "#91432f"),
    CREWMAN("Crewman", "#b4bed0"),
    STORY_TELLER("Story Teller", "#5e2787"),
    ;
    fun cleanName() = name.split("_").joinToString(" ") { it.lowercase().capitalize() }
}