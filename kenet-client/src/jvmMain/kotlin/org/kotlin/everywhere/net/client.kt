package org.kotlin.everywhere.net

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

actual class Client actual constructor() : KenetClient {
    @PublishedApi
    internal val client = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }
        install(JsonFeature)
    }

    actual suspend inline fun <P : Any, reified R : Any> call(call: Call<P, R>, parameter: P): R {
        return client.post("http://localhost:5000/kenet") {
            contentType(ContentType.Application.Json)
            body = parameter
        }
    }
}