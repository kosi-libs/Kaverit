package org.kodein.type

internal class JVMClassTypeToken<T>(override val jvmType: Class<T>) : JVMAbstractTypeToken<T>() {

    override fun simpleErasedDispString() = jvmType.simpleErasedName()
    override fun qualifiedErasedDispString() = jvmType.qualifiedErasedName()

    override fun getRaw() = this

    override fun getGenericParameters(): Array<TypeToken<*>> = jvmType.typeParameters.map { typeToken(it.bounds[0]) } .toTypedArray()

    override fun isGeneric() = false
    override fun isWildcard() = true

    override fun getSuper() = (jvmType.boundedGenericSuperClass?.takeIf { it != Object::class.java }?.let { listOf(typeToken(it)) } ?: emptyList()) +
            jvmType.genericInterfaces.map { typeToken(it) }

    override fun isAssignableFrom(typeToken: TypeToken<*>): Boolean {
        return if (typeToken is JVMClassTypeToken)
            jvmType.isAssignableFrom(typeToken.jvmType)
        else
            super.isAssignableFrom(typeToken)
    }
}
