package org.kotlin.everywhere.net

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch


class HttpClientEngine(private val urlPrefix: String) : ClientEngine() {
    private val client = HttpClient {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        install(WebSockets) {
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
                    dslJsonFormat.encodeToString(fire.parameterSerializer, parameter)
                )
            }
        }
    }

    override suspend fun <S : Any, C : Any> pipe(
        pipe: Pipe<S, C>,
        handler: suspend CoroutineScope.(send: SendChannel<C>, receive: ReceiveChannel<S>) -> Unit
    ) {
        val url = "ws" + urlPrefix.removePrefix("http")
        val urlString = "$url/kenet-ws/${pipe.name}"
        client.webSocket(urlString) {
            val send = Channel<C>()
            val sendJob = launch {
                for (clientMsg in send) {
                    send(dslJsonFormat.encodeToString(pipe.clientMessageSerializer, clientMsg))
                }
            }
            val receive = Channel<S>()
            val receiveJob = launch {
                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> receive.send(
                            dslJsonFormat.decodeFromString(
                                pipe.serverMessageSerializer,
                                frame.readText()
                            )
                        )
                        else -> {
                            // TODO :: validate
                            throw IllegalArgumentException("invalid frame received : frame = $frame")
                        }
                    }
                }
            }
            handler(send, receive)
            sendJob.cancel()
            receiveJob.cancel()
        }
    }
}

fun <P : Any, R : Any> createRequest(call: Call<P, R>, parameter: P): Request {
    return Request(
        createSubPath(call.kenet),
        call.name,
        dslJsonFormat.encodeToString(call.parameterSerializer, parameter)
    )
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