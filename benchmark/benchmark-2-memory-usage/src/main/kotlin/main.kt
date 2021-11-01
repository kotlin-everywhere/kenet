import kotlinx.coroutines.*
import org.kotlin.everywhere.net.HttpServerEngine
import org.kotlin.everywhere.net.createServer
import org.kotlin.everywhere.net.invoke

fun Api.init(mainScope: CoroutineScope, quitDeferred: CompletableDeferred<Unit>) {
    benchmark {
        // 사용중인 메모리 =  JVM 에 설정된 최대 메모리 - JVM 에 남은 메모리
        val usedMemory = Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()
        log(
            "2. memory-usage",
            "total memory: ${Runtime.getRuntime().totalMemory().toMb()}" /* JVM 전체 메모리 */,
            "max memory: ${Runtime.getRuntime().maxMemory().toMb()}" /* JVM 최대 메모리 */,
            "free memory: ${Runtime.getRuntime().freeMemory().toMb()}" /* JVM 미사용 메모리 */,
            "used memory: ${usedMemory.toMb()}" /* 사용중인 메모리 */,
        )
        logResult("2-memory-usage", usedMemory)
    }
    quit.invoke {
        mainScope.launch {
            delay(1000)
            quitDeferred.complete(Unit)
        }
        Unit
    }
}

fun main() = runBlocking {
    val serverQuitDeferred = CompletableDeferred<Unit>()

    val api = Api().apply {
        init(this@runBlocking, serverQuitDeferred)
    }
    val server = createServer(api, HttpServerEngine())
    val serverJob = launch {
        server.launch(5000)
    }

    serverQuitDeferred.await()
    serverJob.cancelAndJoin()
}
