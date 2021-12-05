package org.kotlin.everywhere.net

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import kotlinx.coroutines.CompletableDeferred


actual class HttpServerEngine actual constructor() : ServerEngine() {
    actual override suspend fun launch(port: Int, kenet: Kenet) {
        val es = embeddedServer(CIO, port = port, watchPaths = listOf()) {
            install(ContentNegotiation) {
                json()
                // TODO :: 실행옵션으로 변경, 필요없는 allow 제거
                install(CORS) {
                    method(HttpMethod.Options)
                    method(HttpMethod.Put)
                    method(HttpMethod.Delete)
                    method(HttpMethod.Patch)
                    header(HttpHeaders.Authorization)
                    header(HttpHeaders.ContentType)
                    allowCredentials = true
                    allowNonSimpleContentTypes = true
                    anyHost()
                }
            }

            routing {
                post("/kenet") {
                    val request = call.receive<Request>()
                    val endpoint = findEndpoint(kenet, request.subPath, request.endpointName)
                    // TODO :: Error 응답 리턴
                    check(endpoint != null) { "요청한 ENDPOINT 를 찾을 수 없습니다." }
                    when (endpoint) {
                        is Call<*, *> -> {
                            val responseJson = endpoint.handle(request.parameterJson)
                            call.respond(Response(responseJson = responseJson))
                        }
                        is Fire<*> -> {
                            endpoint.handle(request.parameterJson)
                        }
                        else -> {
                            throw IllegalArgumentException("처리할 수 없는 요청입니다. : endpoint = $endpoint")
                        }
                    }
                }
            }
        }
        es.start()

        // TODO :: 정상적으로 기다리는 방벙 연구
        val deferred = CompletableDeferred<Unit>()
        deferred.await()
    }
}