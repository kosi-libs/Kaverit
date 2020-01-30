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
        assertEquals<TypeToken<*>>(erasedComp(Map::class, erased(String::class), erased(String::class)), erasedComp(Map::class, erased(String::class), erased(String::class)))
        assertNotEquals<TypeToken<*>>(erasedComp(Map::class, erased(String::class), erased(String::class)), erasedComp(Map::class, erased(String::class), erased(Int::class)))
        assertEquals(erasedComp(String::class), erased<String>())
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
        assertEquals<TypeToken<*>>(generic<Map<String, String>>(), erasedComp(Map::class, erased(String::class), erased(String::class)))
        assertEquals<TypeToken<*>>(erasedComp(Map::class, erased(String::class), erased(String::class)), generic<Map<String, String>>())
        assertNotEquals<TypeToken<*>>(generic<Map<String, String>>(), erasedComp(Map::class, erased(String::class), erased(Int::class)))
        assertNotEquals<TypeToken<*>>(erasedComp(Map::class, erased(String::class), erased(Int::class)), generic<Map<String, String>>())
        assertEquals<TypeToken<*>>(generic<Map<String, *>>(), erasedComp(Map::class, erased(String::class), TypeToken.Any))
        assertEquals<TypeToken<*>>(erasedComp(Map::class, erased(String::class), TypeToken.Any), generic<Map<String, *>>())
    }

    @Test fun test05_erasedOf() {
        assertEquals<TypeToken<*>>(erasedOf("a"), erasedOf("b"))
        assertEquals<TypeToken<*>>(erasedOf("a"), erased<String>())
        assertEquals<TypeToken<*>>(erasedOf("a"), generic<String>())
        assertNotEquals<TypeToken<*>>(erasedOf("a"), erasedOf(0))
    }

}
