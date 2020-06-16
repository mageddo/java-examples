## Compiling and running using gradle

```
$ ./gradlew build
$ ./gradlew run
I'm inside main()
I'm a native Agent....
```

## Compiling manually
Compiling DLL

```bash
$ gcc -fPIC -shared -o ./build/JvmtiHelloWorld.dll \
-I"$JAVA_HOME/include" \
-I"${JAVA_HOME}/include/win32" \
./src/main/c/JvmtiHelloWorld.c
```

Running program
```bash
$ ${JAVA_HOME}/bin/java \
-agentpath:"$PWD/build/JvmtiHelloWorld.dll" \
-cp ./build/classes/java/main com.mageddo.jvmti.JniHelloWorld

I'm a native Agent....
I'm inside main()

```
