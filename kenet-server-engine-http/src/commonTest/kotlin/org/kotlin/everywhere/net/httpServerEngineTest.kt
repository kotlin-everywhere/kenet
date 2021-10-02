package org.kotlin.everywhere.net

import kotlin.test.Test
import kotlin.test.assertEquals

class HttpServerEngineTest {
    @Test
    fun testFindEndpoint() {
        val api = object : Kenet() {
            val echo by c<String, String>()
            val sub by c(object : Kenet() {
                val echo2 by c<String, String>()
                val sub2 by c(object : Kenet() {
                    val echo3 by c<String, String>()
                })
            })
        }

        assertEquals(api.echo, findEndpoint(api, listOf(), "echo"))
        assertEquals(api.sub.echo2, findEndpoint(api, listOf("sub"), "echo2"))
        assertEquals(api.sub.sub2.echo3, findEndpoint(api, listOf("sub", "sub2"), "echo3"))
    }
}