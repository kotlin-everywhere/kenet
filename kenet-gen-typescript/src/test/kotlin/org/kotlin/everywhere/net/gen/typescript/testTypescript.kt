package org.kotlin.everywhere.net.gen.typescript

import org.kotlin.everywhere.net.Kenet
import kotlin.reflect.full.createType
import kotlin.test.Test
import kotlin.test.assertEquals

class TestTypescript {
    @Test
    fun testDefine() {
        class Def : Kenet() {
            val echo by c<String, String>()
            val toString by c<Int, String>()
        }

        val def = Def()
        val definition = define(def)
        assertEquals("Def", definition.name, "Kenet 을 정의한 Class 명 확인")
        assertEquals(2, definition.callDefinitions.size, "정의한 endpoint 갯수 출력 확인")
        assertEquals("echo", definition.callDefinitions[0].name, "첫번째 endpoint 이름 echo 인것 확인")
        assertEquals(def.echo.name, definition.callDefinitions[0].name, "첫번째 endpoint 이름 실제 endpoint 와 동일한지 확인")
        assertEquals(
            String::class.createType(),
            definition.callDefinitions[0].parameterType,
            "첫번째 endpoint 파라미터 String 타입확인"
        )
        assertEquals(
            String::class.createType(),
            definition.callDefinitions[0].responseType,
            "첫번째 endpoint 응답 String 타입확인"
        )
        assertEquals("toString", definition.callDefinitions[1].name, "두번째 endpoint 이름 toString 인것 확인")
        assertEquals(def.toString.name, definition.callDefinitions[1].name, "두번째 endpoint 이름 실제 endpoint 와 동일한지 확인")
        assertEquals(
            Int::class.createType(),
            definition.callDefinitions[1].parameterType,
            "두번째 endpoint 파라미터 String 타입확인"
        )
        assertEquals(
            String::class.createType(),
            definition.callDefinitions[1].responseType,
            "두번째 endpoint 응답 String 타입확인"
        )
    }

    @Test
    fun testDefinitionOrder() {
        // 리플렉션은 필드의 순서를 보장하지 않는다. 항상 동일한 결과값을 보장하기 위해서 이름 순으로 정렬한다.
        class Def : Kenet() {
            val c by c<Unit, Unit>()
            val b by c<Unit, Unit>()
            val a by c<Unit, Unit>()
        }
        assertEquals(
            listOf("a", "b", "c"),
            define(Def()).callDefinitions.map { it.name }
        )
    }

    @Test
    fun testRenderKenetDefinition() {
        // 전체 규격 확인
        assertEquals(
            listOf("class Def extends KenetClient {", "}"),
            render(KenetDefinition("Def", listOf()))
        )

        // 클래스 이름 출력 확인
        assertEquals(
            listOf("class Api extends KenetClient {", "}"),
            render(KenetDefinition("Api", listOf()))
        )

        // 콜 출력 확인
        assertEquals(
            listOf(
                "class Def extends KenetClient {",
                "readonly echo = this.c<string, string>('echo');",
                "}"
            ),
            render(
                KenetDefinition(
                    "Def",
                    listOf(
                        CallDefinition(
                            "echo",
                            String::class.createType(),
                            String::class.createType()
                        )
                    )
                )
            )
        )
    }

    @Test
    fun testRenderCallDefinition() {
        // 호출 전체 규격 확인
        assertEquals(
            listOf("readonly echo = this.c<string, string>('echo');"),
            render(CallDefinition("echo", String::class.createType(), String::class.createType()))
        )

        // 호출 규격 이름 확인
        assertEquals(
            listOf("readonly reflect = this.c<string, string>('reflect');"),
            render(CallDefinition("reflect", String::class.createType(), String::class.createType()))
        )

        // 호출 규격 파라미터 타입 확인
        assertEquals(
            listOf("readonly toString = this.c<number, string>('toString');"),
            render(CallDefinition("toString", Int::class.createType(), String::class.createType()))
        )

        // 호출 응답 규격 타입 확인
        assertEquals(
            listOf("readonly parseInt = this.c<string, number>('parseInt');"),
            render(CallDefinition("parseInt", String::class.createType(), Int::class.createType()))
        )
    }

    @Test
    fun testRenderType() {
        // Int -> number
        assertEquals(
            "number",
            renderType(Int::class.createType())
        )

        // String -> string
        assertEquals(
            "string",
            renderType(String::class.createType())
        )
    }

    @Test
    fun testGenerate() {
        class Def : Kenet() {
            val echo by c<String, String>()
            val toString by c<Int, String>()
            val parseInt by c<String, Int>()
            val reflect by c<Int, Int>()
        }

        assertEquals(
            """
                import { KenetClient } from './kenet.ts';
                
                class Def extends KenetClient {
                readonly echo = this.c<string, string>('echo');
                readonly parseInt = this.c<string, number>('parseInt');
                readonly reflect = this.c<number, number>('reflect');
                readonly toString = this.c<number, string>('toString');
                }
            """.trimIndent(),
            generate(Def())
        )
    }
}