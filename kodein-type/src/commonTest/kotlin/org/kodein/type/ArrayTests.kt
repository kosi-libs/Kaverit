package org.kodein.type

import kotlin.test.Test
import kotlin.test.assertEquals


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
}
