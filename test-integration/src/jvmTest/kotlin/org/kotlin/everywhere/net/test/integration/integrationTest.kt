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
        class SubApi : Kenet() {
            val echo2 by c<String, String>()
        }

        class Api : Kenet() {
            val echo by c<String, String>()
            val sub by c(SubApi())
        }

        fun Api.init() {
            echo(handler = { it })
            sub.echo2(handler = { it })
        }

        val api = Api().apply { init() }
        val server = createServer(api, HttpServerEngine())

        // TODO :: server.launch API 변경후 delay & launch 삭제
        val serverJob = launch { server.launch(5000) }
        delay(1000)

        val client = createClient(api, HttpClientEngine("http://localhost:5000"))
        val echo = client.kenet.echo.invoke("hello, world!")
        assertEquals("hello, world!", echo)

//         TODO :: client 전달 및 이름 정의 추가
        val echo2 = client.kenet.sub.echo2.invoke("hello, 2 world!")
        assertEquals("hello, 2 world!", echo2)


        serverJob.cancelAndJoin()
        delay(1000)
    }
}