package org.kodein.type

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KVariance

internal fun KType.dispString(disp: KClass<*>.() -> String?): String {

    val classifierString = when (val c = classifier) {
        is KClass<*> -> c.disp() ?: return "(non-denotable type)"
        else -> return "(non-denotable type)"
    }

    return buildString {
        append(classifierString)

        if (arguments.isNotEmpty()) {
            append('<')

            arguments.forEachIndexed { index, argument ->
                if (index > 0) append(", ")

                if (argument.variance == null) {
                    append('*')
                } else {
                    append(when (argument.variance) {
                        KVariance.IN -> "in "
                        KVariance.OUT -> "out "
                        else -> ""
                    })
                    append(argument.type!!.dispString(disp))
                }
            }

            append('>')
        }

        if (isMarkedNullable) append('?')
    }
}
