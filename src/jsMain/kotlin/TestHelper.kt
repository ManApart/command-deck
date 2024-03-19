import GameState.players
import GameState.rooms
import frames.RoomUpdate
import frames.ShipPositionUpdate
import frames.WSFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import views.helmView

const val testing = true

//val testInputFrame: WSFrame? = null
val testInputFrame: WSFrame? = ShipPositionUpdate(ShipPosition(40,60,90, 6))

fun testView() = helmView()

fun initializeTestingData() {
    if (testing) {
        with(playerState) {
            id = "0"
            name = "Kirk"
            role = CrewRole.HELM
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