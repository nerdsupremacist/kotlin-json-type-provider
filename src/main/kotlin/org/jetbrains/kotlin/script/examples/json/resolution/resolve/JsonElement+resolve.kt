package org.jetbrains.kotlin.script.examples.json.resolution.resolve

import kotlinx.serialization.json.*
import org.jetbrains.kotlin.script.examples.json.resolution.*
import org.jetbrains.kotlin.script.examples.json.resolution.utils.toCamelCase
import org.jetbrains.kotlin.script.examples.json.resolution.utils.toUpperCamelCase

fun JsonElement.resolve(typeName: String): Resolved = when (this) {
    is JsonLiteral -> resolve().asResolved()
    JsonNull -> Resolved.Null
    is JsonObject -> Object(
        name = typeName.toUpperCamelCase(),
        properties = content
            .mapKeys { it.key.toCamelCase() }
            .mapValues { entry ->
                entry.value.resolve(entry.key)
            }
    ).asResolved()
    is JsonArray -> content.resolve(typeName).array()
}

fun Collection<JsonElement>.resolve(typeName: String): Resolved {
    if (isEmpty()) return StandardType.Any.asResolved()
    return map { it.resolve(typeName = typeName) }.reduce { acc, resolved -> acc.mergedWith(resolved) }
}