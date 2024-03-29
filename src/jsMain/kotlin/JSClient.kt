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
import kotlinx.coroutines.delay
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

fun wsSend(frame: WSFrame) {
    val data = jsonMapper.encodeToString(frame)
    println("Sending $data")
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
        setCssVariables()
        if (testing) testView() else readyRoomView()

        CoroutineScope(Dispatchers.Default).launch {
            webSocket = WebSocket("ws://127.0.0.1:9090/game").apply {
                onmessage = {
                    CoroutineScope(Dispatchers.Default).launch {
                        jsonMapper.decodeFromString<WSFrame>(it.data as String).parse()
                    }
                }
            }
        }
        fireTestInputEvent()
    }
}

fun el(id: String) = document.getElementById(id) as HTMLElement
fun <T> el(id: String) = document.getElementById(id) as T
fun elExists(id: String) = (document.getElementById(id) as HTMLElement?) != null

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

fun onElementExists(id: String, maxTries: Int = 100, callback: () -> Unit){
    CoroutineScope(Dispatchers.Default).launch {
        var tries = 0
        var exists = false
        while (tries < maxTries && !exists){
            exists = el<HTMLElement?>(id) != null
            tries++
            delay(100)
        }
        if (exists) callback()
    }
}

private fun setCssVariables() {
    (document.documentElement as HTMLElement).style.apply {
        setProperty("--travel-time", "${Config.travelTime}ms")
    }
}