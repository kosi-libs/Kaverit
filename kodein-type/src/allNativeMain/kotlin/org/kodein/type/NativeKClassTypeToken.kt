package org.kodein.type

import kotlin.reflect.KClass

internal class NativeKClassTypeToken<T>(type: KClass<*>) : AbstractKClassTypeToken<T>(type) {

    override fun simpleErasedDispString(): String = type.simpleName ?: "{Unknown}"
    override fun qualifiedErasedDispString() = type.qualifiedName ?: "{Unknown}"

}
