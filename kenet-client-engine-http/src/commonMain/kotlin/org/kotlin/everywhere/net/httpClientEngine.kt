package org.kotlin.everywhere.net

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
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
            body = createRequest(call, parameter)
        }
        return dslJsonFormat.decodeFromString(call.responseSerializer, response.responseJson)
    }

    override fun <P : Any> fire(fire: Fire<P>, parameter: P) {
        client.launch {
            client.post<Unit>("${urlPrefix}/kenet") {
                contentType(ContentType.Application.Json)
                body = Request(
                    createSubPath(fire.kenet),
                    fire.name,
                    Json.encodeToString(fire.parameterSerializer, parameter)
                )
            }
        }
    }
}

fun <P : Any, R : Any> createRequest(call: Call<P, R>, parameter: P): Request {
    return Request(createSubPath(call.kenet), call.name, Json.encodeToString(call.parameterSerializer, parameter))
}

internal fun createSubPath(kenet: Kenet): List<String> {
    return createSubPath(kenet, mutableListOf())
}

private tailrec fun createSubPath(kenet: Kenet, path: MutableList<String>): List<String> {
    val parent = kenet._parent
    return if (parent == null) {
        path
    } else {
        path.add(0, kenet._name)
        createSubPath(parent, path)
    }
}