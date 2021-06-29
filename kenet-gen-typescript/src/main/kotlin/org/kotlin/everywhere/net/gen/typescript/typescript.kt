package org.kotlin.everywhere.net.gen.typescript

import org.kotlin.everywhere.net.Call
import org.kotlin.everywhere.net.Kenet
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf

data class KenetDefinition(val name: String, val callDefinitions: List<CallDefinition>)
data class CallDefinition(val name: String, val parameterType: KType, val responseType: KType)

fun define(kenet: Kenet): KenetDefinition {
    val name =
        kenet::class.simpleName ?: throw IllegalArgumentException("Cannot find class simple name : kenet = $kenet")
    val callDefinitions = kenet::class
        .members
        // Call 멤버들만
        .filter {
            it.returnType.isSubtypeOf(
                Call::class.createType(
                    listOf(
                        KTypeProjection.STAR,
                        KTypeProjection.STAR
                    )
                )
            )
        }
        .map {
            require(it.returnType.arguments.size == 2) {
                "invalid endpoint parameter length, it must be 2(Parameter, Response) types : parameters = ${it.returnType.arguments}"
            }
            val (parameterType, responseType) = it.returnType.arguments
            CallDefinition(
                it.name,
                parameterType.type
                    ?: throw IllegalArgumentException("call endpoint parameter type missing : parameterType = $parameterType"),
                responseType.type
                    ?: throw IllegalArgumentException("call endpoint response type missing : responseType = $responseType")
            )
        }
    return KenetDefinition(name, callDefinitions)
}