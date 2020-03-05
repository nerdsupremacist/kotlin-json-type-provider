package org.jetbrains.kotlin.script.examples.json.resolution

import kotlinx.serialization.json.*
import org.jetbrains.kotlin.script.examples.json.resolution.utils.toCamelCase
import org.jetbrains.kotlin.script.examples.json.resolution.utils.toUpperCamelCase
import org.jetbrains.kotlin.script.util.Import
import java.io.File

//region Resolve an Import

fun JSONProvider.resolve(baseDirectory: File?): Resolved {
    val file = baseDirectory?.resolve(path)?.takeIf { it.exists() } ?: File(path)
    val typeName = typeName.takeIf { it.isNotBlank() } ?: file.nameWithoutExtension
    return file.json().resolve(typeName = typeName)
}

//endregion


//region Resolve JSON Values

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

fun JsonLiteral.resolve(): StandardType {
    if (isString) return StandardType.String
    if (primitive.booleanOrNull != null) return StandardType.Boolean
    if (primitive.intOrNull != null) return StandardType.Int
    if (primitive.doubleOrNull != null) return StandardType.Double
    return StandardType.Any
}

//endregion


//region Utils

private fun File.json(): JsonElement {
    val content = inputStream().reader().readText()
    return Json(JsonConfiguration.Stable).parseJson(content)
}

private fun Collection<JsonElement>.resolve(typeName: String): Resolved {
    if (isEmpty()) return StandardType.Any.asResolved()
    return map { it.resolve(typeName = typeName) }.reduce { acc, resolved -> acc.mergedWith(resolved) }
}

//endregion