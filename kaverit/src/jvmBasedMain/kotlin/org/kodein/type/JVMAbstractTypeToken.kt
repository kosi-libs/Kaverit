package org.kodein.type

import java.lang.reflect.*


public interface JVMTypeToken<T> : TypeToken<T> {
    public val jvmType: Type
}

internal abstract class JVMAbstractTypeToken<T> : AbstractTypeToken<T>(), JVMTypeToken<T> {

    override fun simpleDispString() = jvmType.simpleDispString()
    override fun qualifiedDispString() = jvmType.qualifiedDispString()

    final override fun typeEquals(other: TypeToken<*>): Boolean {
        require(other is JVMTypeToken<*>)

        return Equals(jvmType, other.jvmType)
    }

    final override fun typeHashCode(): Int = HashCode(jvmType)

    companion object {
        private abstract class WrappingTest<T> {
            val type: Type get() = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        }

        private val needPTWorkaround: Boolean by lazy {
            val t1 = (object : WrappingTest<List<String>>() {}).type as ParameterizedType
            val t2 = (object : WrappingTest<List<String>>() {}).type as ParameterizedType
            t1 != t2
        }

        private val needGATWorkaround: Boolean by lazy {
            val t1 = (object : WrappingTest<Array<List<String>>>() {}).type as GenericArrayType
            val t2 = (object : WrappingTest<Array<List<String>>>() {}).type as GenericArrayType
            t1 != t2
        }

        fun HashCode(type: Type): Int =
            when {
                needPTWorkaround && type is ParameterizedType -> {
                    var hashCode = HashCode(type.rawType)
                    for (arg in type.actualTypeArguments)
                        hashCode = hashCode * 31 + HashCode(arg)
                    hashCode
                }
                needGATWorkaround && type is GenericArrayType -> {
                    53 + HashCode(type.genericComponentType)
                }
                else -> type.hashCode()
            }

        fun Equals(left: Type, right: Type): Boolean {
            if (left.javaClass != right.javaClass)
                return false

            return when {
                needPTWorkaround && left is ParameterizedType -> {
                    right as ParameterizedType
                    Equals(left.rawType, right.rawType) && Equals(left.actualTypeArguments, right.actualTypeArguments)
                }
                needGATWorkaround && left is GenericArrayType -> {
                    right as GenericArrayType
                    Equals(left.genericComponentType, right.genericComponentType)
                }
                else -> left == right
            }
        }

        fun Equals(left: Array<Type>, right: Array<Type>): Boolean {
            if (left.size != right.size)
                return false
            return left.indices.none { !Equals(left[it], right[it]) }
        }


    }
}
