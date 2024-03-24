package components

import el
import kotlinx.html.*
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement

fun TagConsumer<HTMLElement>.slider(id: String, min: Int, max: Int, value: Int, label: (Int) -> Int = { it }, vertical: Boolean = false, onChange: (Int) -> Unit) {
    val sliderCss = if (vertical) "vertical-slider" else "horizontal-slider"
    input(InputType.range, classes = sliderCss) {
        this.id = id
        this.min = "$min"
        this.max = "$max"
        this.value = value.toString()
        list = "$id-ticks"
        if (vertical) attributes["orient"] = "vertical"
        onChangeFunction = {
            val tickValue = el<HTMLInputElement>(id).value.toIntOrNull() ?: 0
            onChange(tickValue)
        }
    }
    val css = if (vertical) "vertical-slider-ticks" else "slider-ticks"
    dataList(css) {
        this.id = "$id-ticks"
        (min..max).forEach {
            option {
                this.value = "$it"
                this.label = "${label(it)}"
            }
        }
    }
}