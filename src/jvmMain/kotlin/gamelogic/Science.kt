package gamelogic

import Connection
import Topic
import frames.DatabaseSearch
import frames.DatabaseSearchResult
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

val topics by lazy { parseTopics() }

suspend fun DatabaseSearch.receive(connection: Connection) {
    val search = topic.lowercase()
    val key = topics.keys.firstOrNull { it.lowercase() == search }
        ?: topics.entries.firstOrNull { (_, topic) -> topic.tags.any { it.lowercase() == search } }?.key
        ?: topics.keys.firstOrNull { it.lowercase().contains(search) }
    when {
        key == null -> connection.send(DatabaseSearchResult(Topic("None", listOf(), "No topic found for $topic")))
        topic.length < 3 -> connection.send(DatabaseSearchResult(Topic("None", listOf(), "Must search for more than 3 letters")))
        else -> connection.send(DatabaseSearchResult(topics[key]!!))
    }
}

//TODO - how do we trigger the topic list?

private fun parseTopics(): Map<String, Topic> {
    return Json.decodeFromString<List<Topic>>(File("./src/jvmMain/resources/ScienceDatabase.json").readText()).associateBy { it.name }
}