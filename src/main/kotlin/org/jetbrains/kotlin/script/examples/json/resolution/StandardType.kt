package org.jetbrains.kotlin.script.examples.json.resolution


//region Definition of a Standard Type

enum class StandardType {
    Any, String, Boolean, Int, Double
}

//endregion


//region Converting to other types

fun StandardType.asType() = Type(name = name)

fun StandardType.asResolved(): Resolved = Resolved.Standard(this)

//endregion