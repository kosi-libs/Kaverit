package org.kodein.type

import kotlin.reflect.KClass

actual fun <T : Any> erasedComp(main: KClass<T>, vararg params: TypeToken<*>): TypeToken<T> =
        if (params.isEmpty()) erased(main)
        else CompositeTypeToken(erased(main), *params)
