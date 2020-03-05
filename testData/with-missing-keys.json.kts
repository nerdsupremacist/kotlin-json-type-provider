
@file:JSONProvider("with-missing-keys.json")

import kotlin.reflect.full.memberProperties

val parsed = WithMissingKeys.parseFromFile("with-missing-keys.json")

val info = parsed.values.map { it.info }
println("parsed.values.info=${info}")
println("parsed.values.info.type=${parsed.javaClass.kotlin.nestedClasses.first().memberProperties.first { it.name == "info" }.returnType}")