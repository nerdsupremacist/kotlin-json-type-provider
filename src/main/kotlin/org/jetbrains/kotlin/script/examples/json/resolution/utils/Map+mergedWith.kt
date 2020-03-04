package org.jetbrains.kotlin.script.examples.json.resolution.utils

fun <K, V> Map<K, V>.mergedWithNonNull(other: Map<K, V>, merge: V.(other: V) -> V): Map<K, V> {
    return mergedWith(other) { otherValue -> otherValue?.let { merge(it) } ?: this }
}

fun <K, V> Map<K, V>.mergedWith(other: Map<K, V>, merge: V.(other: V?) -> V): Map<K, V> {
    return (keys + other.keys)
        .associateWith { key ->
            val left = this[key]
            val right = other[key]
            if (left != null) {
                left.merge(right)
            } else {
                right!!.merge(left)
            }
        }
}