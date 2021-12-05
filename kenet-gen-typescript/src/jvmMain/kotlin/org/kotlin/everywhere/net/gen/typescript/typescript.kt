package org.kotlin.everywhere.net.gen.typescript

import org.kotlin.everywhere.net.Call
import org.kotlin.everywhere.net.Fire
import org.kotlin.everywhere.net.Kenet
import java.io.File
import java.nio.file.Path
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties

data class KenetDefinition(val name: String, val definitions: List<EndpointDefinition>)
sealed class EndpointDefinition {
    abstract val name: String
}

data class CallDefinition(override val name: String, val parameterType: KType, val responseType: KType) :
    EndpointDefinition()

data class FireDefinition(override val name: String, val parameterType: KType) : EndpointDefinition()

data class SubDefinition(override val name: String, val kenetDefinition: KenetDefinition) : EndpointDefinition()

internal fun define(kenet: Kenet, name: String? = null): KenetDefinition {
    val className =
        kenet::class.simpleName
            ?: name
            ?: throw IllegalArgumentException("Cannot find class simple name : kenet = $kenet")
    val definitions = kenet::class
        .members
        // TODO :: reflection 제거
        .map {
            when {
                it.returnType.isSubtypeOf(
                    Call::class.createType(
                        listOf(
                            KTypeProjection.STAR,
                            KTypeProjection.STAR
                        )
                    )
                ) -> {
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
                it.returnType.isSubtypeOf(Fire::class.createType(listOf(KTypeProjection.STAR))) -> {
                    require(it.returnType.arguments.size == 1) {
                        "invalid endpoint parameter length, it must be 1(Parameter) types : parameters = ${it.returnType.arguments}"
                    }
                    val (parameterType) = it.returnType.arguments
                    FireDefinition(
                        it.name,
                        parameterType.type
                            ?: throw IllegalArgumentException("call endpoint parameter type missing : parameterType = $parameterType"),
                    )
                }
                it.returnType.isSubtypeOf(Kenet::class.createType()) -> {
                    SubDefinition(it.name, define(it.call(kenet) as Kenet, it.name))
                }
                else -> {
                    null
                }
            }
        }
        .filterNotNull()
        // 리플렉션은 필드의 순서를 보장하지 않는다. 항상 동일한 결과값을 보장하기 위해서 이름 순으로 정렬한다.
        .sortedBy { it.name }
    return KenetDefinition(className, definitions)
}

internal fun render(def: KenetDefinition, subName: String? = null): List<String> {
    return if (subName == null) {
        listOf("export class ${def.name} extends KenetClient {") +
                def.definitions.flatMap { render(it) } +
                listOf("}")
    } else {
        listOf("readonly $subName = this.s('${subName}', new (class ${def.name} extends KenetClient {") +
                def.definitions.flatMap { render(it) } +
                listOf("}));")
    }
}

internal fun render(def: EndpointDefinition): List<String> {
    return when (def) {
        is CallDefinition -> render(def)
        is FireDefinition -> render(def)
        is SubDefinition -> render(def.kenetDefinition, def.name)
    }
}

internal fun render(def: CallDefinition): List<String> {
    return listOf("readonly ${def.name} = this.c<${renderType(def.parameterType)}, ${renderType(def.responseType)}>('${def.name}');")
}

internal fun render(def: FireDefinition): List<String> {
    return listOf("readonly ${def.name} = this.f<${renderType(def.parameterType)}>('${def.name}');")
}

internal fun renderType(createType: KType): String {
    val rendered = when (createType) {
        Int::class.createType(), Int::class.createType(nullable = true) -> "number"
        String::class.createType(), String::class.createType(nullable = true) -> "string"
        Array<String>::class.createType(listOf(KTypeProjection.invariant(String::class.createType()))) -> "string[]"
        else -> {
            if (createType.toString().startsWith("kotlin.collections.List<")) {
                val listTypeArgument = createType.arguments.first().type
                    ?: throw IllegalArgumentException("List type argument missing")
                return renderType(listTypeArgument) + "[]"
            }
            val kClass: KClass<*> = createType.classifier as KClass<*>
            kClass.memberProperties
                .asSequence()
                .map { it.name to renderType(it.returnType) }
                .sortedBy { it.first }
                .map { "${it.first}: ${it.second}" }
                .joinToString(", ", prefix = "{", postfix = "}")
        }
    }
    return rendered + (if (createType.isMarkedNullable)  " | null" else "")
}

sealed class Variant
object TypeScript : Variant()
object Deno : Variant()

fun generate(kenet: Kenet, variant: Variant): String {
    return (listOf(
        when (variant) {
            Deno ->
                "import { KenetClient } from './kenet.ts';"
            TypeScript ->
                "import { KenetClient } from './kenet';"
        },
        ""
    ) + render(define(kenet)))
        .joinToString("\n")
}

fun generate(kenet: Kenet, path: Path, name: String, variant: Variant) {
    val directory = path.toFile()
    if (!directory.exists()) {
        assert(directory.mkdirs()) { "cannot create target path : path=${path}" }
    }

    File(directory, "kenet.ts").writeBytes(
        object {}.javaClass.getResourceAsStream("/kenet.ts").use { it!!.readAllBytes() }
    )
    File(directory, "$name.ts").writeText(generate(kenet, variant))
}