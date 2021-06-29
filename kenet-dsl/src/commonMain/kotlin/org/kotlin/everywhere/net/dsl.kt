package org.kotlin.everywhere.net

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.properties.ReadOnlyProperty

abstract class Kenet {
    val _endpoints = mutableListOf<Endpoint<*>>()

    var _client: KenetClient? = null

    /**
     * 최초 생성시 이름 지정용 인덱스, 그냥 이름 자리에 null 이 들어가는게 싫다.
     */
    private var anonymousEndpointIndex = 0

    @PublishedApi
    internal fun <P : Any, R : Any> call(
        parameterSerializer: KSerializer<P>,
        responseSerializer: KSerializer<R>
    ): ReadOnlyProperty<Kenet, Call<P, R>> = createEndpointProperty { index, anonymousName ->
        Call(this, index, false, anonymousName, parameterSerializer, responseSerializer)
    }

    private fun <T : Endpoint<*>> createEndpointProperty(create: (index: Int, anonymousName: String) -> T): ReadOnlyProperty<Kenet, T> {
        val index = anonymousEndpointIndex++
        val endpoint = create(index, "anonymousEndpoint#${index}")

        _endpoints.add(endpoint)

        return ReadOnlyProperty { kenet, property ->
            if (!endpoint.initialized) {
                if (endpoint.kenet !== kenet) {
                    throw AssertionError("API 사용법 오류(Invalid API usage) : Endpoint 를 생성한 클래스와 사용하는 클래스가 다르다.")
                }
                endpoint.name = property.name
                endpoint.initialized = true
            }
            endpoint
        }
    }

    inline fun <reified P : Any, reified R : Any> c(): ReadOnlyProperty<Kenet, Call<P, R>> {
        return call(serializer(), serializer())
    }
}

sealed class Endpoint<P : Any>(
    val kenet: Kenet,
    internal val index: Int,
    internal var initialized: Boolean,
    var name: String,
    val parameterSerializer: KSerializer<P>
)

class Call<P : Any, R : Any>(
    kenet: Kenet,
    index: Int,
    initialized: Boolean,
    name: String,
    parameterSerializer: KSerializer<P>,
    val responseSerializer: KSerializer<R>
) : Endpoint<P>(kenet, index, initialized, name, parameterSerializer) {
    var handler: (P) -> R =
        { _ -> throw NotImplementedError("API 사용법 오류(Invalid API usage) : ${name}의 핸들러가 정의되지 않았습니다..") }
}

interface KenetClient

@Serializable
data class Request(val endpointName: String, val parameterJson: String)

@Serializable
data class Response(val responseJson: String)
