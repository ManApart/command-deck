package views

import Config
import GameState
import GameState.power
import GameState.totalPower
import ShipSystem
import View
import el
import frames.PowerUpdate
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.js.div
import kotlinx.html.js.img
import kotlinx.html.js.onClickFunction
import playerState
import replaceElement
import wsSend

fun engineeringView() {
    GameState.currentView = View.CREW
    replaceElement {
        nav()
        div {
            h1 { +"Engineering" }
            div("engineering") {
                div {
                    id = "power-distribution"
                    val availablePower = totalPower - power.values.sum()

                    p {
                        id = "current-power-display"
                        +"Power: $availablePower/$totalPower"
                    }

                    power.forEach { (system, powerLevel) ->
                        div("system-power-row") {
                            id = "system-power-${system.name}"

                            img(classes = "system-power-icon") {
                                src = "assets/icons/${system.iconName}.svg"
                            }

                            val systemLimit = Config.maxPowerPerSystem[system] ?: 0

                            div("system-power-pip-wrapper") {
                                (0..systemLimit).forEach { i ->
                                    val poweredClass = pipPowerClass(i, powerLevel)

                                    div("power-pip $poweredClass") {
                                        id = "${system.name}-power-pip-$i"
                                        onClickFunction = { setPower(system, i)}
                                    }
                                }
                                div("system-power-name") {
                                    +system.name
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

private fun pipPowerClass(i: Int, powerLevel: Int): String {
    return when {
        i == 0 -> "base-pip"
        i > powerLevel -> ""
        i > 8 -> "powered-pip-high"
        i > 5 -> "powered-pip-med"
        else -> "powered-pip-low"
    }
}

fun setPower(system: ShipSystem, level: Int){
    (1..(Config.maxPowerPerSystem[system]?: 0)).forEach { i ->
        val pip = el("${system.name}-power-pip-$i")
        val poweredClass = pipPowerClass(i, level)
        if (i <= level){
            pip.addClass(poweredClass)
        } else {
            pip.removeClass("powered-pip-low")
            pip.removeClass("powered-pip-medium")
            pip.removeClass("powered-pip-high")
        }
    }
    power[system] = level
    wsSend(PowerUpdate(power))
}