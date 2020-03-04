package org.jetbrains.kotlin.script.examples.json.resolution

data class Type(val name: String)

fun Type.array() = Type(name = "List<$name>")
fun Type.optional() = Type(name = "$name?")