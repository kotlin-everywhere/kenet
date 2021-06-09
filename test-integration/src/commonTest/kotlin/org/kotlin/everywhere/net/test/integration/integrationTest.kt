package org.kotlin.everywhere.net.test.integration

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kotlin.everywhere.net.*
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals


// kotlinx-coroutines-test 는 JVM 라이브러리다. 현재 기능 개발 논의 중
// https://github.com/Kotlin/kotlinx.coroutines/issues/1996
expect fun runBlockingTest(block: suspend CoroutineScope.() -> Unit)
expect val testCoroutineContext: CoroutineContext

class EchoTest {
    @Test
    fun testEcho() = runBlockingTest {
        class Api : Kenet() {
            val echo by c<String, String>()
        }

        val server = Server(Api())
        server.kenet.echo.invoke(handler = { msg -> msg })
        val serverJob = launch { server.launch(5000) }

        // TODO :: server.launch API 변경후 delay 삭제
        delay(1000)
        val client = Api().apply { client = Client() }
        val returnedMessage = client.echo("hello, world!")
        assertEquals("hello, world!", returnedMessage)

        serverJob.cancelAndJoin()
    }
}