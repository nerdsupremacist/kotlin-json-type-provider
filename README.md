# kotlin-json-type-provider
Type Provider for JSON files inside of Kotlin Scripting

## Example

Use `@JSONProvider` with a JSON file and it will create all the types needed to use that json file with full type safety

```kotlin
@file:JSONProvider("sample.json") // { "greeting" : "Hello World!" }

val parsed = Sample.parseFromFile("sample.json")
print(parsed.greeting) // "Hello World!"
```

## Usage

coming soon
