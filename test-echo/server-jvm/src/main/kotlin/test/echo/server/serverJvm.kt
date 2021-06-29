package test.echo.server

import kotlinx.coroutines.runBlocking
import org.kotlin.everywhere.net.HttpServerEngine
import org.kotlin.everywhere.net.createServer
import org.kotlin.everywhere.net.invoke
import test.echo.def.Def
import java.util.concurrent.atomic.AtomicInteger

fun main(): Unit = runBlocking {
    Runtime.getRuntime().addShutdownHook(Thread {
        require(echoCount.get() > 0) { "echo dose not called : echoCount = ${echoCount.get()}" }
    })

    val server = createServer(Def().apply { init() }, HttpServerEngine())
    // TODO :: graceful server exit
    server.launch(5000)
}

private val echoCount = AtomicInteger(0)

private fun Def.init() {
    echo {
        echoCount.incrementAndGet()

        it
    }
}