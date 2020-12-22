package org.kodein.type

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class ArrayTests {

    @Test
    fun genericArrays() {
        assertEquals<TypeToken<*>>(generic<Array<String>>(), erasedComp(Array::class, erased(String::class)))
        assertEquals<TypeToken<*>>(generic<Array<List<String>>>(), erasedComp(Array::class, erasedComp(List::class, erased(String::class))))
    }

    @Test
    fun arraySuper() {
        assertEquals(emptyList(), generic<Array<List<String>>>().getSuper())
        assertEquals(emptyList(), erasedComp(Array::class, erasedComp(List::class, erased(String::class))).getSuper())
    }

    @Test
    fun isWildcard() {
        assertTrue(generic<Array<*>>().isWildcard())
        assertFalse(generic<Array<List<*>>>().isWildcard())
    }

    @Test
    fun arrayAsString() {
        assertEquals("Array<Byte>", generic<Array<Byte>>().simpleDispString())
    }

}
