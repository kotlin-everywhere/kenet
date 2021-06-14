import kotlinx.coroutines.*
import org.kotlin.everywhere.net.HttpEngine
import org.kotlin.everywhere.net.createServer
import org.kotlin.everywhere.net.invoke
import java.io.File

private class TargetForJarPath

fun Api.init(quitDeferred: CompletableDeferred<Unit>) {
    benchmark {
        val imageFile = TargetForJarPath::class.java.protectionDomain.codeSource.location.toURI().let(::File)
        val imageSize = imageFile.length()

        log(
            "3. execution-image-size",
            "imagePath: ${imageFile.absolutePath}",
            "imageSize:  ${imageSize.toMb()}",
        )

        logResult("3-execution-image-size", imageSize)
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