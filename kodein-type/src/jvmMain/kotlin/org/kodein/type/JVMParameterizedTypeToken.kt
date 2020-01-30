package org.kodein.type

import java.lang.reflect.*

internal class JVMParameterizedTypeToken<T>(override val jvmType: ParameterizedType) : JVMAbstractTypeToken<T>() {
    override fun simpleErasedDispString() = jvmType.simpleErasedName()

    override fun qualifiedErasedDispString() = jvmType.qualifiedErasedName()

    override fun getGenericParameters(): Array<TypeToken<*>> {
        return jvmType.actualTypeArguments.map { typeToken(it) }.toTypedArray()
    }

    @Suppress("UNCHECKED_CAST")
    override fun getRaw(): TypeToken<T> = JVMClassTypeToken(jvmType.rawClass) as TypeToken<T>

    override fun isGeneric() = true

    override fun isWildcard(): Boolean {
        jvmType.rawClass.typeParameters.forEachIndexed { i, variable ->
            val argument = jvmType.actualTypeArguments[i]
            if (!(argument == Object::class.java || (argument is WildcardType && variable.bounds.all { it in argument.upperBounds })))
                return false
        }
        return true
    }

    override fun getSuper(): List<TypeToken<*>> =
            ((jvmType.rawClass.genericSuperclass ?: (jvmType.rawClass.superclass as Class<*>?))?.takeIf { it != Object::class.java } ?.let { listOf(typeToken(jvmType.reify(it))) } ?: emptyList()) +
                    jvmType.rawClass.genericInterfaces.map { typeToken(jvmType.reify(it)) }

}
