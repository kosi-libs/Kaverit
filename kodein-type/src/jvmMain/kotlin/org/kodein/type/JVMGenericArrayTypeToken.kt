package org.kodein.type

import java.lang.reflect.*

internal class JVMGenericArrayTypeToken<T>(private val trueType: GenericArrayType) : JVMAbstractTypeToken<T>() {
    override val jvmType: Type = trueType.genericComponentType

    private val rawType: Class<*>?
        get() = when (jvmType) {
            is Class<*> -> jvmType
            is ParameterizedType -> jvmType.rawClass
            else -> null
        }

    override fun simpleErasedDispString() = trueType.simpleErasedName()
    override fun qualifiedErasedDispString() = trueType.qualifiedErasedName()

    @Suppress("UNCHECKED_CAST")
    override fun getRaw(): TypeToken<T>? = rawType?.let { JVMClassTypeToken(it as Class<T>) }

    override fun isGeneric() = true

    override fun getGenericParameters(): Array<TypeToken<*>> =
            when (jvmType) {
                is Class<*> -> jvmType.typeParameters?.map { typeToken(it.bounds[0]) }?.toTypedArray() ?: emptyArray()
                is ParameterizedType -> jvmType.actualTypeArguments.map { typeToken(it) }.toTypedArray()
                else -> emptyArray()
            }

    @Suppress("UNCHECKED_CAST")
    override fun isWildcard(): Boolean {
        val realType = trueType as? ParameterizedType ?: return false

        var hasWildCard = false
        var hasSpecific = false

        val cls = realType.rawType as Class<*>
        cls.typeParameters.forEachIndexed { i, variable ->
            val argument = realType.actualTypeArguments[i]

            if (argument is WildcardType && variable.bounds.any { it in argument.upperBounds })
                hasWildCard = true
            else
                hasSpecific = true
        }

        return hasWildCard && !hasSpecific
    }

    override fun getSuper(): List<TypeToken<*>> {
        val extends = when (val jvmType = jvmType) {
            is ParameterizedType -> JVMParameterizedTypeToken<Any>(jvmType)
            else -> null
        }
        val implements = rawType?.genericInterfaces?.map { typeToken(it) } ?: emptyList()
        return (extends?.let { listOf(it) } ?: emptyList()) + implements
    }
}