
@file:JSONProvider("with-conflicting-types.json")

import kotlin.reflect.full.memberProperties

val parsed = WithConflictingTypes.parseFromFile("with-conflicting-types.json")

println("parsed.info=${parsed.info}")
println("parsed.info.type=${parsed.javaClass.kotlin.memberProperties.first { it.name == "info" }.returnType}")