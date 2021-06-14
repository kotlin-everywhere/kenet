import kotlinx.coroutines.*
import org.kotlin.everywhere.net.HttpEngine
import org.kotlin.everywhere.net.createServer
import org.kotlin.everywhere.net.invoke

fun Api.init(mainScope: CoroutineScope, quitDeferred: CompletableDeferred<Unit>, serverStartedAt: Milliseconds) {
    benchmark {
        val launchedAt = Milliseconds(System.currentTimeMillis())
        val launchTime = launchedAt.ms - serverStartedAt.ms

        log(
            "1. server-launch-time",
            "startedAt: ${serverStartedAt.ms}ms",
            "launchedAt: ${launchedAt.ms}ms",
            "launchTime: ${launchTime}ms"
        )

        logResult("1-server-launch-time", launchTime)
    }
    quit.invoke {
        mainScope.launch {
            delay(1000)
            quitDeferred.complete(Unit)
        }
        Unit
    }
}

@JvmInline
value class Milliseconds(val ms: Long)

fun main() = runBlocking {
    val serverStartedAt = Milliseconds(System.currentTimeMillis())
    val serverQuitDeferred = CompletableDeferred<Unit>()

    val api = Api().apply {
        init(this@runBlocking, serverQuitDeferred, serverStartedAt)
    }
    val server = createServer(api, HttpEngine())
    val serverJob = launch {
        server.launch(5000)
    }

    serverQuitDeferred.await()
    serverJob.cancelAndJoin()
}