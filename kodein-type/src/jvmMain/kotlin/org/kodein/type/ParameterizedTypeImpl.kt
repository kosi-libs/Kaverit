package org.kodein.type

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType

internal class ParameterizedTypeImpl(private val rawType: Class<*>, private val args: Array<Type>, private val ownerType: Type?) : ParameterizedType {

    override fun getRawType() = rawType
    override fun getActualTypeArguments(): Array<Type> = args
    override fun getOwnerType(): Type? = ownerType

    /** @suppress */
    override fun hashCode() = typeHashCode()

    /** @suppress */
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Type)
            return false

        if (typeEquals(other)) return true

        if (other !is ParameterizedType) return false

        return bind(this).typeEquals(bind(other))
    }

    /** @suppress */
    override fun toString() = buildString {
        if (ownerType != null) {
            append(ownerType.typeName)
            append("$")
            append(
                    if (ownerType is ParameterizedType) rawType.name.replace((ownerType.rawType as Class<*>).name + "$", "")
                    else rawType.simpleName
            )
        } else append(rawType.name)

        if (args.isNotEmpty()) append(args.joinToString(prefix = "<", separator = ", ", postfix = ">") { it.typeName })
    }

    companion object {
        operator fun invoke(type: ParameterizedType) =
                if (type is ParameterizedTypeImpl) type
                else ParameterizedTypeImpl(type.rawClass, type.actualTypeArguments.map { it.kodein() } .toTypedArray(), type.ownerType.kodein())

        private fun bind(type: ParameterizedType) = ParameterizedTypeImpl(
                type.rawClass,
                type.actualTypeArguments.map { if (it is WildcardType) it.upperBounds.firstOrNull() ?: Any::class.java else it } .toTypedArray(),
                type.ownerType
        )
    }
}
