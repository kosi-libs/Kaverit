package org.kodein.type

import java.lang.reflect.*

internal class JVMParameterizedTypeToken<T>(val trueType: Type) : JVMTypeToken<T>() {
    private var _type: Type? = null

    override fun simpleErasedDispString() = trueType.simpleErasedName()

    override fun qualifiedErasedDispString() = trueType.qualifiedErasedName()

    private val rawType: Class<*>? get() =
        when (trueType) {
            is Class<*> -> trueType
            is ParameterizedType -> trueType.rawType as Class<*>
            else -> null
        }

    override fun getGenericParameters(): Array<TypeToken<*>> {
        val type = _type as? ParameterizedType
                ?: return rawType?.typeParameters?.map { typeToken(it.bounds[0]) } ?.toTypedArray() ?: emptyArray()
        return type.actualTypeArguments.map {
            if (it is WildcardType)
                typeToken(it.upperBounds[0])
            else
                typeToken(it)
        }.toTypedArray()
    }

    override val jvmType: Type = _type ?: run {
        // TypeReference cannot create WildcardTypes nor TypeVariables
        when {
            KodeinWrappedType.IsNeeded.forParameterizedType.not() && KodeinWrappedType.IsNeeded.forGenericArray.not() -> trueType
            trueType is Class<*> -> trueType
            KodeinWrappedType.IsNeeded.forParameterizedType && trueType is ParameterizedType -> KodeinWrappedType(trueType)
            KodeinWrappedType.IsNeeded.forGenericArray && trueType is GenericArrayType -> KodeinWrappedType(trueType)
            else -> trueType
        }.also { _type = it }
    }

    @Suppress("UNCHECKED_CAST")
    override fun getRaw(): TypeToken<T>? {
        return rawType?.let { JVMClassTypeToken(it as Class<T>) }
    }

    override fun isGeneric() = true

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

    @Suppress("UNCHECKED_CAST")
    override fun getSuper(): List<TypeToken<*>> {
        val extends = when (val jvmType = jvmType) {
            is ParameterizedType -> (jvmType.rawType as Class<T>).superTypeToken()
            else -> null
        }
        val implements = rawType?.genericInterfaces?.map { typeToken(it) } ?: emptyList()
        return (extends?.let { listOf(it) } ?: emptyList()) + implements
    }
}