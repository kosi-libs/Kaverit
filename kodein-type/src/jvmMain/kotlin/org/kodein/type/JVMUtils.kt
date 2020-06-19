package org.kodein.type

import java.lang.reflect.*


/**
 * The JVM type that is wrapped by a TypeToken.
 */
val TypeToken<*>.jvmType: Type get() =
    when (this) {
        is JVMAbstractTypeToken -> jvmType
        else -> throw IllegalStateException("${javaClass.simpleName} is not a JVM Type Token")
    }

internal val ParameterizedType.rawClass get() = rawType as Class<*>

@OptIn(ExperimentalStdlibApi::class)
internal fun ParameterizedType.reify(parent: Type, originType: ParameterizedType? = null, realTypeArguments: Array<Type>? = null): Type {
    if (parent !is ParameterizedType) return parent

    /* Keep the reference to the encapsulating type
        e.g. for Set<Map<Int, String>>
        _originType -> Set<? extends T>
        _originRawClass -> java.util.Set
        _realTypeArguments -> [Int, String]
     */
    val _originType = originType ?: this
    val _originRawClass = originType?.rawClass ?: rawClass
    val _realTypeArguments = realTypeArguments ?: actualTypeArguments

    return ParameterizedTypeImpl(
            parent.rawClass,
            buildList {
                parent.actualTypeArguments.forEach { arg ->
                    when (arg) {
                        /* In case the type argument is TypeVariable
                            e.g. T, E, V, K...
                         */
                        is TypeVariable<*> -> {
                            _originRawClass.typeParameters.indexOf(arg).takeIf { it >= 0 }?.let {
                                add((_realTypeArguments)[it].kodein())
                            }
                        }
                        /* In case the type argument is WildcardType
                            and first upperBounds extends a ParameterizedType or TypeVariable
                            e.g. ? extends Set, ? extends Map, ? extends T, ? extends V...
                         */
                        is WildcardType -> {
                            arg.upperBounds[0]?.also { upperBound ->
                                when (upperBound) {
                                    is ParameterizedType -> {
                                        add(upperBound.reify(upperBound, _originType, _realTypeArguments))
                                    }
                                    is TypeVariable<*> -> {
                                        _originRawClass.typeParameters.indexOf(upperBound)
                                            .takeIf { it >= 0 }?.let {
                                                add((_realTypeArguments)[it].kodein())
                                            }
                                    }
                                }
                            }
                        }
                        /* In case the type argument is ParameterizedType
                            e.g. Set<? extends E>
                         */
                        is ParameterizedType -> add(arg.reify(arg, _originType, _realTypeArguments))
                        else -> add(arg.kodein())
                    }
                }
            } .toTypedArray(),
            parent.ownerType.kodein()
    )
}

//@OptIn(ExperimentalStdlibApi::class)
//private fun Array<Type>.flatMapArgs(rawClass: Class<*>, actualTypeArguments: Array<Type>): Array<Type> {
//    val l = mutableListOf<Type>()
//
//    forEach { arg ->
//        when(arg) {
//            is TypeVariable<*> -> arg.run {
//                rawClass.typeParameters.indexOf(arg).takeIf { it >= 0 }
//                        ?.let { l.add(actualTypeArguments[it].kodein()) }
//            }
//            is WildcardType ->  {
//                if (arg.upperBounds[0] is ParameterizedType)
//                    l.add((arg.upperBounds[0] as ParameterizedType).reify(arg.upperBounds[0],actualTypeArguments))
//                else
//                    l.add(actualTypeArguments[0].kodein())
//            }
//            is ParameterizedType -> l.add(arg.reify(arg, actualTypeArguments))
//            else -> l.add(arg.kodein())
//        }
//    }
//
//    return l.toTypedArray()
//}


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
    else -> this
} as T
