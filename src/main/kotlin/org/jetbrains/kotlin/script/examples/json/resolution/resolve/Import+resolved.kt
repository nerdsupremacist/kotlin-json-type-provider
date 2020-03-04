package org.jetbrains.kotlin.script.examples.json.resolution.resolve

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonElement
import org.jetbrains.kotlin.script.examples.json.resolution.Resolved
import org.jetbrains.kotlin.script.util.Import
import java.io.File

fun Import.resolve(baseDirectory: File?): List<Resolved> {
    return paths.map { path ->
        val file = baseDirectory?.resolve(path) ?: File(path)
        file.json().resolve(typeName = file.nameWithoutExtension)
    }
}

private fun File.json(): JsonElement {
    val content = inputStream().reader().readText()
    return Json(JsonConfiguration.Stable).parseJson(content)
}