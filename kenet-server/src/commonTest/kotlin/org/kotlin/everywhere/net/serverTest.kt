package org.kotlin.everywhere.net

import kotlin.test.Test
import kotlin.test.assertIs

class SeverTest {
    @Test
    fun testCompile() {
        class Api : Kenet() {
            val echo by c<String, String>()
        }

        fun Api.init() {
            echo { it }
        }

        val api = Api().apply { init() }
        val server = createServer(api, TestEngine())
        assertIs<Server>(server, "서버를 생성하면 서버가 생성된다.")
    }
}