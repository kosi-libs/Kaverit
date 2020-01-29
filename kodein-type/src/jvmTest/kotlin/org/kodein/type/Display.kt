package org.kodein.type

import kotlin.test.Test
import kotlin.test.assertEquals

class Display {

    @Test fun test00_simple() {
        assertEquals("Map<String, out Int>", generic<Map<String, Int>>().simpleDispString())
        assertEquals("Map<String, Int>", erasedComp(Map::class, String::class, Int::class).simpleDispString())
        assertEquals("Map<*, *>", erased<Map<*, *>>().simpleDispString())
    }

    @Test fun test01_qualified() {
        assertEquals("kotlin.collections.Map<kotlin.String, out kotlin.Int>", generic<Map<String, Int>>().qualifiedDispString())
        assertEquals("kotlin.collections.Map<kotlin.String, kotlin.Int>", erasedComp(Map::class, String::class, Int::class).qualifiedDispString())
        assertEquals("kotlin.collections.Map<*, *>", erased<Map<*, *>>().qualifiedDispString())
    }

}
