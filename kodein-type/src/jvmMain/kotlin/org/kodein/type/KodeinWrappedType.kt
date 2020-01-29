package org.kodein.type

import java.lang.reflect.*

/**
 * Wraps a ParameterizedType and implements hashCode / equals.
 *
 * This is because some JVM implementations (such as Android 4.4 and earlier) do NOT implement hashcode / equals for
 * ParameterizedType (I know...).
 *
 * @property type The type object to wrap.
 */
internal class KodeinWrappedType(val type: Type) : Type {

    @Suppress("unused")
    private abstract class WrappingTest<T> {
        val type: Type get() = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
    }

    internal object IsNeeded {
        val forParameterizedType: Boolean by lazy {
            val t1 = (object : WrappingTest<List<String>>() {}).type as ParameterizedType
            val t2 = (object : WrappingTest<List<String>>() {}).type as ParameterizedType
            t1 != t2
        }

        val forGenericArray: Boolean by lazy {
            val t1 = (object : WrappingTest<Array<List<String>>>() {}).type as GenericArrayType
            val t2 = (object : WrappingTest<Array<List<String>>>() {}).type as GenericArrayType
            t1 != t2
        }
    }

    private var hashCode: Int = 0

    /** @suppress */
    override fun hashCode(): Int {
        if (hashCode == 0)
            hashCode = Func.hashCode(type)
        return hashCode
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Type)
            return false

        if (hashCode() != other.hashCode())
            return false

        return Func.equals(type, other)
    }

    /** @suppress */
    override fun toString(): String {
        return "KodeinWrappedType{$type}"
    }

    private object Func {

        fun hashCode(type: Type): Int = when(type) {
            is Class<*> -> type.hashCode()
            is ParameterizedType -> {
                var hashCode = hashCode(type.rawType)
                for (arg in type.actualTypeArguments)
                    hashCode = hashCode * 31 + hashCode(arg)
                hashCode
            }
            is WildcardType -> {
                var hashCode = 0
                for (arg in type.upperBounds)
                    hashCode = hashCode * 19 + hashCode(arg)
                for (arg in type.lowerBounds)
                    hashCode = hashCode * 17 + hashCode(arg)
                hashCode
            }
            is GenericArrayType -> 53 + hashCode(type.genericComponentType)
            is TypeVariable<*> -> {
                var hashCode = 0
                for (arg in type.bounds)
                    hashCode = hashCode * 29 + hashCode(arg)
                hashCode
            }
            else -> type.hashCode()
        }

        fun equals(l: Type, r: Type): Boolean {
            val left = l.javaType
            val right = r.javaType

            if (left.javaClass != right.javaClass)
                return false

            return when (left) {
                is Class<*> -> left == right
                is ParameterizedType -> {
                    right as ParameterizedType
                    equals(left.rawType, right.rawType) && equals(left.actualTypeArguments, right.actualTypeArguments)
                }
                is WildcardType -> {
                    right as WildcardType
                    equals(left.lowerBounds, right.lowerBounds) && equals(left.upperBounds, right.upperBounds)
                }
                is GenericArrayType -> {
                    right as GenericArrayType
                    equals(left.genericComponentType, right.genericComponentType)
                }
                is TypeVariable<*> -> {
                    right as TypeVariable<*>
                    equals(left.bounds, right.bounds)
                }
                else -> left == right
            }
        }

        fun equals(left: Array<Type>, right: Array<Type>): Boolean {
            if (left.size != right.size)
                return false
            return left.indices.none { !equals(left[it], right[it]) }
        }
    }
}