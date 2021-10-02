package org.kotlin.everywhere.net

expect class HttpServerEngine() : ServerEngine {
    override suspend fun launch(port: Int, kenet: Kenet)
}

internal fun findEndpoint(kenet: Kenet, subPath: List<String>, endpointName: String): Endpoint? {
    return findEndpoint(kenet, subPath, 0, endpointName)
}

private tailrec fun findEndpoint(
    kenet: Kenet,
    subPath: List<String>,
    subPathIndex: Int,
    endpointName: String
): Endpoint? {
    if (subPathIndex >= subPath.size) {
        return kenet._endpoints.firstOrNull { it.name == endpointName }
    }
    // TODO :: 정확한 오류 안내
    val endpoint = kenet._endpoints.firstOrNull { it.name == subPath[subPathIndex] } ?: return null
    if (endpoint !is SubKenet<*>) {
        return null
    }
    return findEndpoint(endpoint.sub, subPath, subPathIndex + 1, endpointName)
}