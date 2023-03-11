This a set of pratical example for real world application using Dagger 2, it tests the following usecases

* [Multi layer application creating a component test stubing the dao][1]
* [Understanding modules][2]

## Running the main sample

```bash
$ ./gradlew clean build shadowJar
$ ls -lha build/libs/dagger-2-all.jar 
-rw-rw-r-- 1 typer typer 57K mar 10 22:47 build/libs/dagger-2-all.jar
$ java -jar build/libs/dagger-2-all.jar
Strawberry was delivered
```

[1]: src/main/java/com/mageddo/main
[2]: src/main/java/com/mageddo/ex02_modules
