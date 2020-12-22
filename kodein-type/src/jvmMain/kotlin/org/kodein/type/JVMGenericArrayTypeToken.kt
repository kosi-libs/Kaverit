package org.kodein.type

import java.lang.reflect.*

internal class JVMGenericArrayTypeToken<T>(override val jvmType: GenericArrayType) : JVMAbstractTypeToken<T>() {

    override fun simpleErasedDispString(): String = jvmType.simpleErasedName()

    override fun qualifiedErasedDispString(): String = jvmType.qualifiedErasedName()

    override fun getRaw(): TypeToken<T> {
        val rawComponent = typeToken(jvmType.genericComponentType).getRaw().jvmType as? Class<*> ?: error("Could not get raw array component type.")
        @Suppress("UNCHECKED_CAST")
        return typeToken(rawComponent.arrayType()) as TypeToken<T>
    }

    override fun isGeneric(): Boolean = true

    override fun getGenericParameters(): Array<TypeToken<*>> = arrayOf(typeToken(jvmType.genericComponentType))

    override fun isWildcard(): Boolean = jvmType.genericComponentType == Object::class.java || jvmType.genericComponentType is WildcardType

    override fun getSuper(): List<TypeToken<*>> = emptyList()
}
