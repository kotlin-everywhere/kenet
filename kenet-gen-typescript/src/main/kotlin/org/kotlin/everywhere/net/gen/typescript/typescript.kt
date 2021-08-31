package org.kotlin.everywhere.net.gen.typescript

import org.kotlin.everywhere.net.Call
import org.kotlin.everywhere.net.Kenet
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf

data class KenetDefinition(val name: String, val callDefinitions: List<CallDefinition>)
data class CallDefinition(val name: String, val parameterType: KType, val responseType: KType)

internal fun define(kenet: Kenet): KenetDefinition {
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
        // 리플렉션은 필드의 순서를 보장하지 않는다. 항상 동일한 결과값을 보장하기 위해서 이름 순으로 정렬한다.
        .sortedBy { it.name }
    return KenetDefinition(name, callDefinitions)
}

internal fun render(def: KenetDefinition): List<String> {
    return listOf("class ${def.name} extends KenetClient {") +
            def.callDefinitions.flatMap { render(it) } +
            listOf("}")
}

internal fun render(def: CallDefinition): List<String> {
    return listOf("readonly ${def.name} = this.c<${renderType(def.parameterType)}, ${renderType(def.responseType)}>('${def.name}');")
}

internal fun renderType(createType: KType): String {
    return when (createType) {
        Int::class.createType() -> "number"
        String::class.createType() -> "string"
        else -> TODO("기타 다른 타입에 대한 처리")
    }
}

fun generate(kenet: Kenet): String {
    return (listOf("import { KenetClient } from './kenet.ts';", "") + render(define(kenet)))
        .joinToString("\n")
}