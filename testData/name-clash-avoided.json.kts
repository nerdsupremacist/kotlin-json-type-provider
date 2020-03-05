
@file:JSONProvider("simple.json")
@file:JSONProvider(path = "subFolder/simple.json", typeName = "OtherSimple")

val first = Simple.parseFromFile("simple.json")
val second = OtherSimple.parseFromFile("subFolder/simple.json")

println(first.greeting)
println(second.other)