import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.kotlin.everywhere.net.createClient
import org.kotlin.everywhere.net.invoke

fun main() = runBlocking {
    val client = createClient(Api())
    val startedAt = System.currentTimeMillis()
    while (true) {
        try {
            client.kenet.benchmark(Unit)
            break
        } catch (e: Exception) {
            val elapsed = System.currentTimeMillis() - startedAt
            if (elapsed >= 2_000) {
                throw e
            }
            delay(50)
        }
    }
    client.kenet.quit(Unit)
}