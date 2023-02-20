Code at JDK 17 but compile to JDK 8!

### Testing

Compiling, requires JDK 17
```bash
$ ./gradlew clean build shadowjar
```

Running, only requires JDK 8

```bash
$ java -jar ./build/libs/source-target-compatibility-all.jar
> Text blocks
Hello World
from text blocks!!!

> Switch expressions
AMD 64 ARCH! 
```

Checking the compiled class major version

```bash
$ javap -v ./build/classes/java/main/com/mageddo/app/App.class | grep "major"
major version: 52
```

### Reference
* [\[1\]][1]
* [\[2\]][2]

[1]: https://github.com/bsideup/jabel
[2]: https://stackoverflow.com/a/60087444/2979435
