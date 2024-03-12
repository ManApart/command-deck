package components

import kotlinx.html.TagConsumer
import kotlinx.html.hidden
import kotlinx.html.id
import kotlinx.html.js.div
import org.w3c.dom.HTMLElement

fun TagConsumer<HTMLElement>.progressBar(){
    div {
        id = "progress-bar"
        hidden = true
        div("progress-empty") {
            id = "progress-bar-inner"
        }
    }
}