package org.kodein.type

import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.ObjCProtocol
import kotlinx.cinterop.getOriginalKotlinClass
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

public actual fun <T : Any> erasedOf(obj: T): TypeToken<out T> = NativeKClassTypeToken(obj::class)

public actual fun <T : Any> erased(cls: KClass<T>): TypeToken<T> = NativeKClassTypeToken(cls)

@Suppress("unused")
public fun <T> erased(oClass: ObjCClass): TypeToken<T> = NativeKClassTypeToken(getOriginalKotlinClass(oClass) ?: error("$oClass is not a Kotlin class"))
@Suppress("unused")
public fun <T> erased(oProtocol: ObjCProtocol): TypeToken<T> = NativeKClassTypeToken(getOriginalKotlinClass(oProtocol) ?: error("$oProtocol is not a Kotlin interface"))

public actual inline fun <reified T : Any> erased(): TypeToken<T> = erased(T::class)

/**
 * Function used to get a generic type at runtime.
 *
 * @param T The type to get.
 * @return The type object representing `T`.
 */
@OptIn(ExperimentalStdlibApi::class)
@Suppress("UNCHECKED_CAST")
public actual inline fun <reified T : Any> generic(): TypeToken<T> = typeToken(typeOf<T>()) as TypeToken<T>

/**
 * Gives a [TypeToken] representing the given type.
 */
@Suppress("RemoveExplicitTypeArguments")
public fun typeToken(type: KType): TypeToken<*> =
        if (type.arguments.isEmpty()) NativeKClassTypeToken<Any>(type.classifier as KClass<*>)
        else NativeKTypeTypeToken<Any>(type)
