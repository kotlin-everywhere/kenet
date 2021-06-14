import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.kotlin.everywhere.net.createClient
import org.kotlin.everywhere.net.invoke

fun main() {
    runBlocking {
        val client = createClient(Api())
        delay(200) // 서버 실행까지 대기
        client.kenet.benchmark(Unit)
        client.kenet.quit(Unit)
    }
}