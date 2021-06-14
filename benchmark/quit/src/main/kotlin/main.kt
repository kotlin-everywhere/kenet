import kotlinx.coroutines.runBlocking
import org.kotlin.everywhere.net.createClient
import org.kotlin.everywhere.net.invoke

fun main() {
    runBlocking {
        val client = createClient(Api())
        client.kenet.benchmark(Unit)
        client.kenet.quit(Unit)
    }
}