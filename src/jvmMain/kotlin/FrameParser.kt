import frames.*
import gamelogic.receive

suspend fun WSFrame.parse(connection: Connection) {
    println("Parsing frame $this")
    when (this) {
        is CaptainFocus -> receive()
        is DatabaseSearch -> receive(connection)
        is Promotion -> receive()
        is GameStart -> receive(connection)
        is MessageUpdate -> receive(connection)
        is RepairUpdate -> receive()
        is RoomUpdate -> receive()
        is HelmUpdate -> receive()
        is TravelUpdate -> receive(connection)
        is Scan -> receive(connection)
        is UserLogin -> receive(connection)
        else -> {
            println("Did not recognize $this")
        }
    }
}

private suspend fun MessageUpdate.receive(connection: Connection) {
    val textWithUsername = "[${connection.playerId}]: Pinged with message: $message"
    println(textWithUsername)
    sendAll(MessageUpdate(textWithUsername))
}
