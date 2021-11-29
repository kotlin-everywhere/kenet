package org.kotlin.everywhere.net

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.properties.ReadOnlyProperty

abstract class Kenet {
    val _endpoints = mutableListOf<Endpoint>()
    var _client: KenetClient? = null
    var _parent: Kenet? = null
    var _name: String = ""

    /**
     * 최초 생성시 이름 지정용 인덱스, 그냥 이름 자리에 null 이 들어가는게 싫다.
     */
    private var anonymousEndpointIndex = 0

    private fun newEndpointName(): String {
        return "anonymousEndpoint#${anonymousEndpointIndex++}"
    }

    @PublishedApi
    internal fun <P : Any, R : Any> call(
        parameterSerializer: KSerializer<P>,
        responseSerializer: KSerializer<R>
    ): ReadOnlyProperty<Kenet, Call<P, R>> = createEndpointProperty(
        { anonymousName -> Call(this, anonymousName, parameterSerializer, responseSerializer) },
        { it }
    )

    private fun <T : Endpoint, U> createEndpointProperty(
        create: (anonymousName: String) -> T,
        mapper: (T) -> U
    ): ReadOnlyProperty<Kenet, U> {
        val endpoint = create(newEndpointName())

        _endpoints.add(endpoint)

        return ReadOnlyProperty { kenet, property ->
            if (!endpoint.initialized) {
                if (endpoint.kenet !== kenet) {
                    throw AssertionError("API 사용법 오류(Invalid API usage) : Endpoint 를 생성한 클래스와 사용하는 클래스가 다르다.")
                }
                endpoint.name = property.name
                if (endpoint is SubKenet<*>) {
                    endpoint.sub._name = property.name
                }
                endpoint.initialized = true
            }
            mapper(endpoint)
        }
    }

    inline fun <reified P : Any, reified R : Any> c(): ReadOnlyProperty<Kenet, Call<P, R>> {
        return call(serializer(), serializer())
    }

    fun <T : Kenet> c(sub: T): ReadOnlyProperty<Kenet, T> {
        // TODO :: _parent 가 null 인지 확인 추가
        sub._parent = this
        return createEndpointProperty(
            { name -> SubKenet(this, name, sub) },
            { sub }
        )
    }
}

sealed class Endpoint(
    val kenet: Kenet,
    var name: String,
    // OPT :: Visibility 설정
    var initialized: Boolean = false
)

class Call<P : Any, R : Any>(
    kenet: Kenet,
    name: String,
    val parameterSerializer: KSerializer<P>,
    val responseSerializer: KSerializer<R>
) : Endpoint(kenet, name) {
    var handler: (P) -> R =
        { _ -> throw NotImplementedError("API 사용법 오류(Invalid API usage) : ${name}의 핸들러가 정의되지 않았습니다..") }
}

class SubKenet<T : Kenet>(
    kenet: Kenet,
    name: String,
    val sub: T
) : Endpoint(kenet, name)

interface KenetClient

@Serializable
data class Request(val subPath: List<String>, val endpointName: String, val parameterJson: String)

@Serializable
data class Response(val responseJson: String)


val dslJsonFormat = Json { encodeDefaults = true }