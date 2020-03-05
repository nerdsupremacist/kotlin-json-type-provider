package org.jetbrains.kotlin.script.examples.json.resolution

import org.jetbrains.kotlin.script.examples.json.resolution.utils.mergedWith
import java.io.File

//region Definition of an Object

data class Object(val name: String, val properties: Map<String, Resolved>)

//endregion


//region Converting to other types

fun Object.asType() = Type(name = "${name}Impl")

fun Object.asResolved(): Resolved = Resolved.Object(this)

//endregion


//region Merging with another version of the Object

fun Object.mergedWith(other: Object): Object {
    require(name == other.name)

    val mergedProperties = properties
        .mergedWith(other.properties) { otherType ->
            mergedWith(otherType ?: Resolved.Null) // handle missing key as null
        }

    return copy(properties = mergedProperties)
}

//endregion


//region Code Generation

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

        for (nestedType in nestedTypes) {
            appendln(nestedType.code(baseDirectory = baseDirectory, isNested = true).prependIndent())
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

//endregion