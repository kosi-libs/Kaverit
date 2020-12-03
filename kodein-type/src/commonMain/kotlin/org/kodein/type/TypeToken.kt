package org.kodein.type

/**
 * An interface that contains a simple Type but is parameterized to enable type safety.
 *
 * @param T The type represented by this object.
 */
public abstract class TypeToken<T> {

    /**
     * @return The simple (a.k.a. not fully qualified) name of the type represented by this TypeToken.
     */
    public abstract fun simpleDispString(): String

    /**
     * @return The simple (a.k.a. not fully qualified) erased (a.k.a without generic parameters) name of the type represented by this TypeToken.
     */
    public abstract fun simpleErasedDispString(): String

    /**
     * @return The fully qualified name of the type represented by this TypeToken.
     */
    public abstract fun qualifiedDispString(): String

    /**
     * @return The fully qualified erased (a.k.a without generic parameters) name of the type represented by this TypeToken.
     */
    public abstract fun qualifiedErasedDispString(): String

    /**
     * @return the raw type represented by this type.
     *   If this type is not generic, than it's raw type is itself.
     */
    public abstract fun getRaw(): TypeToken<T>

    /**
     * @return Whether the type represented by this TypeToken is generic.
     */
    public abstract fun isGeneric(): Boolean

    /**
     * @return A list of generic parameters (empty if this types does not have generic parameters).
     */
    public abstract fun getGenericParameters(): Array<TypeToken<*>>

    /**
     * Returns whether the type represented by this TypeToken is generic and is entirely wildcard.
     *
     * Examples:
     *
     * - `List<*>`: true
     * - `List<String>`: false
     * - `Map<*, *>`: true
     * - `Map<String, *>`: false
     * - `Map<*, String>`: false
     * - `Map<String, String>`: very false!
     *
     * @return Whether the type represented by this TypeToken is generic and is entirely wildcard, otherwise null.
     */
    public abstract fun isWildcard(): Boolean

    /**
     * Returns the parent type of the type represented by this TypeToken, if any.
     */
    public abstract fun getSuper(): List<TypeToken<*>>

    /**
     * Determines if the type represented by this type object is either the same as, or is a superclass or superinterface of, the type represented by the specified type parameter.
     */
    public open fun isAssignableFrom(typeToken: TypeToken<*>): Boolean {
        if (this == typeToken || this == Any)
            return true

        val raw = getRaw()
        if (raw == typeToken.getRaw()) {
            val thisParams = getGenericParameters()
            if (thisParams.isEmpty())
                return true
            val tokenParams = typeToken.getGenericParameters()
            thisParams.forEachIndexed { index, thisParam ->
                val tokenParam = tokenParams[index]
                if (!thisParam.isAssignableFrom(tokenParam))
                    return false
            }
            return true
        }

        return typeToken.getSuper().any { isAssignableFrom(it) }
    }

    final override fun toString(): String = qualifiedDispString()

    internal abstract fun typeEquals(other: TypeToken<*>): Boolean

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TypeToken<*>) return false
        if (this::class == other::class) return typeEquals(other)

        if (this.getRaw() != other.getRaw()) return false
        if (this.isWildcard().not() || other.isWildcard().not()) {
            val tParams = this.getGenericParameters()
            val oParams = other.getGenericParameters()
            if (tParams.size != oParams.size) return false
            for (i in tParams.indices) {
                if (tParams[i] != oParams[i]) return false
            }
        }

        return true
    }

    final override fun hashCode(): Int = typeHashCode()

    internal abstract fun typeHashCode(): Int

    public companion object {
        public val Unit: TypeToken<Unit> = erased<Unit>()
        public val Any: TypeToken<Any> = erased<Any>()
    }

}
