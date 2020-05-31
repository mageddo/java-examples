# antlr-gradle-json-parser
ANTLR configured project example configured with gradle

# Features
* Compiles JSON grammar as example
* Runs and walks through tokens at runtime
* Converts JSON to [Lua Table][1]

# Running it
```bash
./gradlew build shadowJar && java -jar build/libs/antlr-gradle-json-parser-all.jar
```

[1]: https://www.lua.org/pil/2.5.html
