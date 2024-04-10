import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import org.w3c.dom.HTMLElement

fun View.updateHealthFX(healthChange: Int = 0, breachChange: Int = 0, fireChange: Int = 0) {
    println("Shaking room $healthChange, $breachChange $fireChange")
    val room = GameState.systemRoom(system) ?: GameState.currentRoom()
    val commonShakeables = listOf("view-title", "crewman-title", "nav")
    val shakeables = (getShakeables() + commonShakeables).mapNotNull { el<HTMLElement?>(it) }
    //Remove all existing classes
    shakeables.forEach { el ->
        el.removeClass("shake-medium-once")
    }

    //TODO - effect per type
    if (healthChange < 0 || breachChange > 0 || fireChange > 0) {
        //Show initial damage shake if new damage
        shakeables.forEach { el ->
            el.addClass("shake-medium-once")
            val delay = random.nextInt(0,10) * 30
            el.style.animationDelay = "${delay}ms"
        }
    }

    //TODO set constant affects per health of room of corresponding system

}