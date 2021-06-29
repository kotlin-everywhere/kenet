package test.echo.client

import kotlinx.coroutines.runBlocking
import org.kotlin.everywhere.net.HttpClientEngine
import org.kotlin.everywhere.net.createClient
import org.kotlin.everywhere.net.invoke
import test.echo.def.Def

fun main(): Unit = runBlocking {
    val client = createClient(Def(), HttpClientEngine("http://localhost:5000"))
    val message = "test.echo.client.jvm"
    val echoedMessage = client.kenet.echo.invoke(message)
    require(message == echoedMessage) {
        "server dose not return same message : messages = $message, echoedMessage = $echoedMessage"
    }
}