package org.jetbrains.kotlin.script.examples.json.resolution

fun Resolved.asObjectOrNull(): Object? = (this as? Resolved.Object)?.definition

fun Resolved.asStandardTypeOrNull(): StandardType? = (this as? Resolved.Standard)?.type

fun Resolved.asArrayOfResolvedOrNull(): Resolved? = (this as? Resolved.Array)?.resolved

fun Resolved.asOptionalOfResolvedOrNull(): Resolved? = (this as? Resolved.Optional)?.resolved