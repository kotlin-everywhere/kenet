package org.kotlin.everywhere.net.gen.typescript

import org.kotlin.everywhere.net.Kenet
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TestTypescript {
    @Test
    fun testDefine() {
        class Def : Kenet() {
            val echo by c<String, String>()
            val toString by c<Int, String>()
            val sub by c(object : Kenet() {
                val echo2 by c<String, String>()
                val sub2 by c(object : Kenet() {
                    val echo3 by c<String, String>()
                })
            })
        }

        val def = Def()
        val definition = define(def)

        assertEquals("Def", definition.name, "Kenet 을 정의한 Class 명 확인")
        assertEquals(3, definition.definitions.size, "정의한 endpoint 갯수 출력 확인")

        // 첫번째 확인
        val echo = definition.definitions[0]
        assertIs<CallDefinition>(echo, "echo 는 호출 지점이다.")
        assertEquals("echo", echo.name, "첫번째 endpoint 이름 echo 인것 확인")
        assertEquals(def.echo.name, echo.name, "첫번째 endpoint 이름 실제 endpoint 와 동일한지 확인")
        assertEquals(
            String::class.createType(),
            echo.parameterType,
            "첫번째 endpoint 파라미터 String 타입확인"
        )
        assertEquals(
            String::class.createType(),
            echo.responseType,
            "첫번째 endpoint 응답 String 타입확인"
        )

        // 두번째 확인
        val sub = definition.definitions[1]
        assertIs<SubDefinition>(sub, "sub 는 하위 Kenet 이다.")
        assertEquals("sub", sub.name, "두번째 endpoint 의 이름 sub 인것 확인")
        assertEquals(def.sub._name, sub.name, "두번째 endpoint 의 이름 실제 endpoint 와 동일한지 확인")

        // 세번쩨 확인
        val toString = definition.definitions[2]
        assertIs<CallDefinition>(toString, "toString 은 호출 지점이다.")
        assertEquals("toString", toString.name, "세번째 endpoint 이름 toString 인것 확인")
        assertEquals(def.toString.name, toString.name, "세번째 endpoint 이름 실제 endpoint 와 동일한지 확인")
        assertEquals(
            Int::class.createType(),
            toString.parameterType,
            "세번째 endpoint 파라미터 String 타입확인"
        )
        assertEquals(
            String::class.createType(),
            toString.responseType,
            "세번째 endpoint 응답 String 타입확인"
        )
    }

    @Test
    fun testDefinitionOrder() {
        // 리플렉션은 필드의 순서를 보장하지 않는다. 항상 동일한 결과값을 보장하기 위해서 이름 순으로 정렬한다.
        class Def : Kenet() {
            val d by c(object : Kenet() {})
            val c by c<Unit, Unit>()
            val b by c<Unit, Unit>()
            val a by c<Unit, Unit>()
        }
        assertEquals(
            listOf("a", "b", "c", "d"),
            define(Def()).definitions.map { it.name }
        )
    }

    @Test
    fun testRenderKenetDefinition() {
        // 전체 규격 확인
        assertEquals(
            listOf("export class Def extends KenetClient {", "}"),
            render(KenetDefinition("Def", listOf()))
        )

        // 클래스 이름 출력 확인
        assertEquals(
            listOf("export class Api extends KenetClient {", "}"),
            render(KenetDefinition("Api", listOf()))
        )

        // 콜 출력 확인
        assertEquals(
            listOf(
                "export class Def extends KenetClient {",
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

        // Array -> []
        assertEquals(
            "string[]",
            renderType(
                Array<String>::class.createType(listOf(KTypeProjection.invariant(String::class.createType())))
            )
        )

        // object type
        class Person(val name: String, val age: Int, val tags: Array<String>)
        assertEquals("{age: number, name: string, tags: string[]}", renderType(Person::class.createType()))
    }

    @Test
    fun testGenerate() {
        class Def : Kenet() {
            val echo by c<String, String>()
            val toString by c<Int, String>()
            val parseInt by c<String, Int>()
            val reflect by c<Int, Int>()
            val sub by c(object : Kenet() {
                val echo2 by c<String, String>()
                val sub2 by c(object : Kenet() {
                    val echo3 by c<String, String>()
                })
            })
        }

        assertEquals(
            """
                import { KenetClient } from './kenet.ts';
                
                export class Def extends KenetClient {
                readonly echo = this.c<string, string>('echo');
                readonly parseInt = this.c<string, number>('parseInt');
                readonly reflect = this.c<number, number>('reflect');
                readonly sub = this.s('sub', new (class sub extends KenetClient {
                readonly echo2 = this.c<string, string>('echo2');
                readonly sub2 = this.s('sub2', new (class sub2 extends KenetClient {
                readonly echo3 = this.c<string, string>('echo3');
                }));
                }));
                readonly toString = this.c<number, string>('toString');
                }
            """.trimIndent(),
            generate(Def(), Deno)
        )
    }

    @Test
    fun testImportStatement() {
        class Def : Kenet()

        assertEquals(
            """
                import { KenetClient } from './kenet.ts';
                
                export class Def extends KenetClient {
                }
            """.trimIndent(),
            generate(Def(), Deno),
            "deno 옵션일 경우 import 할때 확장자를 포함한다."
        )
        assertEquals(
            """
                import { KenetClient } from './kenet';
                
                export class Def extends KenetClient {
                }
            """.trimIndent(),
            generate(Def(), TypeScript),
            "ts 옵션일 경우 import 할때 확장자를 포함하지 않는다."
        )
    }
}