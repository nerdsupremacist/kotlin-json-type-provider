package org.jetbrains.kotlin.script.examples.json

import org.jetbrains.kotlin.script.examples.json.resolution.asType
import org.jetbrains.kotlin.script.examples.json.resolution.code
import org.jetbrains.kotlin.script.examples.json.resolution.resolve.resolve
import org.jetbrains.kotlin.script.examples.json.resolution.utils.isDistinct
import org.jetbrains.kotlin.script.util.Import
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
                    is Import -> annotation
                    else -> null
                }
            }
            ?.takeIf { it.isNotEmpty() } ?: return context.compilationConfiguration.asSuccess()

        val resolved = annotations.flatMap { it.resolve(baseDirectory = baseDirectory) }

        require(resolved.map { it.asType().name }.isDistinct()) { "No types should be duplicated" }

        val sourceCode = resolved
            .mapNotNull { it.code() }
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