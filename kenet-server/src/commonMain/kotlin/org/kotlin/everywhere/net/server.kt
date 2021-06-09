package org.kotlin.everywhere.net

import kotlinx.serialization.json.Json

abstract class CommonSever<T : Kenet>(val kenet: T)

expect class Server<T : Kenet> constructor(kenet: T) : CommonSever<T> {
    suspend fun launch(port: Int)
}

operator fun <P : Any, R : Any> Call<P, R>.invoke(handler: (P) -> R) {
    this.handler = handler
}

fun <P : Any, R : Any> Call<P, R>.handle(parameterJson: String): String {
    return Json.encodeToString(responseSerializer, handler(Json.decodeFromString(parameterSerializer, parameterJson)))
}