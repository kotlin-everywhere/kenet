package org.kotlin.everywhere.net

import kotlinx.serialization.json.Json

class Server(private val kenet: Kenet, private val engine: ServerEngine) {
    suspend fun launch(port: Int) {
        engine.launch(port, kenet)
    }
}

operator fun <P : Any, R : Any> Call<P, R>.invoke(handler: (P) -> R) {
    this.handler = handler
}

fun <P : Any, R : Any> Call<P, R>.handle(parameterJson: String): String {
    return Json.encodeToString(responseSerializer, handler(Json.decodeFromString(parameterSerializer, parameterJson)))
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
