package org.kotlin.everywhere.net

import kotlin.test.*

class DslTest {
    /**
     * 컴파일 테스트
     * 최소한의 테스트 통과여부 확인
     */
    @Test
    fun testCompile() {
        class Api : Kenet() {
            val call by c<Unit, Unit>()

            // 동일 이름 사용 가능 여부 확인, Kenet 사용 중인 프로퍼티 명과 충돌하는지 테스트
            val endpoints by c<Unit, String>()
        }

        val api = Api()
        assertIs<Kenet>(api, "Kenet Protocol 정의 객체")
        assertIs<Call<*, *>>(api.call, "Call 호출 지점")
        assertEquals("endpoints", api.endpoints.name, "동일한 프로퍼티명 겹치지 않고 호출되는지 확인")
    }

    /**
     * 호출 지점 초기화 확인
     * 호출 지점의 이름이 정확하게 들어가는지 확인
     */
    @Test
    fun testEndpointInitialized() {
        class Api : Kenet() {
            val call by c<Unit, Unit>()
        }

        val api = Api()
        // 호출하지 않은 기본 상태에서 직접 액세스 하면 아직 초기화되지 않은 상태이다.
        assertFalse(api._endpoints[0].initialized, "호출하지 않은 상태에서는 초기화되기 전이다.")
        // 기본 상태 익명 이름, 실제 입력된 순서와 동일한지 확인
        assertEquals("anonymousEndpoint#0", api._endpoints[0].name, "초기화 전에는 anonymous#index 형식의 이름을 가지고 있어야 한다.")

        // dot(.) 형식으로 액세스하여 사용하면, 초기화된다.
        assertTrue(api.call.initialized, "호출하면 초기화되어야 한다.")
        // 기본 상태 익명 이름, 실제 입력된 순서와 동일한지 확인
        assertEquals("call", api.call.name, "초기화되면 실제 프로퍼티명을 가지고 있어야 한다.")
    }
}