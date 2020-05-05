### Current Status

* [x] Spring JBC + Postgres
* [x] JDBC Transactional Support
* [x] Thymeleaf template processing
* [x] RESTFul
* [x] Static resources
* [x] Automated Component Tests
* [x] Junit 5
* [x] Embedded Postgres

### Building binary and running

**GraalVM is expected to be set on `JAVA_HOME` env**

Setup Postgres

```bash
$ docker-compose up
```

Building
```bash
$ ./gradlew flywayMigrate nativeImage
```

Running the app 
```bash
$ ./build/native-image/micronaut-graalvm
```

### Testing the API

Access http://localhost:8080
