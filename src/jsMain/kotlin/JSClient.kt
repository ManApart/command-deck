import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.html.dom.append
import kotlinx.html.js.h2
import org.w3c.dom.HTMLElement


val client = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

fun main() {
    window.onload = {
        el("root").append {
            h2 {
                +"Command Deck"
            }
        }
        CoroutineScope(Dispatchers.Default).launch {
        }
    }
}


fun el(id: String) = document.getElementById(id) as HTMLElement
fun <T> el(id: String) = document.getElementById(id) as T