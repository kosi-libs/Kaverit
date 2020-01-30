package org.kodein.type

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Assign {

    @Test fun test00_self() {
        assertTrue(erased<String>().isAssignableFrom(erased<String>()))
        assertTrue(generic<List<String>>().isAssignableFrom(generic<List<String>>()))
        assertTrue(generic<List<String>>().isAssignableFrom(erasedComp(List::class, erased(String::class))))
        assertTrue(erasedComp(List::class, erased(String::class)).isAssignableFrom(generic<List<String>>()))
    }

    @Test fun test01_any() {
        assertTrue(erased<Any>().isAssignableFrom(erased<String>()))
        assertTrue(erased<Any>().isAssignableFrom(generic<List<String>>()))
        assertTrue(erased<Any>().isAssignableFrom(erasedComp(List::class, erased(String::class))))
    }

    @Test fun test02_generics() {
        assertTrue(generic<Map<String, Any>>().isAssignableFrom(generic<Map<String, String>>()))
        assertTrue(generic<Map<String, Any>>().isAssignableFrom(erasedComp(Map::class, erased(String::class), erased(String::class))))
        assertTrue(erasedComp(Map::class, erased(String::class), TypeToken.Any).isAssignableFrom(generic<Map<String, String>>()))

        assertFalse(generic<Map<String, Int>>().isAssignableFrom(generic<Map<String, String>>()))
        assertFalse(generic<Map<String, Int>>().isAssignableFrom(erasedComp(Map::class, erased(String::class), erased(String::class))))
        assertFalse(erasedComp(Map::class, erased(String::class), erased(Int::class)).isAssignableFrom(generic<Map<String, String>>()))
    }
}
