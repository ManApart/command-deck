package gamelogic

import GameState
import frames.Promotion
import sendAll

suspend fun Promotion.receive() {
    GameState.players[playerId]?.role = role
    sendAll(this)
}