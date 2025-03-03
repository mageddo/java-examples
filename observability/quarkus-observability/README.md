# quarkus-observability
Configured Quarkus:

* With Micrometer as API to code Metrics
* With OTLP to report metrics and traces 
* OTLP as API to code Traces
* Custom Handler which reports WARN and ERROR logs as Tracings

**Running**

Run observability stack on java-examples/observability/infra-stack

## Enable Observability With Quarkus (Metrics + Tracing)

* https://quarkus.io/guides/telemetry-micrometer-to-opentelemetry
* https://quarkus.io/guides/telemetry-micrometer
* https://quarkus.io/guides/opentelemetry-tracing
* https://quarkus.io/guides/opentelemetry
* https://quarkus.io/blog/quarkus-observability-3-3/
* https://quarkus.io/guides/opentelemetry-metrics
* https://docs.quarkiverse.io/quarkus-micrometer-registry/dev/micrometer-registry-otlp.html

```bash
$ curl -i http://localhost:8282/q/metrics
HTTP/1.1 200 OK
Content-Type: application/openmetrics-text; version=1.0.0; charset=utf-8
content-length: 15516

# TYPE process_files_max_files gauge
# HELP process_files_max_files The maximum file descriptor count
process_files_max_files 1048576.0
....
```


## Exporting logs as tracing to Jaeger

* https://github.com/mageddo/java-examples/blob/052ab28de3afb26a946258e00fa938d5164148ff/observability/exports-logs-to-jaeger-quarkus/src/main/resources/logback.xml.bkp
* https://quarkus.io/guides/cdi-reference
* [[2][1]]

Generating Traces

```bash
$ curl -i -w '\n'  localhost:8282/hello
```

## Other Researches
* https://chatgpt.com/c/67c37d09-c020-800a-bb0b-8d734d2c9df0
* https://chatgpt.com/c/67c5f9b8-e148-800a-961a-4c740dc43355

[1]: https://chatgpt.com/c/67c37d09-c020-800a-bb0b-8d734d2c9df0

-------------------------------

## Quarkus auto generated stuff Below


This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./gradlew build -Dquarkus.native.enabled=true
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./gradlew build -Dquarkus.native.enabled=true -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/exports-logs-to-jaeger-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/gradle-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
