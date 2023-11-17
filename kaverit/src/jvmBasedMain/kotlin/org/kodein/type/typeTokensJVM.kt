package org.kodein.type

import java.lang.UnsupportedOperationException
import java.lang.reflect.*
import kotlin.reflect.KClass

public actual fun <T : Any> erasedOf(obj: T): TypeToken<out T> = JVMClassTypeToken(obj.javaClass)

public actual fun <T : Any> erased(cls: KClass<T>): TypeToken<T> = JVMClassTypeToken(cls.javaObjectType)

public actual inline fun <reified T : Any> erased(): TypeToken<T> = erased(T::class)

private val boxes = mapOf(
        Boolean::class.java to java.lang.Boolean::class.java,
        Byte::class.java to java.lang.Byte::class.java,
        Char::class.java to java.lang.Character::class.java,
        Short::class.java to java.lang.Short::class.java,
        Int::class.java to java.lang.Integer::class.java,
        Long::class.java to java.lang.Long::class.java,
        Float::class.java to java.lang.Float::class.java,
        Double::class.java to java.lang.Double::class.java
)

public actual fun <T : Any> erasedComp(main: KClass<T>, vararg params: TypeToken<*>): TypeToken<T> {
    if (main == Array::class) {
        require(params.size == 1) { "Arrays may have only one parameter" }
        @Suppress("UNCHECKED_CAST")
        return if (params[0].isGeneric()) {
            typeToken(GenericArrayTypeImpl(params[0].jvmType)) as TypeToken<T>
        } else {
            val rawComponent = params[0].getRaw().jvmType as? Class<*> ?: error("Could not get raw array component type.")
            typeToken(rawComponent.jvmArrayType()) as TypeToken<T>
        }
    }

    require(main.java.typeParameters.size == params.size) { "Got ${params.size} type parameters, but ${main.java} takes ${main.java.typeParameters.size} parameters." }

    if (params.isEmpty()) return erased(main)

    return JVMParameterizedTypeToken(
            ParameterizedTypeImpl(
                    main.java,
                    params.map {
                        it.jvmType.takeIf { it is Class<*> && it.isPrimitive } ?.let { boxes[it] } ?: it.jvmType
                    } .toTypedArray(),
                    main.java.enclosingClass
            )
    )
}

public fun <T> erased(jCls: Class<T>): TypeToken<T> = JVMClassTypeToken(jCls)

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

@PublishedApi
internal class GenericJVMTypeTokenDelegate<T: Any>(private val typeToken: JVMTypeToken<T>, private val raw: Class<T>) : JVMTypeToken<T> by typeToken {
    override fun getRaw(): TypeToken<T> = JVMClassTypeToken(raw)
    override fun equals(other: Any?): Boolean = typeToken.equals(other)
    override fun hashCode(): Int = typeToken.hashCode()
    override fun toString(): String = typeToken.toString()
}

/**
 * Function used to get a generic type at runtime.
 *
 * @param T The type to get.
 * @return The type object representing `T`.
 */
@Suppress("UNCHECKED_CAST")
public actual inline fun <reified T : Any> generic(): TypeToken<T> = GenericJVMTypeTokenDelegate(typeToken((object : TypeReference<T>() {}).superType) as JVMTypeToken<T>, T::class.java)

private val Type.isReified: Boolean get() =
    when (this) {
        is Class<*> -> true
        is ParameterizedType -> actualTypeArguments.all { it.isReified }
        is GenericArrayType -> genericComponentType.isReified
        is WildcardType -> lowerBounds.all { it.isReified } && upperBounds.all { it.isReified }
        is TypeVariable<*> -> false
        else -> throw IllegalArgumentException("Unknown type $this")
    }

/**
 * Gives a [TypeToken] representing the given type.
 */
public fun typeToken(type: Type): JVMTypeToken<*> =
        when (val k = type.kodein()) {
            is Class<*> -> JVMClassTypeToken(k)
            is ParameterizedType -> JVMParameterizedTypeToken<Any>(k.also { require(it.isReified) { "Cannot create TypeToken for non fully reified type $k" } })
            is GenericArrayType -> {
                val component = typeToken(k.genericComponentType)
                val rawComponent = (component.getRaw().jvmType as Class<*>)
                when {
                    rawComponent.isPrimitive -> JVMClassTypeToken(rawComponent.jvmArrayType())
                    !component.isGeneric() -> JVMClassTypeToken(rawComponent.jvmArrayType())
                    component.isGeneric() && component.isWildcard() -> JVMClassTypeToken((component.getRaw().jvmType as Class<*>).jvmArrayType())
                    else -> JVMGenericArrayTypeToken(k)
                }
            }
            is WildcardType -> typeToken(k.upperBounds[0])
            is TypeVariable<*> -> typeToken(k.firstBound)
            else -> throw UnsupportedOperationException("Unsupported type ${k.javaClass.name}: $k")
        }
