import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.kotlin.everywhere.net.createClient
import org.kotlin.everywhere.net.invoke

fun main() = runBlocking {
    delay(150) // 서버 실행까지 대기

    val client = createClient(Api())
    client.kenet.benchmark(Unit)
    client.kenet.quit(Unit)
}