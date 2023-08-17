package org.kodein.type

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class ArrayTests {

    @Test
    fun test00_genericArrays() {
        assertEquals<TypeToken<*>>(generic<Array<Byte>>(), erasedComp(Array::class, erased<Byte>()))
        assertEquals<TypeToken<*>>(generic<ByteArray>(), erased<ByteArray>())
        assertEquals<TypeToken<*>>(generic<Array<String>>(), erasedComp(Array::class, erased<String>()))
        assertEquals<TypeToken<*>>(generic<Array<List<String>>>(), erasedComp(Array::class, erasedComp(List::class, erased<String>())))
        assertEquals<TypeToken<*>>(generic<Array<List<*>>>(), erasedComp(Array::class, erased<List<*>>()))
    }

    @Test
    fun test01_arraySuper() {
        assertEquals(emptyList(), generic<Array<List<String>>>().getSuper())
        assertEquals(emptyList(), erasedComp(Array::class, erasedComp(List::class, erased(String::class))).getSuper())
    }

    @Test
    fun test02_isWildcard() {
        assertTrue(generic<Array<*>>().isWildcard())
        assertFalse(generic<Array<List<*>>>().isWildcard())
    }

    @Test
    fun test03_arrayAsString() {
        assertEquals("Array<Byte>", generic<Array<Byte>>().simpleDispString())
        assertEquals("Array<Byte>", erasedComp(Array::class, erased(Byte::class)).simpleDispString())
    }

    @Test
    fun test04_primitiveArrayToString() {
        assertEquals("ByteArray", generic<ByteArray>().simpleDispString())
        assertEquals("ByteArray", erased<ByteArray>().simpleDispString())
    }
}
