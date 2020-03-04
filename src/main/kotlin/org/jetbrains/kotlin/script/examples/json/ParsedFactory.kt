package org.jetbrains.kotlin.script.examples.json

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

interface ParsedFactory<Value : Any>

inline fun <reified T : Any> ParsedFactory<T>.parseFromFile(name: String): T {
    return File(name).let(::parseFromFile)
}

inline fun <reified T : Any> ParsedFactory<T>.parseFromFile(file: File): T {
    return file.inputStream().reader().readText().let(::parse)
}

inline fun <reified T : Any> ParsedFactory<T>.parse(string: String): T {
    val mapper = jacksonObjectMapper()
    return mapper.readValue(string)
}

inline fun <reified T : Any> ParsedFactory<T>.parseListFromFile(name: String): List<T> {
    return File(name).let(::parseListFromFile)
}

inline fun <reified T : Any> ParsedFactory<T>.parseListFromFile(file: File): List<T> {
    return file.inputStream().reader().readText().let { parseList(it) }
}

inline fun <reified T : Any> ParsedFactory<T>.parseList(string: String): List<T> {
    val mapper = jacksonObjectMapper()
    return mapper.readValue(string)
}
