## Instructions

#### Building 
It will build a vanilla jar called `proguard-example.jar` and obfuscated jar called `obfuscated.jar`

```bash
$ ./gradlew clean build proguard
$ ls -lha ./build/libs
total 8.0K
drwxr-xr-x 1 Typer 197121    0 Dec 28 00:19 .
drwxr-xr-x 1 Typer 197121    0 Dec 28 00:19 ..
-rw-r--r-- 1 Typer 197121 1.2K Dec 28 00:19 obfuscated.jar
-rw-r--r-- 1 Typer 197121 1.8K Dec 28 00:19 proguard-example.jar
```

#### Testing 
Running original and obfuscated jars to compare results

```bash
$ java -jar build/libs/proguard-example.jar
Hello World!!!
Hello World!!!
2022-12-28T00:20:08.001898

$ java -jar build/libs/obfuscated.jar
Hello World!!!
Hello World!!!
2022-12-28T00:20:16.319876
```

#### Checking obfuscated jar with a decompiler
Download [JD GUI][1] decompile the jar and check if the source is secure!

![](https://i.imgur.com/wVyQJQb.jpg)

[1]: http://java-decompiler.github.io/
