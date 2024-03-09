import frames.WSFrame
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.dom.addClass
import kotlinx.dom.createElement
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.HTMLElement
import org.w3c.dom.WebSocket
import views.readyRoomView

lateinit var webSocket: WebSocket

val gameState = GameState()
val playerState = PlayerState()

fun wsSend(frame: WSFrame){
    val data = jsonMapper.encodeToString(frame)
    println(data)
    webSocket.send(data)
}

val jsonMapper = Json {
    ignoreUnknownKeys = true
    encodeDefaults = false
}

val client = HttpClient {
    install(WebSockets) {
        pingInterval = 20_000
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
    install(ContentNegotiation) {
        json()
    }
}

fun main() {
    window.onload = {
        readyRoomView()
        CoroutineScope(Dispatchers.Default).launch {
            webSocket = WebSocket("ws://127.0.0.1:9090/chat").apply {
                onmessage = {
                    println("Received ${it.data}")
                    CoroutineScope(Dispatchers.Default).launch {
                        jsonMapper.decodeFromString<WSFrame>(it.data as String).parse()
                    }
                }
            }
        }
    }
}


fun el(id: String) = document.getElementById(id) as HTMLElement
fun <T> el(id: String) = document.getElementById(id) as T

fun replaceElement(id: String = "root", rootClasses: String? = null, newHtml: suspend TagConsumer<HTMLElement>.() -> Unit) {
    val root = el<HTMLElement?>(id)
    if (root != null) {
        val newRoot = document.createElement("div") {
            this.id = id
            rootClasses?.split(" ")?.forEach { this.addClass(it) }
        }
        newRoot.append {
            CoroutineScope(Dispatchers.Default).launch {
                newHtml()
            }
        }
        root.replaceWith(newRoot)
    }
}