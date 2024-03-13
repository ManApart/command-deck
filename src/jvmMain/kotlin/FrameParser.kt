import frames.*
import gamelogic.receive

suspend fun WSFrame.parse(connection: Connection) {
    println("Parsing frame $this")
    when (this) {
        is MessageFrame -> receive(connection)
        is UserLoginFrame -> receive(connection)
        is GameStart -> receive(connection)
        is TravelFrame -> receive(connection)
        is RepairFrame -> receive()
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
