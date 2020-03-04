package org.jetbrains.kotlin.script.examples.json.resolution

import org.jetbrains.kotlin.script.examples.json.resolution.utils.mergedWith

data class Object(val name: String, val properties: Map<String, Resolved>)

fun Object.asType() = Type(name = "${name}Impl")

fun Object.asResolved(): Resolved = Resolved.Object(this)

fun Object.mergedWith(other: Object): Object {
    require(name == other.name)

    val mergedProperties = properties
        .mergedWith(other.properties) { otherType ->
            mergedWith(otherType ?: Resolved.Null) // handle missing key as null
        }

    return copy(properties = mergedProperties)
}