package org.kotlin.everywhere.net

expect class HttpServerEngine() : ServerEngine {
    override suspend fun launch(port: Int, kenet: Kenet)
}