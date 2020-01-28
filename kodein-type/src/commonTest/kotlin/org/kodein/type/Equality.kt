package org.kodein.type

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@Suppress("RemoveExplicitTypeArguments")
class Equality {

    @Test fun test00_erased() {
        assertEquals<TypeToken<*>>(erased<String>(), erased<String>())
        assertEquals<TypeToken<*>>(erased<String>(), erased(String::class))
        assertNotEquals<TypeToken<*>>(erased<String>(), erased<Int>())
        assertEquals<TypeToken<*>>(erased<List<String>>(), erased<List<Int>>())
    }

    @Test fun test01_generic() {
        assertEquals<TypeToken<*>>(generic<String>(), generic<String>())
        assertNotEquals<TypeToken<*>>(generic<String>(), generic<Int>())
        assertEquals<TypeToken<*>>(generic<List<String>>(), generic<List<String>>())
        assertNotEquals<TypeToken<*>>(generic<List<String>>(), generic<List<Int>>())
    }

    @Test fun test02_composite() {
        assertEquals<TypeToken<*>>(erasedComp2<Map<String, String>, String, String>(), erasedComp2<Map<String, String>, String, String>())
        assertNotEquals<TypeToken<*>>(erasedComp2<Map<String, String>, String, String>(), erasedComp2<Map<String, Int>, String, Int>())
    }

    @Test fun test03_erasedEqualsGeneric() {
        assertEquals<TypeToken<*>>(erased<String>(), generic<String>())
        assertEquals<TypeToken<*>>(generic<String>(), erased<String>())
        assertNotEquals<TypeToken<*>>(erased<String>(), generic<Int>())
        assertNotEquals<TypeToken<*>>(generic<String>(), erased<Int>())
        assertEquals<TypeToken<*>>(erased<List<*>>(), generic<List<*>>())
        assertEquals<TypeToken<*>>(generic<List<*>>(), erased<List<*>>())
    }

    @Test fun test04_genericEqualsComposite() {
        assertEquals<TypeToken<*>>(generic<Map<String, String>>(), erasedComp2<Map<String, String>, String, String>())
        assertEquals<TypeToken<*>>(erasedComp2<Map<String, String>, String, String>(), generic<Map<String, String>>())
        assertNotEquals<TypeToken<*>>(generic<Map<String, String>>(), erasedComp2<Map<String, Int>, String, Int>())
        assertNotEquals<TypeToken<*>>(erasedComp2<Map<String, Int>, String, Int>(), generic<Map<String, String>>())
    }

}
