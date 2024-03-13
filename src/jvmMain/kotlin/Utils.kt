import kotlin.math.max
import kotlin.math.min

fun Int.clamp(min: Int = 0, max: Int = 100) = min(max, max(min, this))