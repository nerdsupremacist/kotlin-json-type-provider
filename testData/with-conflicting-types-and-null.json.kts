
@file:JSONProvider("with-conflicting-types-and-null.json")

import kotlin.reflect.full.memberProperties

val parsed = WithConflictingTypesAndNull.parseFromFile("with-conflicting-types-and-null.json")

println("parsed.info=${parsed.info}")
println("parsed.info.type=${parsed.javaClass.kotlin.memberProperties.first { it.name == "info" }.returnType}")