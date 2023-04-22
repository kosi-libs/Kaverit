package org.kodein.type

import kotlin.reflect.KClass

internal class JSKClassTypeToken<T>(type: KClass<*>) : AbstractKClassTypeToken<T>(type) {

    override fun simpleErasedDispString(): String = type.simpleName ?: "(non-denotable type)"
    override fun qualifiedErasedDispString() = type.simpleName ?: "(non-denotable type)"

}
