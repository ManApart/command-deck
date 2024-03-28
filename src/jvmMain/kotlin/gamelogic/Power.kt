package gamelogic

import GameState
import frames.PowerUpdate
import sendAll
import kotlin.math.max
import kotlin.math.min

suspend fun PowerUpdate.receive() {
    var powerLeft = GameState.totalPower
    power.entries.forEach { (system, desired) ->
        val applied = min(desired, powerLeft)
        GameState.power[system] = applied
        powerLeft = max(0, powerLeft-applied)
    }
    sendAll(PowerUpdate(GameState.power))
}
