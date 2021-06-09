package org.kotlin.everywhere.net


suspend operator fun <P : Any, R : Any> Call<P, R>.invoke(parameter: P): R {
    val client =
        kenet._client ?: throw AssertionError("API 사용법 오류(Invalid API usage) : 연결에 사용할 클라이언트 엔진이 필요하다.")
    if (client !is Client<*>) {
        throw AssertionError("API 사용법 오류(Invalid API usage) : 올바른 클라이언트가 아니다.")
    }
    return client.call(this, parameter)
}

abstract class CommonClient<T : Kenet>(val kenet: T) : KenetClient

expect class Client<T : Kenet>(kenet: T) : CommonClient<T> {
    internal suspend fun <P : Any, R : Any> call(call: Call<P, R>, parameter: P): R
}

fun <T : Kenet> createClient(kenet: T): Client<T> {
    return Client(kenet)
}