package org.jetbrains.kotlin.script.examples.json.resolution

fun Resolved.code(level: Int = 0): String? = when (this) {
    is Resolved.Object -> definition.code(level = level)
    is Resolved.Standard -> null
    is Resolved.Array -> resolved.code(level = level)
    is Resolved.Optional -> resolved.code(level = level)
    Resolved.Null -> null
}


fun Object.code(level: Int): String = buildString {
    val type = asType()
    val constructorProperties = properties
        .entries
        .joinToString(", ") { entry ->
            "val ${entry.key}: ${entry.value.asType().name}"
        }

    append("data class ${type.name}($constructorProperties)")

    val nestedTypes = properties.values.mapNotNull { it.underlyingType().asObjectOrNull() }

    if (nestedTypes.isNotEmpty()) {
        appendln(" {")
        appendln()

        for (type in nestedTypes) {
            appendln(type.code(level = level + 1).prependIndent())
        }

        appendln("}")
    }

    if (level < 1) {
        appendln()
        append("val $name = object : ParsedFactory<${type.name}> {}")
    }
}