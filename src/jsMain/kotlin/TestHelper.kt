import GameState.players
import GameState.rooms
import frames.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import views.*

const val testing = true

fun testView() = lifeSignsView()

private val testInputFrames: List<WSFrame> = listOf(
//    MessageUpdate("Test scan message"),
//    DatabaseSearchResult(Topic("Test", "This is a test topic.\nHere is a second paragraph"))
//    ReadyRoomUpdate(players),
//    PowerUpdate(41, mapOf(ShipSystem.SHIELDS to 10)),
//    ShieldsUpdate(Direction.entries.associateWith { Shield(3,2) })
    RoomUpdate("Bridge", 50)
)

fun initializeTestingData() {
    if (testing) {
        with(playerState) {
            id = "0"
            name = "Kirk"
            role = CrewRole.MEDICAL
            rooms["Bridge"]?.players?.add("0")
            rooms["Bridge"]?.players?.add("1")

            players = mapOf(
                id to Player(id, name, role),
                "1" to Player("1", "Spock", CrewRole.SCIENCE, true),
                "2" to Player("2", "Checkov", CrewRole.CREWMAN),
            )
        }
    }
}

fun fireTestInputEvent() {
    if (testing) {
        CoroutineScope(Dispatchers.Default).launch {
            delay(500)
            testInputFrames.forEach { it.parse() }
        }
    }
}