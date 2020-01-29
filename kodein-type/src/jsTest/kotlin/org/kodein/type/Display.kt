package org.kodein.type

import kotlin.test.Test
import kotlin.test.assertEquals

class Display {

    @Test fun test00_simple() {
        assertEquals("Map<String, Int>", generic<Map<String, Int>>().simpleDispString())
        assertEquals("Map<*, *>", generic<Map<*, *>>().simpleDispString())
        assertEquals("Map<String, Int>", erasedComp(Map::class, String::class, Int::class).simpleDispString())
        assertEquals("Map", erased<Map<*, *>>().simpleDispString())
    }

}
