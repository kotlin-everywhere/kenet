package org.kotlin.everywhere.net.test.integration

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kotlin.everywhere.net.*
import kotlin.test.Test
import kotlin.test.assertEquals


class JvmEchoTest {
    @Test
    fun testEcho() = runBlockingTest {
        class Api : Kenet() {
            val echo by c<String, String>()
        }

        fun Api.init() {
            echo(handler = { it })
        }

        val api = Api().apply { init() }
        val server = createServer(api, HttpServerEngine())

        // TODO :: server.launch API 변경후 delay & launch 삭제
        val serverJob = launch { server.launch(5000) }
        delay(1000)

        val client = createClient(api, HttpClientEngine("http://localhost:5000"))
        val returnedMessage = client.kenet.echo("hello, world!")
        assertEquals("hello, world!", returnedMessage)

        serverJob.cancelAndJoin()
        delay(1000)
    }
}