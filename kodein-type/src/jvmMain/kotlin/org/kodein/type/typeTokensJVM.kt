package org.kodein.type

import java.lang.UnsupportedOperationException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.lang.reflect.WildcardType
import kotlin.reflect.KClass

actual fun <T : Any> erasedOf(obj: T): TypeToken<out T> = JVMClassTypeToken(obj.javaClass)

actual fun <T : Any> erased(cls: KClass<T>): TypeToken<T> = JVMClassTypeToken(cls.java)

actual inline fun <reified T : Any> erased(): TypeToken<T> = erased(T::class)

actual fun <T : Any> erasedComp(main: KClass<T>, vararg params: TypeToken<*>): TypeToken<T> {
    require(main.java.typeParameters.size == params.size) { "Got ${params.size} type parameters, but ${main.java} takes ${main.java.typeParameters.size} parameters." }

    if (params.isEmpty()) return erased(main)

    return JVMParameterizedTypeToken(
            ParameterizedTypeImpl(
                    main.java,
                    params.map { it.jvmType } .toTypedArray(),
                    main.java.enclosingClass
            )
    )
}

fun <T> erased(jCls: Class<T>): TypeToken<T> = JVMClassTypeToken(jCls)

/**
 * Class used to get a generic type at runtime.
 *
 * @param T The type to extract.
 * @see generic
 */
@PublishedApi
internal abstract class TypeReference<T> {

    /**
     * Generic type, unwrapped.
     */
    val superType: Type = (javaClass.genericSuperclass as? ParameterizedType ?: throw RuntimeException("Invalid TypeToken; must specify type parameters")).actualTypeArguments[0]
}

/**
 * Function used to get a generic type at runtime.
 *
 * @param T The type to get.
 * @return The type object representing `T`.
 */
@Suppress("UNCHECKED_CAST")
actual inline fun <reified T : Any> generic(): TypeToken<T> = typeToken((object : TypeReference<T>() {}).superType) as TypeToken<T>

/**
 * Gives a [TypeToken] representing the given type.
 */
fun typeToken(type: Type): TypeToken<*> =
        when (val k = type.kodein()) {
            is Class<*> -> JVMClassTypeToken(k)
            is ParameterizedType -> JVMParameterizedTypeToken<Any>(k)
            is WildcardType -> typeToken(k.upperBounds[0])
            is TypeVariable<*> -> typeToken(k.firstBound)
            else -> throw UnsupportedOperationException("Unsupported type ${k.javaClass.name}: $k")
        }
