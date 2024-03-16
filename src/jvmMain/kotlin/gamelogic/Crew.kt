package gamelogic

import GameState
import frames.CaptainFocus
import frames.Promotion
import sendAll

suspend fun CaptainFocus.receive() {
    GameState.players.values.forEach { it.focused = false }
    GameState.players[playerId]?.focused = true
    sendAll(this)
}

suspend fun Promotion.receive() {
    GameState.players[playerId]?.role = role
    sendAll(this)
}

