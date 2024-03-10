package views

import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.id
import org.w3c.dom.HTMLElement

fun TagConsumer<HTMLElement>.nav() {
    div {
        id = "nav"

    }
}