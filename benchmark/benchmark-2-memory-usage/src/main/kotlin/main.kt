import kotlinx.coroutines.*
import org.kotlin.everywhere.net.HttpEngine
import org.kotlin.everywhere.net.createServer
import org.kotlin.everywhere.net.invoke

fun Api.init(quitDeferred: CompletableDeferred<Unit>) {
    benchmark {
        log(
            "2. memory-usage",
            "total memory: ${Runtime.getRuntime().totalMemory().toMb()}",
            "max memory: ${Runtime.getRuntime().maxMemory().toMb()}",
            "free memory: ${Runtime.getRuntime().freeMemory().toMb()}",
            "used memory: ${(Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()).toMb()}",
        )
    }
    quit.invoke {
        GlobalScope.launch {
            delay(1000)
            quitDeferred.complete(Unit)
        }
        Unit
    }
}

fun main() {
    runBlocking {
        val serverQuitDeferred = CompletableDeferred<Unit>()

        val api = Api().apply {
            init(serverQuitDeferred)
        }
        val server = createServer(api, HttpEngine())
        val serverJob = launch {
            server.launch(5000)
        }

        serverQuitDeferred.await()
        serverJob.cancelAndJoin()
    }
}