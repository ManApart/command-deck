import frames.ReadyRoomUpdate
import frames.ServerInfoFrame
import frames.WSFrame
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
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.DatagramSocket
import java.net.InetAddress
import java.time.Duration
import java.util.*

const val testing = true
val connections: MutableSet<Connection> = Collections.synchronizedSet(LinkedHashSet())

val jsonMapper = Json {
    ignoreUnknownKeys = true
    encodeDefaults = false
}

fun main() {
    val usedPort = java.lang.System.getenv("PORT")?.toInt() ?: 9090
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

                webSocket("/game") {
                    println("Adding user!")
                    val thisConnection = Connection(this)
                    connections += thisConnection
                    try {
                        var ip = ""
                        DatagramSocket().use { datagramSocket ->
                            datagramSocket.connect(InetAddress.getByName("8.8.8.8"), 12345)
                            ip = datagramSocket.localAddress.hostAddress
                        }
                        val url = "http://${ip}:$usedPort"
                        sendSerialized(ServerInfoFrame(url, thisConnection.playerId,  connections.count()) as WSFrame)
                        sendSerialized(ReadyRoomUpdate(GameState.players) as WSFrame)
                        onWebsocketConnect(thisConnection)
                        for (frame in incoming) {
                            frame as? Frame.Text ?: continue
                            jsonMapper.decodeFromString<WSFrame>(frame.readText()).parse(thisConnection)
                        }
                    } catch (e: Exception) {
                        println(e.localizedMessage)
                    } finally {
                        println("Removing connection for ${thisConnection.playerId}!")
                        GameState.players.remove(thisConnection.playerId)
                        connections -= thisConnection
                    }
                }
            }
        }
    }

    embeddedServer(Netty, environment).start(wait = true)
}


suspend fun sendAll(wsFrame: WSFrame){
    connections.forEach {
        it.session.sendSerialized(wsFrame)
    }
}