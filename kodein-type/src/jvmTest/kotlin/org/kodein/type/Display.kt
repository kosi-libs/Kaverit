package org.kodein.type

import kotlin.test.Test
import kotlin.test.assertEquals

class Display {

    @Test fun test00_simple() {
        assertEquals("Map<String, out Int>", generic<Map<String, Int>>().simpleDispString())
        assertEquals("Map<String, Int>", erasedComp(Map::class, erased(String::class), erased(Int::class)).simpleDispString())
        assertEquals("Map<*, *>", erased<Map<*, *>>().simpleDispString())
    }

    @Test fun test01_qualified() {
        assertEquals("kotlin.collections.Map<kotlin.String, out kotlin.Int>", generic<Map<String, Int>>().qualifiedDispString())
        assertEquals("kotlin.collections.Map<kotlin.String, kotlin.Int>", erasedComp(Map::class, erased(String::class), erased(Int::class)).qualifiedDispString())
        assertEquals("kotlin.collections.Map<*, *>", erased<Map<*, *>>().qualifiedDispString())
    }

    @Test fun test02_simpleErased() {
        assertEquals("Map", generic<Map<String, Int>>().simpleErasedDispString())
        assertEquals("Map", erasedComp(Map::class, erased(String::class), erased(Int::class)).simpleErasedDispString())
        assertEquals("Map", erased<Map<*, *>>().simpleErasedDispString())
    }

    @Test fun test03_qualifiedErased() {
        assertEquals("kotlin.collections.Map", generic<Map<String, Int>>().qualifiedErasedDispString())
        assertEquals("kotlin.collections.Map", erasedComp(Map::class, erased(String::class), erased(Int::class)).qualifiedErasedDispString())
        assertEquals("kotlin.collections.Map", erased<Map<*, *>>().qualifiedErasedDispString())
    }

}
