package test.echo.server

import kotlinx.coroutines.runBlocking
import org.kotlin.everywhere.net.HttpServerEngine
import org.kotlin.everywhere.net.createServer
import org.kotlin.everywhere.net.invoke
import test.echo.def.Def

fun main(): Unit = runBlocking {
    val server = createServer(Def().apply { init() }, HttpServerEngine())
    // TODO :: graceful server exit
    server.launch(5000)
}

fun Def.init() {
    echo {
        it
    }
}