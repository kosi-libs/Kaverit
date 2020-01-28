package org.kodein.type

/**
 * A composite type token represents a generic class in an erased manner.
 *
 * For example, the type `Map<String, List<String>>` can be represented as:
 *
 * ```
 * CompositeTypeToken(erased<Map<*, *>>(), erased<String>(), CompositeTypeToken(erased<List<*>(), erased<String>()))
 * ```
 *
 * Note that you should rather use the [erasedComp1], [erasedComp2] or [erasedComp3] functions to create a composite type token.
 *
 * @param T The main type represented by this type token.
 * @property main The main type represented by this type token.
 * @property params The type parameters of the main type.
 */
class CompositeTypeToken<T>(val main: TypeToken<T>, vararg val params: TypeToken<*>) : TypeToken<T>() {

    init {
        if (params.isEmpty())
            throw IllegalStateException("CompositeTypeToken must be given at least one type parameter")
    }

    override fun simpleDispString() = "${main.simpleErasedDispString()}<${params.joinToString(", ") { it.simpleDispString() }}>"

    override fun simpleErasedDispString() = main.simpleErasedDispString()

    override fun qualifiedDispString() = "${main.qualifiedErasedDispString()}<${params.joinToString(", ")  { it.qualifiedDispString() }}>"

    override fun qualifiedErasedDispString() = main.qualifiedErasedDispString()

    override fun getRaw() = main.getRaw()

    override fun isGeneric() = true

    override fun isWildcard() = false

    override fun getSuper() = main.getSuper()

    @Suppress("UNCHECKED_CAST")
    override fun getGenericParameters() = params as Array<TypeToken<*>>

    /** @suppress */
    override fun typeEquals(other: TypeToken<*>): Boolean {
        require(other is CompositeTypeToken<*>)
        return main == other.main && params.contentEquals(other.params)
    }

    /** @suppress */
    override fun typeHashCode() = 31 * main.hashCode() + params.contentHashCode()
}
