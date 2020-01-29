package org.kodein.type

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

actual fun <T : Any> erasedOf(obj: T): TypeToken<out T> = JSKClassTypeToken(obj::class)

actual fun <T : Any> erased(cls: KClass<T>): TypeToken<T> = JSKClassTypeToken(cls)

actual inline fun <reified T : Any> erased(): TypeToken<T> = erased(T::class)

/**
 * Function used to get a generic type at runtime.
 *
 * @param T The type to get.
 * @return The type object representing `T`.
 */
@UseExperimental(ExperimentalStdlibApi::class)
@Suppress("UNCHECKED_CAST")
actual inline fun <reified T : Any> generic(): TypeToken<T> = typeToken(typeOf<T>()) as TypeToken<T>

/**
 * Gives a [TypeToken] representing the given type.
 */
@Suppress("RemoveExplicitTypeArguments")
fun typeToken(type: KType): TypeToken<*> =
        if (type.arguments.isEmpty()) JSKClassTypeToken<Any>(type.classifier as KClass<*>)
        else JSKTypeTypeToken<Any>(type)
