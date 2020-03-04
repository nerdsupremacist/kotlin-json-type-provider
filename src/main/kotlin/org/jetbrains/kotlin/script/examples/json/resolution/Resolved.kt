package org.jetbrains.kotlin.script.examples.json.resolution

import org.jetbrains.kotlin.script.examples.json.resolution.Object as ObjectDefinition

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
            val otherResolved = other.asOptionalOfResolvedOrNull() ?: return StandardType.Any.asResolved()

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