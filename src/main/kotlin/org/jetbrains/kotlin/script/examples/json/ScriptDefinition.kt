package org.jetbrains.kotlin.script.examples.json

import org.jetbrains.kotlin.script.util.Import
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromClassContext
import kotlin.script.experimental.jvm.jvm

object ScriptDefinition : ScriptCompilationConfiguration({
    defaultImports(
        Import::class,
        ParsedFactory::class
    )
    defaultImports(
        "org.jetbrains.kotlin.script.examples.jsonTypeProviderMainKts.parse",
        "org.jetbrains.kotlin.script.examples.jsonTypeProviderMainKts.parseFromFile",
        "org.jetbrains.kotlin.script.examples.jsonTypeProviderMainKts.parseList",
        "org.jetbrains.kotlin.script.examples.jsonTypeProviderMainKts.parseListFromFile"
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