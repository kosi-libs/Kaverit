package org.kodein.type

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass

actual fun <T : Any> erasedOf(obj: T): TypeToken<out T> = JVMClassTypeToken(obj.javaClass)

actual fun <T : Any> erased(cls: KClass<T>): TypeToken<T> = JVMClassTypeToken(cls.java)

fun <T> erased(jCls: Class<T>): TypeToken<T> = JVMClassTypeToken(jCls)

@Suppress("UNCHECKED_CAST")
actual inline fun <reified T> erased(): TypeToken<T> = JVMClassTypeToken(T::class.java)

/**
 * Class used to get a generic type at runtime.
 *
 * @param T The type to extract.
 * @see generic
 */
@PublishedApi
internal abstract class TypeReference<T> {

    /**
     * Generic type, unwrapped.
     */
    val superType: Type = (javaClass.genericSuperclass as? ParameterizedType ?: throw RuntimeException("Invalid TypeToken; must specify type parameters")).actualTypeArguments[0]
}

/**
 * Function used to get a generic type at runtime.
 *
 * @param T The type to get.
 * @return The type object representing `T`.
 */
@Suppress("UNCHECKED_CAST")
actual inline fun <reified T> generic(): TypeToken<T> = typeToken((object : TypeReference<T>() {}).superType) as TypeToken<T>

/**
 * Gives a [TypeToken] representing the given type.
 */
fun typeToken(type: Type): TypeToken<*> =
        if (type is Class<*>) JVMClassTypeToken(type)
        else JVMParameterizedTypeToken<Any>(type)
