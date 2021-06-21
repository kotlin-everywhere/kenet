package org.kotlin.everywhere.net

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json


class HttpClientEngine(private val urlPrefix: String) : ClientEngine() {
    private val client = HttpClient {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    override suspend fun <P : Any, R : Any> call(call: Call<P, R>, parameter: P): R {
        val response = client.post<Response>("${urlPrefix}/kenet") {
            contentType(ContentType.Application.Json)
            body = Request(call.name, Json.encodeToString(call.parameterSerializer, parameter))
        }
        return Json.decodeFromString(call.responseSerializer, response.responseJson)
    }
}