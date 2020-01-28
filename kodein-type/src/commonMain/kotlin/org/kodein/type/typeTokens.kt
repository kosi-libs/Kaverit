package org.kodein.type

import kotlin.reflect.KClass

expect fun <T: Any> erasedOf(obj: T): TypeToken<out T>

expect fun <T: Any> erased(cls: KClass<T>): TypeToken<T>

expect inline fun <reified T> erased(): TypeToken<T>

expect inline fun <reified T> generic(): TypeToken<T>

fun <T : Any> erasedComp(main: KClass<T>, vararg params: KClass<*>) = CompositeTypeToken(erased(main), *params.map { erased(it) }.toTypedArray())
