package org.kodein.type

import java.lang.reflect.*

internal class JVMGenericArrayTypeToken<T>(override val jvmType: GenericArrayType) : JVMAbstractTypeToken<T>() {

    override fun simpleErasedDispString(): String = jvmType.simpleErasedName()

    override fun qualifiedErasedDispString(): String = jvmType.qualifiedErasedName()

    override fun getRaw(): TypeToken<T> {
        val rawComponent = typeToken(jvmType.genericComponentType).getRaw().jvmType as? Class<*> ?: error("Could not get raw array component type.")
        val descriptor = "[L${rawComponent.name};"
        @Suppress("UNCHECKED_CAST")
        return typeToken(Class.forName(descriptor)) as TypeToken<T>
    }

    override fun isGeneric(): Boolean = true

    override fun getGenericParameters(): Array<TypeToken<*>> = arrayOf(typeToken(jvmType.genericComponentType))

    override fun isWildcard(): Boolean = getGenericParameters()[0].isWildcard()

    override fun getSuper(): List<TypeToken<*>> = emptyList()
}
