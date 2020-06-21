## jvm-class-agent
This repository have a lot of code designed to
 
* Dynamically inject a JavaAgent into a running JVM
* JVMTI examples
* JNI examples
* List all JVM class instances 

## Compiling and running using gradle

Build packages

```
$ ./gradlew build
```

Run an example program to attach the agent
```
java -cp ./build/libs/jvmti-jvm-class-agent-all.jar com.mageddo.jvmti.Main
```

Attach the agent passing the example program pid 
```
java -cp ./build/libs/jvmti-jvm-class-agent-all.jar com.mageddo.jvmti.ExternalJvmAttach 13674
```

Count how many instances of JiraIssue class example program has 
```
curl -w '\n' localhost:8200/instances -d 'com.mageddo.jvmti.JiraIssue'
```