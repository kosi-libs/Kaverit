package org.kodein.type

import org.junit.Test
import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.WildcardType
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

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

    @Test fun test02_nonreified() {
        fun <T> nonreified() = generic<List<T>>()
        assertFailsWith<IllegalArgumentException> {
            nonreified<String>()
        }
    }

    @Test fun test03_genericArrayType() {
        // Test generic<>()
        assertTrue(generic<Array<List<String>>>() is JVMGenericArrayTypeToken)
        // Test raw Type
        assertEquals(List::class.java, (generic<Array<List<String>>>().getRaw() as JVMClassTypeToken).jvmType)
        assertEquals(List::class.java, (generic<Array<List<String>>>().jvmType as ParameterizedType).rawType)
        // Test generic parameters (when Array<ParameterizedType>)
        assertEquals(String::class.java, generic<Array<List<String>>>().getGenericParameters()[0].jvmType)
        assertEquals(String::class.java, ((generic<Array<List<String>>>().jvmType as ParameterizedType).actualTypeArguments[0] as WildcardType).upperBounds[0])
        // Test typeToken of GenericArrayType -> Java primitive array
        assertEquals(Byte::class.java, typeToken(GenericArrayType { java.lang.Byte.TYPE }).jvmType)
        assertEquals(Int::class.java, typeToken(GenericArrayType { Integer.TYPE }).jvmType)
    }

}
