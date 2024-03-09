import frames.FrameWrapper
import frames.serverInfoFrame
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import java.time.Duration
import java.util.*
import kotlin.collections.LinkedHashSet

val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())

val jsonMapper = Json {
    ignoreUnknownKeys = true
    encodeDefaults = false
}

fun main() {
    val usedPort = System.getenv("PORT")?.toInt() ?: 9090
    println("Command Deck started on port $usedPort")

    val environment = applicationEngineEnvironment {
        connector {
            port = usedPort
        }
        module {
            install(ContentNegotiation) {
                json()
            }
            install(Compression) {
                gzip()
            }
            install(WebSockets) {
                pingPeriod = Duration.ofSeconds(15)
                timeout = Duration.ofSeconds(15)
                maxFrameSize = Long.MAX_VALUE
                masking = false
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
            routing {
                get("/") {
                    call.respondText(
                        this::class.java.classLoader.getResource("index.html")!!.readText(),
                        ContentType.Text.Html
                    )
                }
                static("/") {
                    resources("")
                }

                webSocket("/chat") {
                    println("Adding user!")
                    val thisConnection = Connection(this)
                    connections += thisConnection
                    try {
                        sendSerialized(serverInfoFrame("123", connections.count()))
                        for (frame in incoming) {
                            frame as? Frame.Text ?: continue
                            println("Receiving frame $frame")
                            val wrapper = receiveDeserialized<FrameWrapper>()
                            println("Wrapper: $wrapper")
                            wrapper.parse(thisConnection)
                        }
                    } catch (e: Exception) {
                        println(e.localizedMessage)
                    } finally {
                        println("Removing $thisConnection!")
                        connections -= thisConnection
                    }
                }
            }
        }
    }

    embeddedServer(Netty, environment).start(wait = true)
}
