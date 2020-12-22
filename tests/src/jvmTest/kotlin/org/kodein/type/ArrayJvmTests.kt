package org.kodein.type

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ArrayJvmTests {

    @Test
    fun nonGenericArray() {
        assertEquals<TypeToken<*>>(generic<Array<String>>(), erased<Array<String>>())
    }

    @Test
    fun simpleArrayIsNotGeneric() {
        assertFalse(generic<Array<String>>().isGeneric())
        assertFalse(erased<Array<String>>().isGeneric())
        assertFalse(erasedComp(Array::class, erased(String::class)).isGeneric())
        assertTrue(generic<Array<List<String>>>().isGeneric())
        assertTrue(erasedComp(Array::class, erasedComp(List::class, erased(String::class))).isGeneric())
    }

}
