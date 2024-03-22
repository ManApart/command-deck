package tools

import Topic
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

fun main() {
    File("./dataEntries").listFiles()!!
        .map { it.readText() }
        .flatMap { it.parseEntries() }
        .let { Json.encodeToString(it) }
        .let {
            File("./src/jvmMain/resources/ScienceDatabase.json").writeText(it)
        }
}

fun String.parseEntries(): List<Topic> {
    return split("#")
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .map { raw ->
            val parts = raw.split("\n")
            val topic = parts.first()
            val data = parts.drop(1).joinToString("\n")
            Topic(topic, data)
        }
}