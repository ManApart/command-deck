import gamelogic.shipTravel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object GameManager {

    fun start(){
        CoroutineScope(Dispatchers.Default).launch {
            tick()
        }
    }

    private suspend fun tick(){
        shipTravel()

        delay(Config.tickRate)
        tick()
    }
}