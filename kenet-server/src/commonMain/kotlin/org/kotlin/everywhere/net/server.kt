package org.kotlin.everywhere.net

abstract class CommonSever(protected val kenet: Kenet) {
}

expect class Server constructor(kenet: Kenet) : CommonSever {
    suspend fun launch(port: Int)
}