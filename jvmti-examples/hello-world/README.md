```
gcc -fPIC -shared -o ./build/JvmtiHelloWorld.dll \
-I"$JAVA_HOME/include" \
-I"${JAVA_HOME}/include/win32" \
./src/main/c/JvmtiHelloWorld.c
```
```
java -agentpath:"$PWD/build/JvmtiHelloWorld.dll" \
./build/classes/java/main/HelloWorld.class
```
