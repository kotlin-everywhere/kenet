package org.kotlin.everywhere.net.dsl

import kotlin.test.Test
import kotlin.test.assertIs

class DslTest {
    /**
     * 초기화 테스트
     *
     * Kenet 객체 생성 확인, 코드 사용성 확인
     */
    @Test
    fun testInitialize() {
        class Api : Kenet() {
            val fire by f<Int>()
            val call by c<Unit, Unit>()
        }

        val api = Api()
        assertIs<Kenet>(api, "Kenet Protocol 정의 객체")
        assertIs<Fire<*>>(api.fire, "Fire 호출 지점")
        assertIs<Call<*, *>>(api.call, "Call 호출 지점")
    }
}