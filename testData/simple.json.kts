
@file:Import("simple.json")

val parsed = Simple.parseFromFile("simple.json")
println(parsed.greeting)