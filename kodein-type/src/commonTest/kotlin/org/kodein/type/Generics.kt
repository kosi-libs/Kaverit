package org.kodein.type

import kotlin.test.Test
import kotlin.test.assertEquals

class Generics {

    @Test fun test00_genericReifiedParameter() {
        val tt = generic<List<String>>()
        assertEquals(1, tt.getGenericParameters().size)
        assertEquals(erased<String>(), tt.getGenericParameters()[0])
    }

    @Test fun test01_genericStarParameter() {
        val tt = generic<List<*>>()
        assertEquals(1, tt.getGenericParameters().size)
        assertEquals(TypeToken.Any, tt.getGenericParameters()[0])
    }

}
