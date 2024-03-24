package components

import el
import kotlinx.html.*
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement

fun TagConsumer<HTMLElement>.toggle(id: String, onChange: (Boolean) -> Unit){
    label("switch") {
        input(InputType.checkBox) {
            this.id = id
            onChangeFunction = {
                val checked = el<HTMLInputElement>(id).checked
                onChange(checked)
            }
        }
        span("toggle") { }
    }
}