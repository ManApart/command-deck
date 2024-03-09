import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
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
import kotlinx.html.js.h2
import org.w3c.dom.HTMLElement
import org.w3c.dom.WebSocket
import views.welcomeView

lateinit var webSocket: WebSocket

val client = HttpClient {
    install(WebSockets) {
        pingInterval = 20_000
    }
    install(ContentNegotiation) {
        json()
    }
}

fun main() {
    window.onload = {
        welcomeView()
        CoroutineScope(Dispatchers.Default).launch {
            webSocket = WebSocket("ws://127.0.0.1:9090/chat").apply {
                onmessage = {
                    println("Received ${it.data}")
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