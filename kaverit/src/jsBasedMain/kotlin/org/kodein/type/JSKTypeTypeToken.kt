package org.kodein.type

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection

internal class JSKTypeTypeToken<T>(private val type: KType) : AbstractTypeToken<T>() {

    override fun simpleDispString() = type.toString().stripPackages()

    override fun simpleErasedDispString() = (type.classifier as? KClass<*>)?.simpleName ?: "(non-denotable type)"

    override fun qualifiedDispString() = type.toString()

    override fun qualifiedErasedDispString() = (type.classifier as? KClass<*>)?.simpleName ?: "(non-denotable type)"

    override fun getRaw(): TypeToken<T> = JSKClassTypeToken(type.classifier as KClass<*>)

    override fun isGeneric() = true

    override fun getGenericParameters(): Array<TypeToken<*>> =
            type.arguments.map { arg -> arg.type?.let { typeToken(it) } ?: Any } .toTypedArray()

    override fun isWildcard(): Boolean = type.arguments.all { it == KTypeProjection.STAR }

    override fun getSuper(): List<TypeToken<*>> = emptyList()

    override fun typeEquals(other: TypeToken<*>): Boolean {
        require(other is JSKTypeTypeToken<*>)
        return type == other.type
    }

    override fun typeHashCode(): Int = type.hashCode()
}

private val packageQualifiedNameRegex = Regex("""\b[a-z][\w.]*\.([A-Z]\w*)""")

internal fun String.stripPackages(): String =
    replace(packageQualifiedNameRegex) { it.groupValues[1] }
