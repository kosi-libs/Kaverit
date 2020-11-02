package org.kodein.type

import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class GenericArrayTypeImpl(val component: Type) : GenericArrayType {
    override fun getGenericComponentType(): Type = component

    /** @suppress */
    override fun hashCode() = typeHashCode()

    /** @suppress */
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Type)
            return false

        return typeEquals(other)
    }

    /** @suppress */
    override fun toString() = "[L$component;"


    companion object {
        operator fun invoke(type: GenericArrayType) =
            if (type is GenericArrayTypeImpl) type
            else GenericArrayTypeImpl(type.kodein())
    }
}
