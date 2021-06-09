package org.kotlin.everywhere.net

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

actual class Client<T : Kenet> actual constructor(kenet: T) : CommonClient<T>(kenet) {
    @PublishedApi
    internal val client = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }
        install(JsonFeature)
    }

    init {
        kenet._client = this
    }

    actual suspend fun <P : Any, R : Any> call(call: Call<P, R>, parameter: P): R {
        val response = client.post<Response>("http://localhost:5000/kenet") {
            contentType(ContentType.Application.Json)
            body = Request(call.name, Json.encodeToString(call.parameterSerializer, parameter))
        }
        return Json.decodeFromString(call.responseSerializer, response.responseJson)
    }
}