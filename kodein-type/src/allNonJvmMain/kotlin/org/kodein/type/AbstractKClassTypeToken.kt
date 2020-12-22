package org.kodein.type

import kotlin.reflect.KClass

internal abstract class AbstractKClassTypeToken<T>(protected val type: KClass<*>): TypeToken<T>() {

    override fun simpleDispString(): String = simpleErasedDispString()

    override fun qualifiedDispString() = qualifiedErasedDispString()

    override fun getRaw(): TypeToken<T> = this
    override fun isGeneric(): Boolean = false
    override fun isWildcard(): Boolean = true

    override fun getSuper(): List<TypeToken<*>> = emptyList()
    override fun getGenericParameters(): Array<TypeToken<*>> = emptyArray()

    override fun isAssignableFrom(typeToken: TypeToken<*>): Boolean {
        if (this == typeToken)
            return true
        if (type == kotlin.Any::class)
            return true
        return false
    }

    override fun typeEquals(other: TypeToken<*>): Boolean {
        require(other is AbstractKClassTypeToken<*>)
        if (type != other.type) return false
        return true
    }

    override fun typeHashCode(): Int = type.hashCode()
}
