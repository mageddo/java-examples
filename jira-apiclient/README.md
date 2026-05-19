```bash
$ export $(grep -v '^#' .env | xargs)
$ ./gradlew quarkusDev
```
