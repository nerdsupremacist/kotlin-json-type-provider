package org.jetbrains.kotlin.script.examples.json

import org.jetbrains.kotlin.script.examples.json.resolution.JSONProvider
import org.jetbrains.kotlin.script.examples.json.resolution.asType
import org.jetbrains.kotlin.script.examples.json.resolution.code
import org.jetbrains.kotlin.script.examples.json.resolution.resolve
import org.jetbrains.kotlin.script.examples.json.resolution.utils.isDistinct
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.FileBasedScriptSource
import kotlin.script.experimental.host.toScriptSource

object Configurator : RefineScriptCompilationConfigurationHandler {

    override fun invoke(context: ScriptConfigurationRefinementContext): ResultWithDiagnostics<ScriptCompilationConfiguration> {
        val baseDirectory = (context.script as? FileBasedScriptSource)?.file?.parentFile

        val annotations = context
            .collectedData
            ?.get(ScriptCollectedData.foundAnnotations)
            ?.mapNotNull { annotation ->
                when (annotation) {
                    is JSONProvider -> annotation
                    else -> null
                }
            }
            ?.takeIf { it.isNotEmpty() } ?: return context.compilationConfiguration.asSuccess()

        val resolved = annotations.map { it.resolve(baseDirectory = baseDirectory) }

        resolved
            .groupBy { it.asType().name }
            .filterValues { it.count() > 1 }
            .takeIf { it.isNotEmpty() }
            ?.keys
            ?.map { "Multiple definitions of `$it`".asErrorDiagnostics() }
            ?.let { return makeFailureResult(it) }

        val sourceCode = resolved
            .mapNotNull { it.code(baseDirectory = baseDirectory) }
            .map { resolvedCode ->
                createTempFile(prefix = "JSONCodeGen", suffix = ".json.kts", directory = baseDirectory)
                    .apply { writeText(resolvedCode) }
                    .apply { deleteOnExit() }
                    .toScriptSource()
            }

        return ScriptCompilationConfiguration(context.compilationConfiguration) {
            importScripts.append(sourceCode)
        }.asSuccess()
    }

}