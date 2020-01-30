package org.kodein.type

import java.lang.reflect.*

private abstract class TypeStringer {

    fun dispString(type: Type, skipStars: Boolean = false): String {
        return when (type) {
            is Class<*> -> dispName(type, skipStars)
            is ParameterizedType -> {
                val arguments = type.rawClass.typeParameters.mapIndexed { i, variable ->
                    val argument = type.actualTypeArguments[i]
                    if (argument is WildcardType && variable.bounds.any { it in argument.upperBounds })
                        "*"
                    else
                        dispString(argument)
                }
                dispString(type.rawClass, true) + "<" + arguments.joinToString(", ") + ">"
            }
            is WildcardType -> when {
                type.lowerBounds.isNotEmpty() -> "in " + dispString(type.lowerBounds[0])
                type.upperBounds.isNotEmpty() -> when(type.upperBounds[0]) {
                    Any::class.java -> "*"
                    else -> "out " + dispString(type.upperBounds[0])
                }
                else -> "*"
            }
            is GenericArrayType -> "Array<" + dispString(type.genericComponentType) + ">"
            is TypeVariable<*> -> type.name
            else -> throw IllegalStateException("Unknown type $javaClass")
        }
    }

    abstract fun dispName(cls: Class<*>, skipStars: Boolean = false): String
}

private object SimpleTypeStringer : TypeStringer() {
    override fun dispName(cls: Class<*>, skipStars: Boolean): String = when {
        cls.isArray -> "Array<" + dispString(cls.componentType) + ">"
        else -> cls.primitiveName ?: cls.simpleErasedName() + (if (!skipStars) cls.stars else "")
    }
}

private object QualifiedTypeStringer : TypeStringer() {
    override fun dispName(cls: Class<*>, skipStars: Boolean) = when {
        cls.isArray -> "kotlin.Array<" + dispString(cls.componentType) + ">"
        else -> cls.primitiveName?.let { "kotlin.$it" }
                ?: ((cls.`package`?.name?.plus(".") ?: "") + SimpleTypeStringer.dispName(cls, true)).magic() + (if (!skipStars) cls.stars else "")
    }
}

private val Class<*>.stars: String get() {
    if (typeParameters.isEmpty())
        return ""

    return Array(typeParameters.size) { "*" }.joinToString(prefix = "<", separator = ", ", postfix = ">")
}

private val Class<*>.primitiveName: String?
    get() = when (this) {
        Boolean::class.javaPrimitiveType, Boolean::class.javaObjectType -> "Boolean"
        Byte::class.javaPrimitiveType, Byte::class.javaObjectType -> "Byte"
        Char::class.javaPrimitiveType, Char::class.javaObjectType -> "Char"
        Short::class.javaPrimitiveType, Short::class.javaObjectType -> "Short"
        Int::class.javaPrimitiveType, Int::class.javaObjectType -> "Int"
        Long::class.javaPrimitiveType, Long::class.javaObjectType -> "Long"
        Float::class.javaPrimitiveType, Float::class.javaObjectType -> "Float"
        Double::class.javaPrimitiveType, Double::class.javaObjectType -> "Double"
        Object::class.java -> "Any"
        else -> null
    }

private fun String.magic(): String = when {
    startsWith("java.") -> when (this) {
        "java.util.List" -> "kotlin.collections.List"
        "java.util.ArrayList" -> "kotlin.collections.ArrayList"

        "java.util.Map" -> "kotlin.collections.Map"
        "java.util.LinkedHashMap" -> "kotlin.collections.LinkedHashMap"
        "java.util.HashMap" -> "kotlin.collections.HashMap"

        "java.util.Set" -> "kotlin.collections.Set"
        "java.util.HashSet" -> "kotlin.collections.HashSet"
        "java.util.LinkedHashSet" -> "kotlin.collections.LinkedHashSet"

        "java.lang.String" -> "kotlin.String"
        "java.lang.Object" -> "kotlin.Any"
        "java.lang.Error" -> "kotlin.Error"
        "java.lang.Throwable" -> "kotlin.Throwable"
        "java.lang.Exception" -> "kotlin.Exception"
        "java.lang.RuntimeException" -> "kotlin.RuntimeException"
        "java.lang.IllegalArgumentException" -> "kotlin.IllegalArgumentException"
        "java.lang.IllegalStateException" -> "kotlin.IllegalStateException"
        "java.lang.IndexOutOfBoundsException" -> "kotlin.IndexOutOfBoundsException"
        "java.lang.UnsupportedOperationException" -> "kotlin.UnsupportedOperationException"
        "java.lang.NumberFormatException" -> "kotlin.NumberFormatException"
        "java.lang.NullPointerException" -> "kotlin.NullPointerException"
        "java.lang.ClassCastException" -> "kotlin.ClassCastException"
        "java.lang.AssertionError" -> "kotlin.AssertionError"
        "java.util.NoSuchElementException" -> "kotlin.NoSuchElementException"

        "java.util.Comparator" -> "kotlin.Comparator"
        else -> this
    }
    else -> this
}

/**
 * A string representing this type in a Kotlin-esque fashion using simple type names.
 */
fun Type.simpleDispString(): String = SimpleTypeStringer.dispString(this)

/**
 * A string representing this type in a Kotlin-esque fashion using full type names.
 */
fun Type.qualifiedDispString(): String = QualifiedTypeStringer.dispString(this)

/**
 * Returns the erased name of a type (e.g. the type name without it's generic parameters).
 */
fun Type.simpleErasedName(): String {
    return when (this) {
        is Class<*> -> (enclosingClass?.simpleErasedName()?.plus(".") ?: "") + simpleName
        is ParameterizedType -> rawClass.simpleErasedName()
        is GenericArrayType -> genericComponentType.simpleErasedName()
        is WildcardType -> "*"
        is TypeVariable<*> -> name
        else -> throw IllegalArgumentException("Unknown type $javaClass $this")
    }
}

/**
 * Returns the fully qualified erased name of a type (e.g. the type name without it's generic parameters).
 */
fun Type.qualifiedErasedName(): String {
    return when (this) {
        is Class<*> -> canonicalName.magic()
        is ParameterizedType -> rawClass.qualifiedErasedName()
        is GenericArrayType -> genericComponentType.qualifiedErasedName()
        is WildcardType -> "*"
        is TypeVariable<*> -> name
        else -> throw IllegalArgumentException("Unknown type $javaClass $this")
    }
}