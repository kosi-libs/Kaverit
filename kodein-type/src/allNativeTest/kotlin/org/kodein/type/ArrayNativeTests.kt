package org.kodein.type

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ArrayNativeTests {

    @Test
    fun simpleArrayIsNotGeneric() {
        assertTrue(generic<Array<String>>().isGeneric())
        assertTrue(erasedComp(Array::class, erased(String::class)).isGeneric())
        assertTrue(generic<Array<List<String>>>().isGeneric())
        assertTrue(erasedComp(Array::class, erasedComp(List::class, erased(String::class))).isGeneric())
    }

}
