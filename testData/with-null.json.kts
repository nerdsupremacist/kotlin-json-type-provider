
@file:JSONProvider("with-null.json")

import kotlin.reflect.full.memberProperties

val parsed = WithNull.parseFromFile("with-null.json")

println("parsed.info=${parsed.info}")
println("parsed.info.type=${parsed.javaClass.kotlin.memberProperties.first { it.name == "info" }.returnType}")