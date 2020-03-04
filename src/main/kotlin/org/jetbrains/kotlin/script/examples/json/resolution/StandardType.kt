package org.jetbrains.kotlin.script.examples.json.resolution

enum class StandardType {
    Any, String, Boolean, Int, Double
}

fun StandardType.asType() = Type(name = name)

fun StandardType.asResolved(): Resolved = Resolved.Standard(this)