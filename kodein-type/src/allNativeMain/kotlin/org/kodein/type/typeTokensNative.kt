package org.kodein.type

import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.ObjCProtocol
import kotlinx.cinterop.getOriginalKotlinClass
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

actual fun <T : Any> erasedOf(obj: T): TypeToken<out T> = NativeKClassTypeToken(obj::class)

actual fun <T : Any> erased(cls: KClass<T>): TypeToken<T> = NativeKClassTypeToken(cls)

fun <T> erased(oClass: ObjCClass): TypeToken<T> = NativeKClassTypeToken(getOriginalKotlinClass(oClass) ?: error("$oClass is not a Kotlin class"))
fun <T> erased(oProtocol: ObjCProtocol): TypeToken<T> = NativeKClassTypeToken(getOriginalKotlinClass(oProtocol) ?: error("$oProtocol is not a Kotlin interface"))

@Suppress("UNCHECKED_CAST")
actual inline fun <reified T> erased(): TypeToken<T> = NativeKClassTypeToken(T::class)

/**
 * Function used to get a generic type at runtime.
 *
 * @param T The type to get.
 * @return The type object representing `T`.
 */
@UseExperimental(ExperimentalStdlibApi::class)
@Suppress("UNCHECKED_CAST")
actual inline fun <reified T> generic(): TypeToken<T> = typeToken(typeOf<T>()) as TypeToken<T>

/**
 * Gives a [TypeToken] representing the given type.
 */
fun typeToken(type: KType): TypeToken<*> =
        if (type.arguments.isEmpty()) NativeKClassTypeToken<Any>(type.classifier as KClass<*>)
        else NativeKTypeTypeToken<Any>(type)
