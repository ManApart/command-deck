import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.style
import org.w3c.dom.HTMLElement
import kotlin.js.Date
import kotlin.math.roundToInt

fun View.updateHealthFX(healthChange: Int = 0, breachChange: Int = 0, fireChange: Int = 0) {
    val room = GameState.systemRoom(system) ?: GameState.currentRoom()
    val commonShakeables = listOf("view-title", "crewman-title", "nav")
    val shakeables = (getShakeables() + commonShakeables).mapNotNull { el<HTMLElement?>(it) }
    val sparks = listOf(1, 2, 3).mapNotNull { el<HTMLElement?>("spark$it") }
    //Remove all existing classes
    shakeables.forEach { el ->
        el.removeClass("shake-medium-once")
    }

    //TODO - effect per type
    if (healthChange < 0 || breachChange > 0 || fireChange > 0) {
        //Show initial damage shake if new damage
        shakeables.forEach { el ->
            el.addClass("shake-medium-once")
            val delay = random.nextInt(0, 10) * 30
            el.style.animationDelay = "${delay}ms"
        }
        sparks.forEach { el ->
            el.addClass("spark")
        }
    }

    //TODO set constant affects per health of room of corresponding system

    CoroutineScope(Dispatchers.Default).launch {
        delay(1500)
        sparks.forEach { el ->
            el.removeClass("spark")
        }
    }
}

fun TagConsumer<HTMLElement>.sparks() {
    (1..3).forEach {
        div {
            id = "spark$it"
            val left = random.nextInt(-30,18)
            style = """
                left: ${50+left}%;
                top: ${it*20}%;
            """.trimIndent()
        }
    }
}