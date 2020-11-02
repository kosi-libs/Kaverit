package org.kodein.type

import kotlin.test.Test
import kotlin.test.assertEquals


class ArrayJvmTests {

    @Test
    fun arrays() {
        assertEquals(generic<Array<List<String>>>(), erasedComp(Array::class, erased(List::class), erased(String::class)))
    }

}
