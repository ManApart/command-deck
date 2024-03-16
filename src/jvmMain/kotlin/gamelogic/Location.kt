package gamelogic

import Connection
import GameState
import frames.HelmUpdate
import frames.ShipPositionUpdate

suspend fun HelmUpdate.receive(connection: Connection) {
    //TODO - add game tick and make this take time
    //TODO - must have power, damage system if not enough power?
    //TODO -handle warp to new sector
    //TODO - edge of map goes to new sector, edge of all sectors says turn back
    GameState.position.heading = heading
    GameState.velocity = velocity
    GameState.warpEngaged = warpEngaged
    connection.send(ShipPositionUpdate(GameState.position))
}