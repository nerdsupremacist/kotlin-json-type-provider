package org.jetbrains.kotlin.script.examples.json.resolution

fun Resolved.underlyingType(): Resolved = when (this) {
    is Resolved.Object -> this
    is Resolved.Standard -> this
    is Resolved.Array -> resolved.underlyingType()
    is Resolved.Optional -> resolved.underlyingType()
    Resolved.Null -> this
}