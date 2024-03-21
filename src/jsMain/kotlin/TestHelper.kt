import GameState.players
import GameState.rooms
import frames.MessageUpdate
import frames.RoomUpdate
import frames.ShipPositionUpdate
import frames.WSFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import views.helmView
import views.scienceView

const val testing = true

val testInputFrame: WSFrame = MessageUpdate("Test scan message")

fun testView() = scienceView()

fun initializeTestingData() {
    if (testing) {
        with(playerState) {
            id = "0"
            name = "Kirk"
            role = CrewRole.SCIENCE
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
            testInputFrame.parse()
        }
    }
}