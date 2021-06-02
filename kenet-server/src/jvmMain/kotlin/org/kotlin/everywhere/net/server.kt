package org.kotlin.everywhere.net

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CompletableDeferred


actual class Server actual constructor(kenet: Kenet) : CommonSever(kenet) {

    actual suspend fun launch(port: Int) {
        val es = embeddedServer(Netty, port = port) {
            install(ContentNegotiation) {
                json()
            }

            routing {
                post("/kenet") {
                    call.respondText("hello, world!")
                }
            }
        }
        es.start()

        // TODO :: 정상적으로 기다리는 방벙 연구
        val deferred = CompletableDeferred<Unit>()
        deferred.await()
    }
}