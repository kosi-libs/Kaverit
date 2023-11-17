package org.kodein.type

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

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

        return typeEquals(other)
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
    }
}
