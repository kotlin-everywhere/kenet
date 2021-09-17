package org.kotlin.everywhere.net

import kotlin.test.Test
import kotlin.test.assertIs

class SeverTest {
    @Test
    fun testCompile() {
        class SubApi : Kenet() {
            val echo2 by c<String, String>()
        }

        class Api : Kenet() {
            val echo by c<String, String>()
            val sub by c(SubApi())
        }

        fun SubApi.init() {
            echo2 { it }
        }

        fun Api.init() {
            echo { it }
            sub.init()
        }

        val api = Api().apply { init() }
        val server = createServer(api, TestServerEngine())
        assertIs<Server>(server, "서버를 생성하면 서버가 생성된다.")
    }
}