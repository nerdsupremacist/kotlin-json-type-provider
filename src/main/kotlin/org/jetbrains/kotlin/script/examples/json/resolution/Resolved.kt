package org.jetbrains.kotlin.script.examples.json.resolution

import java.io.File
import org.jetbrains.kotlin.script.examples.json.resolution.Object as ObjectDefinition

//region Definition of a Resolved Type

sealed class Resolved {
    abstract fun mergedWith(other: Resolved): Resolved

    data class Object(val definition: ObjectDefinition) : Resolved() {
        override fun mergedWith(other: Resolved): Resolved {
            if (other == Null) return optional()
            val otherObject = other.asObjectOrNull() ?: return StandardType.Any.asResolved()

            return definition.mergedWith(otherObject).asResolved()
        }
    }

    data class Standard(val type: StandardType) : Resolved() {
        override fun mergedWith(other: Resolved): Resolved {
            if (other == Null) return optional()
            val otherType = other.asStandardTypeOrNull() ?: return StandardType.Any.asResolved()

            if (otherType != type) return StandardType.Any.asResolved()

            return this
        }
    }

    data class Array(val resolved: Resolved) : Resolved() {
        override fun mergedWith(other: Resolved): Resolved {
            if (other == Null) return optional()
            val otherResolved = other.asArrayOfResolvedOrNull() ?: return StandardType.Any.asResolved()

            return resolved.mergedWith(otherResolved).array()
        }
    }

    data class Optional(val resolved: Resolved) : Resolved() {
        override fun mergedWith(other: Resolved): Resolved {
            if (other == Null) return this
            val otherResolved = other.asOptionalOfResolvedOrNull() ?: other

            return resolved.mergedWith(otherResolved).optional()
        }
    }

    object Null : Resolved() {
        override fun mergedWith(other: Resolved): Resolved = when (other) {
            is Optional -> other
            Null -> other
            else -> other.optional()
        }
    }
}

//endregion


//region (De-)Specializing a Resolved Type

fun Resolved.array(): Resolved = Resolved.Array(this)

fun Resolved.optional(): Resolved = Resolved.Optional(this)

fun Resolved.underlyingType(): Resolved = when (this) {
    is Resolved.Object -> this
    is Resolved.Standard -> this
    is Resolved.Array -> resolved.underlyingType()
    is Resolved.Optional -> resolved.underlyingType()
    Resolved.Null -> this
}

//endregion


//region Converting to Type

fun Resolved.asType(): Type = when (this) {
    is Resolved.Object -> definition.asType()
    is Resolved.Standard -> type.asType()
    is Resolved.Array -> resolved.asType().array()
    is Resolved.Optional -> resolved.asType().optional()
    Resolved.Null -> StandardType.Any.asType().optional()
}

//endregion


//region Getting the individual Cases

fun Resolved.asObjectOrNull(): ObjectDefinition? = (this as? Resolved.Object)?.definition

fun Resolved.asStandardTypeOrNull(): StandardType? = (this as? Resolved.Standard)?.type

fun Resolved.asArrayOfResolvedOrNull(): Resolved? = (this as? Resolved.Array)?.resolved

fun Resolved.asOptionalOfResolvedOrNull(): Resolved? = (this as? Resolved.Optional)?.resolved

//endregion


//region Code Generation

fun Resolved.code(baseDirectory: File?, isNested: Boolean = false): String? = when (this) {
    is Resolved.Object -> definition.code(baseDirectory = baseDirectory, isNested = isNested)
    is Resolved.Standard -> null
    is Resolved.Array -> resolved.code(baseDirectory = baseDirectory, isNested = isNested)
    is Resolved.Optional -> resolved.code(baseDirectory = baseDirectory, isNested = isNested)
    Resolved.Null -> null
}

//endregion