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
}