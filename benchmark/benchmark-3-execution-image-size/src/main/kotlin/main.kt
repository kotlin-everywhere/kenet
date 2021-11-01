import kotlinx.coroutines.*
import org.kotlin.everywhere.net.HttpServerEngine
import org.kotlin.everywhere.net.createServer
import org.kotlin.everywhere.net.invoke
import java.io.File

private class TargetForJarPath

fun Api.init(mainScope: CoroutineScope, quitDeferred: CompletableDeferred<Unit>) {
    benchmark {
        val imageFile = TargetForJarPath::class.java.protectionDomain.codeSource.location.toURI().let(::File)
        val imageSize = imageFile.length()

        log(
            "3. execution-image-size",
            "imagePath: ${imageFile.absolutePath}" /* 실행 이미지 경로 */,
            "imageSize:  ${imageSize.toMb()}"  /* 실행 이미지 용량 */,
        )

        logResult("3-execution-image-size", imageSize)
    }
    quit.invoke {
        mainScope.launch {
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
            init(this@runBlocking, serverQuitDeferred)
        }
        val server = createServer(api, HttpServerEngine())
        val serverJob = launch {
            server.launch(5000)
        }

        serverQuitDeferred.await()
        serverJob.cancelAndJoin()
    }
}
