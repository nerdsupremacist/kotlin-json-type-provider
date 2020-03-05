package org.jetbrains.kotlin.script.examples.json.resolution.utils

fun <T> List<T>.isDistinct(): Boolean {
    val set = mutableSetOf<T>()
    for (entry in this) {
        if (set.contains(entry)) {
            return false
        }
        set.add(entry)
    }
    return true
}