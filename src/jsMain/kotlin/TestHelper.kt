import GameState.players
import GameState.rooms
import frames.WSFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val testing = false

val testInputFrame: WSFrame? = null
//val testInputFrame: WSFrame? = RoomUpdate("Bridge", 50, 10, 11)

fun initializeTestingData() {
    if (testing) {
        with(playerState) {
            id = "0"
            name = "Kirk"
            role = CrewRole.CAPTAIN
            rooms["Bridge"]?.players?.add("0")
            rooms["Bridge"]?.players?.add("1")

            players = mapOf(
                id to Player(id, name, role),
                "1" to Player("1", "Spock", CrewRole.SCIENCE, true),
                "2" to Player("1", "Checkov", CrewRole.CREWMAN),
            )
        }
    }
}

fun fireTestInputEvent() {
    if (testing) {
        CoroutineScope(Dispatchers.Default).launch {
            delay(500)
            testInputFrame?.parse()
        }
    }
}