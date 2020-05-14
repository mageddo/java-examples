Compiling and running aspectj compile time

```
$ ./gradlew shadowJar && java -jar ./build/libs/aspectj-compile-time-gradle-all.jar

before
Hello
after
```


Compiling to binary with graalvm
```
$ ./gradlew nativeImage
$ ./build/native-image/aspectj-compile-time-gradle
 
before
Hello
after
```

Using on Intellij

![](https://i.imgur.com/0T4vsxY.png)
