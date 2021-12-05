package org.kotlin.everywhere.net

class Server(private val kenet: Kenet, private val engine: ServerEngine) {
    suspend fun launch(port: Int) {
        ensureInitialize()

        engine.launch(port, kenet)
    }

    fun ensureInitialize() {
        // OPT :: 친절한 오류 메시지
        val notInitialized = kenet._endpoints.filter { !it.initialized }
        if (notInitialized.isNotEmpty()) {
            throw NotInitialized("초기화 하지 않은 호출지점이 있습니다. : ${notInitialized.joinToString { it.name }}")
        }
    }
}

class NotInitialized(message: String) : Throwable(message)

operator fun <P : Any, R : Any> Call<P, R>.invoke(handler: suspend (P) -> R) {
    this.handler = handler
}

suspend fun <P : Any, R : Any> Call<P, R>.handle(parameterJson: String): String {
    return dslJsonFormat.encodeToString(
        responseSerializer,
        handler(dslJsonFormat.decodeFromString(parameterSerializer, parameterJson))
    )
}

operator fun <P : Any> Fire<P>.invoke(handler: suspend (P) -> Unit) {
    this.handler = handler
}

suspend fun <P : Any> Fire<P>.handle(parameterJson: String) {
    handler(dslJsonFormat.decodeFromString(parameterSerializer, parameterJson))
}

fun createServer(api: Kenet, engine: ServerEngine): Server {
    return Server(api, engine)
}

abstract class ServerEngine {
    abstract suspend fun launch(port: Int, kenet: Kenet)
}

class TestServerEngine : ServerEngine() {
    override suspend fun launch(port: Int, kenet: Kenet) {
        TODO("Not yet implemented")
    }
}
