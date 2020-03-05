package org.jetbrains.kotlin.script.examples.json.resolution

@Target(AnnotationTarget.FILE)
@Repeatable
@Retention(AnnotationRetention.SOURCE)
annotation class JSONProvider(val path: String, val typeName: String = "")