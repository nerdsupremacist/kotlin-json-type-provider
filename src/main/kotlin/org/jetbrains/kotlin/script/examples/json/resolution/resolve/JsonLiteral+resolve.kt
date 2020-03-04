package org.jetbrains.kotlin.script.examples.json.resolution.resolve

import kotlinx.serialization.json.JsonLiteral
import org.jetbrains.kotlin.script.examples.json.resolution.StandardType

fun JsonLiteral.resolve(): StandardType {
    if (isString) return StandardType.String
    if (primitive.booleanOrNull != null) return StandardType.Boolean
    if (primitive.intOrNull != null) return StandardType.Int
    if (primitive.doubleOrNull != null) return StandardType.Double
    return StandardType.Any
}