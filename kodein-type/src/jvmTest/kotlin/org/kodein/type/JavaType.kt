package org.kodein.type

import org.junit.Test
import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.WildcardType
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class JavaType {

    @Test fun test00_erased() {
        assertEquals<TypeToken<*>>(erased(String::class.java), erased<String>())
        assertEquals<TypeToken<*>>(erased(String::class.java), erased(String::class))
        assertNotEquals<TypeToken<*>>(erased(String::class.java), erased(Int::class.java))
    }

    @ExperimentalStdlibApi
    @Test fun test01_jvmType() {
        assertEquals(String::class.java, erased<String>().jvmType)

        assertTrue(generic<List<String>>().jvmType is ParameterizedType)
        assertEquals(List::class.java, (generic<List<String>>().jvmType as ParameterizedType).rawType)
        assertEquals(String::class.java, ((generic<List<String>>().jvmType as ParameterizedType).actualTypeArguments[0] as WildcardType).upperBounds[0])
    }

}