App using

* Micrometer API for metrics, exporting using micrometer/spring OTLP exporter configured via application.properties
* Open Telemetry API for Tracing, exporting using OTLP JavaAgent exporter 
  * Traces are more rich when using java agent, @WithSpan annotations are intercepted.
  * HTTP, Kafka producing and consuming are also tracked.


## Build

```bash
$ ./gradlew build && docker-compose build && docker-compose up --force-recreate
```

## Reference 
* https://github.com/micrometer-metrics/micrometer-samples/blob/main/micrometer-samples-boot3-database/build.gradle
