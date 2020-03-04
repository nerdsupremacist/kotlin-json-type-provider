package org.jetbrains.kotlin.script.examples.json.resolution

import java.io.File

fun Resolved.code(baseDirectory: File?, isNested: Boolean = false): String? = when (this) {
    is Resolved.Object -> definition.code(baseDirectory = baseDirectory, isNested = isNested)
    is Resolved.Standard -> null
    is Resolved.Array -> resolved.code(baseDirectory = baseDirectory, isNested = isNested)
    is Resolved.Optional -> resolved.code(baseDirectory = baseDirectory, isNested = isNested)
    Resolved.Null -> null
}

fun Object.code(baseDirectory: File?, isNested: Boolean): String = buildString {
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
            appendln(type.code(baseDirectory = baseDirectory, isNested = true).prependIndent())
        }

        appendln("}")
    }

    if (!isNested) {
        appendln()
        append("val $name = object : ParsedFactory<${type.name}> {")
        if (baseDirectory != null) {
            appendln()
            appendln("override val baseDirectory: File? = File(\"${baseDirectory.path}\")".prependIndent())
        }
        append("}")
    }
}