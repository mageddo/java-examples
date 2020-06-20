# runtime-jar-loading
Programmatically loading jar at running JVM

# Running it
```bash
$ ./gradlew clean build &&\
 java -jar ./build/libs/runtime-jar-loading-simple-example.jar \ 
 ./hello-world-library/build/libs/hello-world-library.jar

Simple class loading and method execution
------------------------------
result: com.mageddo.runtimejarloading.helloworldlibrary.HelloWorldLibrary: Hello World!!!

Testing conflicting classpath
------------------------------
from main jar: 7 + 3=10
from loaded jar: 7 + 3=4
 
```

# Features
* [x] Dynamically jar load and method execution
* [x] Creation of separated ClassLoader for the jar to evict same classpath confliction 
