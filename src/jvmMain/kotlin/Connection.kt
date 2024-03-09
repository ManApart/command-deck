import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.util.concurrent.atomic.*

class Connection(val session: WebSocketServerSession) {
    companion object {
        val lastId = AtomicInteger(0)
    }
    val name = "user${lastId.getAndIncrement()}"
    var playerName = ""
}