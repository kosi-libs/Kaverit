package org.kodein.type

import kotlin.reflect.KClass

public expect fun <T: Any> erasedOf(obj: T): TypeToken<out T>

public expect fun <T: Any> erased(cls: KClass<T>): TypeToken<T>

public expect inline fun <reified T : Any> erased(): TypeToken<T>

public expect inline fun <reified T : Any> generic(): TypeToken<T>

public expect fun <T : Any> erasedComp(main: KClass<T>, vararg params: TypeToken<*>): TypeToken<T>
