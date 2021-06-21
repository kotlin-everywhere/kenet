package org.kotlin.everywhere.net

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ClientTest {
    @Test
    fun testCompile() {
        class Api : Kenet() {
            val echo by c<String, String>()
        }

        val client = createClient(Api(), TestClientEngine())
        assertIs<Client<Api>>(client, "클라이언트를 생성하면 클라이언트가 생성된다.")
        assertEquals("echo", client.kenet.echo.name, "클라이언트를 통해서 Kenet 액서스 가능한지 확인")
    }
}