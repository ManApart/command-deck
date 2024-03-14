import GameState.players
import GameState.rooms
import frames.RoomUpdate
import frames.WSFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val testing = true

val testInputFrame: WSFrame? = null
//val testInputFrame: WSFrame? = RoomUpdate("Bridge", 50, 10, 11)

fun initializeTestingData() {
    if (testing) {
        with(playerState) {
            id = "0"
            name = "Kirk"
            role = CrewRole.CAPTAIN
            players = mapOf(id to Player(id, name, role))
            rooms["Bridge"]?.players?.add("0")
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