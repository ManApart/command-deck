package gamelogic

import Connection
import GameState
import ShipSystem
import Topic
import frames.DatabaseSearch
import frames.DatabaseSearchResult
import frames.ShieldsUpdate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.math.max
import kotlin.math.min

val topics by lazy { parseTopics() }

suspend fun DatabaseSearch.receive(connection: Connection) {
    val search = topic.lowercase()
    val key = topics.keys.firstOrNull { it.lowercase() == search }
        ?: topics.keys.firstOrNull { it.lowercase().contains(search) }
    when {
        key == null -> connection.send(DatabaseSearchResult(Topic("None", "No topic found for $topic")))
        topic.length < 3 -> connection.send(DatabaseSearchResult(Topic("None", "Must search for more than 3 letters")))
        else -> connection.send(DatabaseSearchResult(topics[key]!!))
    }
}

//TODO - how do we trigger the topic list?

private fun parseTopics(): Map<String, Topic> {
    return Json.decodeFromString<List<Topic>>(File("./src/jvmMain/resources/ScienceDatabase.json").readText()).associateBy { it.name }
}


suspend fun ShieldsUpdate.receive(connection: Connection) {
    var powerLeft = GameState.power[ShipSystem.SHIELDS] ?: 0

    shields.entries.forEach { (direction, desiredShield) ->
        val shield = GameState.shields[direction]!!
        shield.frequency = desiredShield.frequency
        val newAmplitude = min(desiredShield.amplitude, powerLeft)
        shield.amplitude = newAmplitude
        powerLeft = max(0, powerLeft - newAmplitude)
    }
    connection.send(ShieldsUpdate(GameState.shields))
}