package org.jetbrains.kotlin.script.examples.json

import kotlin.script.experimental.annotations.KotlinScript

@Suppress("unused")
@KotlinScript(
    fileExtension = "json.kts",
    compilationConfiguration = ScriptDefinition::class
)
abstract class Script(val args: Array<String>)