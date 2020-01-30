package org.kodein.type

import java.lang.reflect.Type

internal abstract class JVMAbstractTypeToken<T> : TypeToken<T>() {

    abstract val jvmType: Type

    override fun simpleDispString() = jvmType.simpleDispString()
    override fun qualifiedDispString() = jvmType.qualifiedDispString()

    final override fun typeEquals(other: TypeToken<*>): Boolean {
        require(other is JVMAbstractTypeToken<*>)
        if (jvmType != other.jvmType) return false
        return true
    }

    final override fun typeHashCode(): Int = jvmType.hashCode()
}
