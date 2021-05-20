package org.kotlin.everywhere.net.dsl

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass

@Suppress("PropertyName", "FunctionName")
abstract class Kenet {
    private val _endpoints = mutableListOf<Endpoint>()

    fun <P : Any> _fire(parameter: KClass<P>): ReadOnlyProperty<Kenet, Fire<P>> {
        var endpoint: Fire<P>? = null
        return ReadOnlyProperty { kenet, property ->
            val endpointInstance = endpoint
            if (endpointInstance != null) {
                return@ReadOnlyProperty endpointInstance
            }
            val newEndpointInstance = Fire(kenet, property.name, parameter)
            endpoint = newEndpointInstance
            kenet._endpoints.add(newEndpointInstance)
            newEndpointInstance
        }
    }

    fun <P : Any, R : Any> _call(parameter: KClass<P>, response: KClass<R>): ReadOnlyProperty<Kenet, Call<P, R>> {
        var endpoint: Call<P, R>? = null
        return ReadOnlyProperty { kenet, property ->
            val endpointInstance = endpoint
            if (endpointInstance != null) {
                return@ReadOnlyProperty endpointInstance
            }
            val newEndpointInstance = Call(kenet, property.name, parameter, response)
            endpoint = newEndpointInstance
            _endpoints.add(newEndpointInstance)
            newEndpointInstance
        }
    }
}

@PublishedApi
internal inline fun <reified P : Any> Kenet.f(): ReadOnlyProperty<Kenet, Fire<P>> {
    return _fire(P::class)
}

@PublishedApi
internal inline fun <reified P : Any, reified R : Any> Kenet.c(): ReadOnlyProperty<Kenet, Call<P, R>> {
    return _call(P::class, R::class)
}

sealed class Endpoint

class Fire<P : Any>(val kenet: Kenet, val name: String, val parameterClass: KClass<P>) : Endpoint() {
    operator fun invoke(parameter: P) {
        TODO()
    }
}

class Call<P : Any, R : Any>(
    val kenet: Kenet,
    val name: String,
    val parameterClass: KClass<P>,
    val responseClass: KClass<R>
) : Endpoint() {
    operator fun invoke(parameter: P): R {
        TODO()
    }
}
