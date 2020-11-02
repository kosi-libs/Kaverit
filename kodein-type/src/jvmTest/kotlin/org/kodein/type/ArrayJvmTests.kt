package org.kodein.type

import kotlin.test.Test
import kotlin.test.assertEquals

class ArrayJvmTests {

    @Test
    fun nonGenericArray() {
        assertEquals<TypeToken<*>>(generic<Array<String>>(), erased<Array<String>>())
    }

}
