package org.kotlin.everywhere.net


suspend inline operator fun <P : Any, reified R : Any> Call<P, R>.invoke(parameter: P): R {
    val client =
        kenet.client ?: throw AssertionError("API 사용법 오류(Invalid API usage) : 연결에 사용할 클라이언트 엔진이 필요하다.")
    if (client !is Client) {
        throw AssertionError("API 사용법 오류(Invalid API usage) : 올바른 클라이언트가 아니다.")
    }
    return client.call(this, parameter)
}

expect class Client() : KenetClient {
    suspend inline fun <P : Any, reified R : Any> call(call: Call<P, R>, parameter: P): R
}