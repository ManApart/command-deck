import frames.WSFrame
import io.ktor.server.websocket.*
import java.util.concurrent.atomic.*

class Connection(val session: WebSocketServerSession) {
    companion object {
        val lastId = AtomicInteger(0)
    }
    val playerId = "user${lastId.getAndIncrement()}"

    suspend fun send(frame: WSFrame){
        session.sendSerialized(frame)
    }
}