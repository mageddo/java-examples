By some reason native-image won't generate binary because of `com.sun.jna.platform.win32.Advapi32` while
it works great on x64.

```bash
$ docker run --rm --privileged multiarch/qemu-user-static --reset -p yes
$ docker-compose build -progress=plain
```

But on Linux x64 works great
```bash
$ uname -sm
Linux x86_64

$ ./gradlew build -Dquarkus.package.type=uber-jar
```

