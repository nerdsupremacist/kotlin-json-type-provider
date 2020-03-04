package org.jetbrains.kotlin.script.examples.json

import org.jetbrains.kotlin.script.util.Import
import java.io.File
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromClassContext
import kotlin.script.experimental.jvm.jvm

object ScriptDefinition : ScriptCompilationConfiguration({
    defaultImports(
        File::class,
        Import::class,
        ParsedFactory::class
    )
    defaultImports(
        "org.jetbrains.kotlin.script.examples.json.parse",
        "org.jetbrains.kotlin.script.examples.json.parseFromFile",
        "org.jetbrains.kotlin.script.examples.json.parseList",
        "org.jetbrains.kotlin.script.examples.json.parseListFromFile"
    )

    jvm {
        dependenciesFromClassContext(ScriptDefinition::class, wholeClasspath = true)
    }

    refineConfiguration {
        onAnnotations(Import::class, handler = Configurator)
    }

    ide {
        acceptedLocations(ScriptAcceptedLocation.Everywhere)
    }
})