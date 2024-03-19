package gamelogic

import Config
import GameState
import ShipSystem
import frames.HelmUpdate
import frames.ShipPositionUpdate
import sendAll
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun HelmUpdate.receive() {
    GameState.position.heading = heading
    GameState.velocity = velocity
    GameState.warpEngaged = warpEngaged
}

suspend fun shipTravel() {
    if (GameState.warpEngaged) {
        shipTravel(ShipSystem.WARP_CORE, 10)
    } else {
        shipTravel(ShipSystem.THRUSTERS)
    }

    sendAll(ShipPositionUpdate(GameState.position))
}

private fun shipTravel(powerType: ShipSystem, velocityMultiplier: Int = 1) {
    val power = GameState.getPower(powerType)
    if (GameState.velocity > power) {
        GameState.velocity = 0
        //TODO - must have power, damage system if not enough power?
        //Damage System because not enough power?
    } else {
        with(GameState.position) {
            val velocity = GameState.velocity * velocityMultiplier
            //TODO - ship angle should take time
            val angle = GameState.position.heading.toRadians()
            val newX = x + (velocity * sin(angle))
            val newY = y + (velocity * cos(angle))
            val newSectorX = sectorX
            val newSectorY = sectorY
            val (cleanX, cleanSectorX) = crossSector(newX.toInt(), newSectorX)
            val (cleanY, cleanSectorY) = crossSector(newY.toInt(), newSectorY)
            x = cleanX
            y = cleanY
            sectorX = cleanSectorX
            sectorY = cleanSectorY
        }
    }
}

private fun crossSector(pos: Int, sector: Int): Pair<Int, Int> {
    var newPos = pos
    var newSector = sector
    if (pos > Config.sectorDistance) {
        if (newSector + 1 > Config.maxSectors) {
            newPos = Config.sectorDistance
        } else {
            newPos -= Config.sectorDistance
            newSector += 1
        }
    } else if (pos < 0) {
        if (newSector - 1 < 0) {
            newPos = 0
        } else {
            newPos += Config.sectorDistance
            newSector -= 1
        }
    }
    return Pair(newPos, newSector)
}


private fun shipWarpTravel() {

}

fun Int.toRadians(): Double = this / 180.0 * PI

fun Double.toDegrees(): Double = this * 180.0 / PI