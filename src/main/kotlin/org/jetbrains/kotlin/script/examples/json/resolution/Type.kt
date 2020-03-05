package org.jetbrains.kotlin.script.examples.json.resolution


//region Definition of a Type

data class Type(val name: String)

//endregion


//region Specializing Types

fun Type.array() = Type(name = "List<$name>")
fun Type.optional() = Type(name = "$name?")

//endregion