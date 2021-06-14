import org.kotlin.everywhere.net.Kenet
import org.kotlin.everywhere.net.c

class Api : Kenet() {
    val benchmark by c<Unit, Unit>()
    val quit by c<Unit, Unit>()
}

fun Long.toMb(): String {
    return "${toFloat() / 1024 / 1024}mb"
}

fun log(vararg messages: String) {
    messages.forEach {
        println(it)
    }
    println()
    logToResultFile(messages)
}

expect fun logToResultFile(messages: Array<out String>)
