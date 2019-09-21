Compiling and running aspectj compile time

```
./gradlew aspectj-compile-time-gradle:run
before
Hello
after
```


Compiling to binary with graalvm
```
./gradlew aspectj-compile-time-gradle:nativeImage
./aspectj-compile-time-gradle/build/graal/aspectj-compile-time-gradle 
before
Hello
after
```

Using on Intellij

![](https://i.imgur.com/0T4vsxY.png)
