import frames.*
import gamelogic.receive

suspend fun WSFrame.parse(connection: Connection) {
    println("Parsing frame $this")
    when (this) {
        is CaptainFocus -> receive()
        is Promotion -> receive()
        is GameStart -> receive(connection)
        is MessageFrame -> receive(connection)
        is RepairFrame -> receive()
        is RoomUpdate -> receive()
        is TravelFrame -> receive(connection)
        is UserLoginFrame -> receive(connection)
        else -> {
            println("Did not recognize $this")
        }
    }
}

private suspend fun MessageFrame.receive(connection: Connection) {
    val textWithUsername = "[${connection.playerId}]: Pinged with message: $message"
    println(textWithUsername)
    sendAll(MessageFrame(textWithUsername))
}
