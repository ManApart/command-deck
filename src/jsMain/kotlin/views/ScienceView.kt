package views

import Topic
import el
import frames.DatabaseSearch
import frames.Scan
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.div
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyPressFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.get
import replaceElement
import wsSend

fun scienceView() {
    GameState.currentView = View.SCIENCE
    replaceElement {
        nav()
        div {
            h1 {
                +"Science"
            }
            crewmanTitle()
            div {
                id = "science-scan"
                h3 { +"Scanning Controls" }
                span { +"(" }
                input(InputType.number) {
                    id = "scan-x"
                    value = "0"
                }
                span { +"," }
                input(InputType.number) {
                    id = "scan-y"
                    value = "0"
                }
                span { +")" }
                button {
                    +"Scan"
                    onClickFunction = { doScan() }
                }
                div {
                    id = "science-scan-results-wrapper"
                    textArea {
                        id = "science-scan-results"
                        readonly = true
                    }
                }
            }
            div {
                id = "science-database"
                h3 { +"Science Database" }
                input {
                    id = "search-input"
                    onKeyPressFunction = {
                        it as KeyboardEvent
                        if (it.key == "Enter") {
                            val topic = el<HTMLInputElement>("search-input").value
                            searchDatabase(topic)
                        }
                    }
                }
                button {
                    +"Search"
                    onClickFunction = {
                        val topic = el<HTMLInputElement>("search-input").value
                        searchDatabase(topic)
                    }
                }
                select {
                    id = "topics-list"
                    hidden = true
                    onChangeFunction = {
                        val select = el<HTMLSelectElement>("topics-list")
                        val topic = select.options[select.selectedIndex]!!.id
                        searchDatabase(topic)
                    }
                }
                div {
                    id = "science-search-results-wrapper"
                    textArea {
                        id = "science-search-results"
                        readonly = true
                    }
                }

            }
        }
    }
}

fun receiveMessage(content: String) {
    el("science-scan-results").innerText = content
}

fun updateTopics(topics: List<String>) {
    val select = el<HTMLSelectElement>("topics-list")
    select.innerHTML = ""
    select.append {
        topics.forEach {
            option { +it }
        }
    }
}

fun searchResults(topic: Topic) {
    val text = el<HTMLTextAreaElement>("science-search-results")
    text.value = "${topic.name}\n${topic.tags}\n\n${topic.data}"
}

private fun doScan() {
    val x = el<HTMLInputElement>("scan-x").value.toIntOrNull() ?: 0
    val y = el<HTMLInputElement>("scan-y").value.toIntOrNull() ?: 0
    wsSend(Scan(x, y))
}

private fun searchDatabase(topic: String) {
    wsSend(DatabaseSearch(topic))
}