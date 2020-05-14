
If you load two classes with same package and name which one will be executed?

R: The one what was loaded first
```bash

$ ./gradlew build
$ java -cp ./build/classes/java/main/:./lib/ com.mageddo.fruitcase.FruitMain
3

$ java -cp ./lib:./build/classes/java/main/ com.mageddo.fruitcase.FruitMain
2
```
