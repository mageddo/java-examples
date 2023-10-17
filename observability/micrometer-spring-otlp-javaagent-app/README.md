App using
* Micrometer API for Metrics and exporting using OTLP Java Agent Exporter
* Open Telemetry API for Tracing, exporting using OTLP Java Agent exporter
  * Traces are more rich when using java agent, @WithSpan annotations are intercepted.
  * HTTP, Kafka producing and consuming are also tracked.
* No Spring additional config is necessary

## Build

```bash
$ ./gradlew build && docker-compose build && docker-compose up --force-recreate
```

## Reference 
* https://github.com/micrometer-metrics/micrometer-samples/blob/main/micrometer-samples-boot3-database/build.gradle
