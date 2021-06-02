package org.kotlin.everywhere.net

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass

abstract class Kenet {
    val _endpoints = mutableListOf<Endpoint<*>>()

    var client: KenetClient? = null

    /**
     * 최초 생성시 이름 지정용 인덱스, 그냥 이름 자리에 null 이 들어가는게 싫다.
     */
    private var anonymousEndpointIndex = 0

    @PublishedApi
    internal fun <P : Any> fire(parameter: KClass<P>): ReadOnlyProperty<Kenet, Fire<P>> =
        createEndpointProperty { index, anonymousName ->
            Fire(this, index, false, anonymousName, parameter)
        }

    @PublishedApi
    internal fun <P : Any, R : Any> call(
        parameter: KClass<P>,
        response: KClass<R>
    ): ReadOnlyProperty<Kenet, Call<P, R>> = createEndpointProperty { index, anonymousName ->
        Call(this, index, false, anonymousName, parameter, response)
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
}

inline fun <reified P : Any> Kenet.f(): ReadOnlyProperty<Kenet, Fire<P>> {
    return fire(P::class)
}

inline fun <reified P : Any, reified R : Any> Kenet.c(): ReadOnlyProperty<Kenet, Call<P, R>> {
    return call(P::class, R::class)
}

sealed class Endpoint<P : Any>(
    val kenet: Kenet,
    internal val index: Int,
    internal var initialized: Boolean,
    internal var name: String,
    internal val parameterClass: KClass<P>
)

class Fire<P : Any>(kenet: Kenet, index: Int, initialized: Boolean, name: String, parameterClass: KClass<P>) :
    Endpoint<P>(kenet, index, initialized, name, parameterClass)

class Call<P : Any, R : Any>(
    kenet: Kenet, index: Int, initialized: Boolean, name: String, parameterClass: KClass<P>,
    val responseClass: KClass<R>
) : Endpoint<P>(kenet, index, initialized, name, parameterClass)

interface KenetClient