package org.kotlin.everywhere.net

expect class HttpEngine() : Engine {
    override suspend fun launch(port: Int, kenet: Kenet)
}