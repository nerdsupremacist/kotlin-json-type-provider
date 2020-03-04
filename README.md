# kotlin-json-type-provider
Type Provider for JSON files inside of Kotlin Scripting

## Example

Use `@Import` with a JSON file and it will create all the types needed to use that json file with full type safety

```
@file:Import("sample.json") // { "greeting" : "Hello World!" }

val parsed = Sample.parseSingleFromFile("sample.json")
print(parsed.greeting) // "Hello World!"
```

## Usage

coming soon
