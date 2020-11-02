package org.kodein.type

import java.lang.reflect.*


/**
 * The JVM type that is wrapped by a TypeToken.
 */
public val TypeToken<*>.jvmType: Type get() =
    when (this) {
        is JVMAbstractTypeToken -> jvmType
        else -> throw IllegalStateException("${javaClass.simpleName} is not a JVM Type Token")
    }

internal val ParameterizedType.rawClass get() = rawType as Class<*>

internal fun ParameterizedType.reify(parent: Type): Type {
    if (parent !is ParameterizedType) return parent

    return ParameterizedTypeImpl(
            parent.rawClass,
            parent.actualTypeArguments.map { arg ->
                (arg as? TypeVariable<*>)?.run {
                    rawClass.typeParameters.indexOf(arg).takeIf { it >= 0 } ?.let { actualTypeArguments[it].kodein() }
                } ?: arg.kodein()
            } .toTypedArray(),
            parent.ownerType.kodein()
    )
}

internal fun Type.removeVariables(): Type {
    if (this !is ParameterizedType) return this
    return ParameterizedTypeImpl(
            rawClass,
            actualTypeArguments.map { arg ->
                if (arg is TypeVariable<*>) Any::class.java
                else arg.kodein()
            } .toTypedArray(),
            ownerType.kodein()
    )
}

internal val TypeVariable<*>.firstBound: Type get() =
        (bounds[0] as? TypeVariable<*>)?.firstBound ?: bounds[0]

internal val Class<*>.boundedGenericSuperClass: Type? get() {
    val parent = genericSuperclass ?: return superclass
    if (parent !is ParameterizedType) return parent
    return ParameterizedTypeImpl(
            parent.rawClass,
            parent.actualTypeArguments.map { (it as? TypeVariable<*>)?.firstBound ?: it } .toTypedArray(),
            parent.ownerType
    )
}

internal fun Type.typeHashCode(): Int = when(this) {
    is Class<*> -> this.hashCode()
    is ParameterizedType -> actualTypeArguments.fold(rawClass.typeHashCode()) { hash, arg -> hash * 31 + arg.typeHashCode() }
    is WildcardType -> (this.upperBounds + this.lowerBounds).fold(17) { hash, arg -> hash * 19 + arg.typeHashCode() }
    is GenericArrayType -> 53 + this.genericComponentType.typeHashCode()
    is TypeVariable<*> -> bounds.fold(23) { hash, arg -> hash * 29 + arg.typeHashCode() }
    else -> this.hashCode()
}

internal fun Type.typeEquals(other: Type): Boolean {
    return when (this) {
        is Class<*> -> this == other
        is ParameterizedType -> {
            if (other !is ParameterizedType) return false
            rawClass.typeEquals(other.rawClass) && (
                    actualTypeArguments.allTypeEquals(other.actualTypeArguments)
                ||  boundedTypeArguments().allTypeEquals(other.boundedTypeArguments())
            )
        }
        is WildcardType -> {
            if (other !is WildcardType) return false
            lowerBounds.allTypeEquals(other.lowerBounds) && upperBounds.allTypeEquals(other.upperBounds)
        }
        is GenericArrayType -> {
            if (other !is GenericArrayType) return false
            genericComponentType.typeEquals(other.genericComponentType)
        }
        is TypeVariable<*> -> {
            if (other !is TypeVariable<*>) return false
            bounds.allTypeEquals(other.bounds)
        }
        else -> this == other
    }
}

private fun ParameterizedType.boundedTypeArguments() =
        actualTypeArguments.map { if (it is WildcardType) it.upperBounds.firstOrNull() ?: Any::class.java else it } .toTypedArray()

private fun Array<Type>.allTypeEquals(other: Array<Type>): Boolean {
    if (size != other.size) return false
    return indices.all { this[it].typeEquals(other[it]) }
}

@Suppress("UNCHECKED_CAST")
@PublishedApi
internal fun <T : Type?> T.kodein(): T = when (this) {
    is ParameterizedType -> ParameterizedTypeImpl(this)
    is GenericArrayType -> GenericArrayTypeImpl(this)
    else -> this
} as T
