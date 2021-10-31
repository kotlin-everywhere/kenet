package org.kotlin.everywhere.net

import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertFailsWith
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

    @Test
    fun testEnsureInitialize() {
        class Api : Kenet() {
            val echo by c<String, String>()
        }

        val api = Api()
        val server = createServer(api, TestServerEngine())
        assertFailsWith<NotInitialized>("초기화 하지 않은 endpoint 가 있을경우 NotInitialized 로 알려줘야 한다.") {
            server.ensureInitialize()
        }

        // echo 구현 으로 초기화 추가
        api.echo { it }
        // 초기화 완료 확인, 만약 아닐경우 예외 발생
        server.ensureInitialize()
    }

    @Ignore // suspend 처리전까지 일단 무시 한다.
    @Test
    fun testLaunchPreconditions() {
        class Api : Kenet() {
            val echo by c<String, String>()
        }

        val api = Api()
        val server = createServer(api, TestServerEngine())
        assertFailsWith<NotInitialized> {
            // TODO :: Suspend 처리, https://youtrack.jetbrains.com/issue/KT-22228
//            server.launch("초기화 하지 않은 endpoint 가 있을경우 NotInitialized 로 알려줘야 한다.")
        }
    }
}