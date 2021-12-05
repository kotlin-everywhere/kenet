package org.kotlin.everywhere.net.test.integration

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.kotlin.everywhere.net.*
import kotlin.test.Test
import kotlin.test.assertEquals


@Serializable
sealed class ServerMsg {
    @Serializable
    @SerialName("Ping")
    class Ping(val no: Int) : ServerMsg()
}

@Serializable
sealed class ClientMsg {
    @Serializable
    @SerialName("Pong")
    class Pong(val no: Int) : ClientMsg()
}

class JvmEchoTest {
    @Test
    fun testEcho() = runBlockingTest {
        class SubApi : Kenet() {
            val echo2 by c<String, String>()
        }

        class Api : Kenet() {
            val echo by c<String, String>()
            val sub by c(SubApi())
            val addMessage by f<String>()
            val pipe by p<ServerMsg, ClientMsg>()
        }

        val messages = mutableListOf<String>()
        var pongReceived = false
        fun Api.init() {
            echo(handler = { it })
            sub.echo2(handler = { it })
            addMessage(handler = {
                messages.add(it)
            })
            pipe(serverHandler = { send, receive ->
                send.send(ServerMsg.Ping(1))
                for (clientMsg in receive) {
                    when (clientMsg) {
                        is ClientMsg.Pong -> {
                            pongReceived = true
                        }
                    }
                }
            })
        }

        val api = Api().apply { init() }
        val server = createServer(api, HttpServerEngine())

        // TODO :: server.launch API 변경후 delay & launch 삭제
        val serverJob = launch { server.launch(5000) }
        delay(1000)

        val client = createClient(api, HttpClientEngine("http://localhost:5000"))
        val echo = client.kenet.echo.invoke("hello, world!")
        assertEquals("hello, world!", echo)

        // Ping Pong 테스트
        val deferred = CompletableDeferred<Unit>()
        client.kenet.pipe.invoke(clientHandler = { sender, receiver ->
            when (val serverMsg = receiver.receive()) {
                is ServerMsg.Ping -> {
                    sender.send(ClientMsg.Pong(serverMsg.no))
                }
            }
            deferred.complete(Unit)
        })

        deferred.await()
        assertEquals(true, pongReceived)

        serverJob.cancelAndJoin()
        delay(1000)
    }
}