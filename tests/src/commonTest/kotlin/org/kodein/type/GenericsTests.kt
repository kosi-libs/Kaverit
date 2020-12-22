package org.kodein.type

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GenericsTests {

    @Test fun test00_genericReifiedParameter() {
        val typeToken = generic<List<String>>()
        assertEquals(1, typeToken.getGenericParameters().size)
        assertEquals(erased<String>(), typeToken.getGenericParameters()[0])
    }

    @Test fun test01_genericStarParameter() {
        val typeToken = generic<List<*>>()
        assertEquals(1, typeToken.getGenericParameters().size)
        assertEquals(TypeToken.Any, typeToken.getGenericParameters()[0])
    }

    @Test fun test02_genericRaw() {
        assertEquals<TypeToken<*>>(erased<List<*>>(), generic<List<*>>().getRaw())
        assertEquals<TypeToken<*>>(erased<List<*>>(), erased<List<*>>().getRaw())
        assertEquals<TypeToken<*>>(erased<List<*>>(), erasedComp(List::class, erased(String::class)).getRaw())
    }

    @Test fun test03_isGeneric() {
        assertTrue(generic<List<String>>().isGeneric())
        assertFalse(generic<String>().isGeneric())
        assertFalse(erased<List<String>>().isGeneric())
        assertTrue(erasedComp(List::class, erased(String::class)).isGeneric())
        assertFalse(erasedComp(String::class).isGeneric())
    }

    @Test fun test04_isWildcard() {
        assertFalse(generic<List<String>>().isWildcard())
        assertTrue(generic<List<*>>().isWildcard())
        assertTrue(erased<List<*>>().isWildcard())
        assertFalse(erasedComp(List::class, erased(String::class)).isWildcard())
        assertTrue(erasedComp(List::class, TypeToken.Any).isWildcard())
    }


}
