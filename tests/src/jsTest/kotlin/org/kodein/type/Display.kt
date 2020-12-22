package org.kodein.type

import kotlin.test.Test
import kotlin.test.assertEquals

class Display {

    @Test fun test00_simple() {
        assertEquals("Map<String, Int>", generic<Map<String, Int>>().simpleDispString())
        assertEquals("Map<*, *>", generic<Map<*, *>>().simpleDispString())
        assertEquals("Map<String, Int>", erasedComp(Map::class, erased(String::class), erased(Int::class)).simpleDispString())
        assertEquals("Map", erased<Map<*, *>>().simpleDispString())
    }

    @Test fun test01_simpleErased() {
        assertEquals("Map", generic<Map<String, Int>>().simpleErasedDispString())
        assertEquals("Map", generic<Map<*, *>>().simpleErasedDispString())
        assertEquals("Map", erasedComp(Map::class, erased(String::class), erased(Int::class)).simpleErasedDispString())
        assertEquals("Map", erased<Map<*, *>>().simpleErasedDispString())
    }

}
