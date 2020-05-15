# code-with-quarkus project

* [x] [Static Resources][8]
* [x] [Reactive WebSockets][9]
* [x] [Rest Server][10]
* [x] [Kafka Consuming][12]
* [x] [Scheduler][11]
* [x] [Transactional support on services + JDBI][13]
* [x] Flyway
* [x] Swagger
* [x] [Thymeleaf][7]

# Running

Start Postgres instance
```
$ docker-compose up
```

Run the app
```
$ ./gradlew quarkusDev
```

Access http://localhost:8084

![](https://i.imgur.com/xNSZ0m5.png)

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./gradlew quarkusDev
```

## Packaging and running the application

The application can be packaged using `./gradlew quarkusBuild`.
It produces the `code-with-quarkus-1.0.0-SNAPSHOT-runner.jar` file in the `build` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/lib` directory.

The application is now runnable using `java -jar build/code-with-quarkus-1.0.0-SNAPSHOT-runner.jar`.

If you want to build an _über-jar_, just add the `--uber-jar` option to the command line:
```
./gradlew quarkusBuild --uber-jar
```

## Creating a native executable

You can create a native executable using: `./gradlew build -Dquarkus.package.type=native`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./build/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling#building-a-native-executable.


## Features 

### Static Resources

Access: http://localhost:8084/prices.html

### Reactive WebSockets

Take a look at http://localhost:8084/prices.html to see a client working sample for web sockets. 

```
$ curl http://localhost:8084/prices/stream

data: 32.10

data: 13.87
```

### Rest Server

```bash
$ curl -w '\n' http://localhost:8084/stocks
[{"symbol":"PAGS","price":1.2084996008677629}]
```

### Kafka Consuming

See [consumer example][4], also take a look at [mageddo's kafka-client][2] library

### Scheduler

[Click here][5] to see a Scheduler working sample

### Transactional + JDBI
See [this reference][6] for a Transactional working sample sample, [click here][3] to take a look at the library for
 Transactional support for JDBI on JAVA EE apps.

[1]: https://quarkus.io/guides/kafka#starting-kafka
[2]: https://github.com/mageddo-projects/kafka-client
[3]: https://github.com/mageddo-projects/javaee-jdbi
[4]: src/main/java/com/mageddo/mdb/StockPriceMDB.java
[5]: src/main/java/com/mageddo/mdb/StockPriceMDB.java#L76
[6]: src/main/java/com/mageddo/service/StockPriceService.java#L48
[7]: src/main/java/com/mageddo/resource/UserResource.java
[8]: #static-resources
[9]: #reactive-websockets
[10]: #rest-server
[11]: #scheduer
[12]: #kafka-consuming
[13]: #transactional--jdbi
