Annotation processing on Java 7

### Testing

This project created a annotation processor who turn all classs fields final.
The main class tried to assign a value to the field that is not final on source but
will turn on a final field on compile time  

```
./gradlew build
> Task :example:compileJava FAILED
found annotation
.../annotation-processor/example/src/main/java/MutableClass.java:16: error: cannot assign a value to final variable name
        new MutableClass("x").name = "x";
                             ^
1 error

```
