package org.kotlin.everywhere.net

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel


suspend operator fun <P : Any, R : Any> Call<P, R>.invoke(parameter: P): R {
    val client = lookupClient(kenet) ?: throw AssertionError("API 사용법 오류(Invalid API usage) : 연결에 사용할 클라이언트 엔진이 필요하다.")
    if (client !is Client<*>) {
        throw AssertionError("API 사용법 오류(Invalid API usage) : 올바른 클라이언트가 아니다.")
    }
    return client.call(this, parameter)
}

operator fun <P : Any> Fire<P>.invoke(parameter: P) {
    val client = lookupClient(kenet) ?: throw AssertionError("API 사용법 오류(Invalid API usage) : 연결에 사용할 클라이언트 엔진이 필요하다.")
    if (client !is Client<*>) {
        throw AssertionError("API 사용법 오류(Invalid API usage) : 올바른 클라이언트가 아니다.")
    }
    client.fire(this, parameter)
}

suspend operator fun <S : Any, C : Any> Pipe<S, C>.invoke(clientHandler: suspend CoroutineScope.(sender: SendChannel<C>, receiver: ReceiveChannel<S>) -> Unit) {
    val client = lookupClient(kenet) ?: throw AssertionError("API 사용법 오류(Invalid API usage) : 연결에 사용할 클라이언트 엔진이 필요하다.")
    if (client !is Client<*>) {
        throw AssertionError("API 사용법 오류(Invalid API usage) : 올바른 클라이언트가 아니다.")
    }

    client.pipe(this, clientHandler)
}

private fun lookupClient(kenet: Kenet): KenetClient? {
    return kenet._client ?: kenet._parent?.let(::lookupClient)
}

class Client<T : Kenet>(val kenet: T, private val engine: ClientEngine) : KenetClient {
    init {
        kenet._client = this
    }

    internal suspend fun <P : Any, R : Any> call(call: Call<P, R>, parameter: P): R {
        return engine.call(call, parameter)
    }

    internal fun <P : Any> fire(fire: Fire<P>, parameter: P) {
        engine.fire(fire, parameter)
    }

    internal suspend fun <S : Any, C : Any> pipe(
        pipe: Pipe<S, C>,
        handler: suspend CoroutineScope.(send: SendChannel<C>, receive: ReceiveChannel<S>) -> Unit
    ) {
        engine.pipe(pipe, handler)
    }
}

fun <T : Kenet> createClient(kenet: T, engine: ClientEngine): Client<T> {
    return Client(kenet, engine)
}

abstract class ClientEngine {
    abstract suspend fun <P : Any, R : Any> call(call: Call<P, R>, parameter: P): R
    abstract fun <P : Any> fire(fire: Fire<P>, parameter: P)
    abstract suspend fun <S : Any, C : Any> pipe(
        pipe: Pipe<S, C>,
        handler: suspend CoroutineScope.(send: SendChannel<C>, receive: ReceiveChannel<S>) -> Unit
    )
}

class TestClientEngine : ClientEngine() {
    override suspend fun <P : Any, R : Any> call(call: Call<P, R>, parameter: P): R {
        TODO("Not yet implemented")
    }

    override fun <P : Any> fire(fire: Fire<P>, parameter: P) {
        TODO("Not yet implemented")
    }

    override suspend fun <S : Any, C : Any> pipe(
        pipe: Pipe<S, C>,
        handler: suspend CoroutineScope.(send: SendChannel<C>, receive: ReceiveChannel<S>) -> Unit
    ) {
        TODO("Not yet implemented")
    }
}