
@file:JSONProvider("no-info.json")

import kotlin.reflect.full.memberProperties

val parsed = NoInfo.parseFromFile("no-info.json")

println("parsed.info=${parsed.info}")
println("parsed.info.type=${parsed.javaClass.kotlin.memberProperties.first { it.name == "info" }.returnType}")