import org.kotlin.everywhere.net.Kenet
import org.kotlin.everywhere.net.createRequest
import org.kotlin.everywhere.net.createSubPath
import kotlin.test.Test
import kotlin.test.assertEquals

class HttpClientEngineTest {
    @Test
    fun testRequest() {
        val sub2 = object : Kenet() {
            val echo3 by c<Int, Int>()
        }
        val sub = object : Kenet() {
            val echo2 by c<Int, Int>()
            val sub2 by c(sub2)
        }
        val api = object : Kenet() {
            val echo by c<String, String>()
            val sub by c(sub)
        }

        val apiEchoRequest = createRequest(api.echo, "hello")
        assertEquals("echo", apiEchoRequest.endpointName, "엔드포인트 이름 확인")
        assertEquals(listOf(), apiEchoRequest.subPath, "최상위 는 subChain 이 공백이다.")

        val subEcho2Request = createRequest(api.sub.echo2, 1)
        assertEquals("echo2", subEcho2Request.endpointName, "서브 엔드포인트 이름 확인")
        assertEquals(listOf("sub"), subEcho2Request.subPath, "서브는 subPath 에 경로가 포함되어야 한다.")

        val sub2Echo3Request = createRequest(api.sub.sub2.echo3, 2)
        assertEquals("echo3", sub2Echo3Request.endpointName, "서브 엔드포인트 이름 확인")
        assertEquals(listOf("sub", "sub2"), sub2Echo3Request.subPath, "서브는 subPath 에 전체 경로가 포함되어야 한다.")
    }

    @Test
    fun testSubPath() {
        val api = object : Kenet() {
            val sub by c(object : Kenet() {
                val sub2 by c(object : Kenet() {})
            })
        }

        assertEquals(listOf(), createSubPath(api), "최상위 는 subPath 는 공백이다.")
        assertEquals(listOf("sub"), createSubPath(api.sub), "서브는 subPath 에 경로가 포함되어야 한다.")
        assertEquals(listOf("sub", "sub2"), createSubPath(api.sub.sub2), "서브는 subPath 에 전체 경로가 포함되어야 한다.")
    }
}