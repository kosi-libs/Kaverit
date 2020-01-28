package org.kodein.type

import java.lang.reflect.Type


/**
 * The true Java `Type` if this is a [KodeinWrappedType], or itself if this is already a true Java `Type`.
 */
internal val Type.javaType: Type get() = (this as? KodeinWrappedType)?.type ?: this

/**
 * The JVM type that is wrapped by a TypeToken.
 *
 * Note that this function may return a [KodeinWrappedType].
 * If you only want Java types, you should call [javaType] on the result.
 */
val TypeToken<*>.jvmType: Type get() =
    when (this) {
        is JVMTypeToken -> jvmType.javaType
        is CompositeTypeToken -> main.jvmType
        else -> throw IllegalStateException("${javaClass.simpleName} is not a JVM Type Token")
    }


internal fun <T> Class<T>.superTypeToken(): TypeToken<in T>? {
    val parent = genericSuperclass ?: return null
    @Suppress("UNCHECKED_CAST")
    return typeToken(parent) as TypeToken<in T>
}
