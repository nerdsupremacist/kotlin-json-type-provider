
@file:JSONProvider("simple.json")
@file:JSONProvider("otherSubFolder/simple.json")

val first = Simple.parseFromFile("simple.json")
val second = Simple.parseFromFile("otherSubFolder/simple.json")

println(first.greeting)
println(second.greeting)