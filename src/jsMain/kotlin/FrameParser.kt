import frames.MessageFrame
import frames.ServerInfoFrame
import frames.WSFrame
import org.w3c.dom.HTMLElement

fun WSFrame.parse() {
    println("Parsing frame $this")
    when (this) {
        is MessageFrame -> this.receive()
        is ServerInfoFrame -> this.receive()
        else -> {
            println("Did not recognize $this")
        }
    }
}

private fun MessageFrame.receive() {
    println("Server said: $message")
}
private fun ServerInfoFrame.receive() {
    el<HTMLElement>("ip-display").innerText = ipAddress
}