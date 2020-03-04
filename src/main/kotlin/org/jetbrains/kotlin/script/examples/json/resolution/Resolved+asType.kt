package org.jetbrains.kotlin.script.examples.json.resolution

fun Resolved.asType(): Type = when (this) {
    is Resolved.Nested -> nested.asType()
    is Resolved.Standard -> type.asType()
    is Resolved.Array -> resolved.asType().array()
    is Resolved.Optional -> resolved.asType().optional()
    Resolved.Null -> StandardType.Any.asType().optional()
}