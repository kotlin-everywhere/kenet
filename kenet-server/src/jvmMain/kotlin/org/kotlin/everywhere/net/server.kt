package org.kotlin.everywhere.net

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CompletableDeferred


actual class Server<T : Kenet> actual constructor(kenet: T) : CommonSever<T>(kenet) {

    actual suspend fun launch(port: Int) {
        val es = embeddedServer(Netty, port = port) {
            install(ContentNegotiation) {
                json()
            }

            routing {
                post("/kenet") {
                    val request = call.receive<Request>()
                    val endpoint = kenet._endpoints.firstOrNull() { it.name == request.endpointName }
                    // TODO :: Error 응답 리턴
                    check(endpoint != null) { "요청한 ENDPOINT 를 찾을 수 없습니다." }
                    // TODO :: Error 응답 리턴
                    check(endpoint is Call<*, *>) { "요청한 endpoint 는 call 을 처리할 수 없습니다." }

                    val responseJson = endpoint.handle(request.parameterJson)
                    call.respond(Response(responseJson = responseJson))
                }
            }
        }
        es.start()

        // TODO :: 정상적으로 기다리는 방벙 연구
        val deferred = CompletableDeferred<Unit>()
        deferred.await()
    }
}