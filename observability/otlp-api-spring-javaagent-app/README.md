App using Open Telemetry API and exporting metrics and Traces using OTLP Java Agent Instrumentor, no Micrometer used.

## Build

```bash
$ ./gradlew build && docker-compose build && docker-compose up --force-recreate
```

## Reference 
* https://opentelemetry.io/docs/instrumentation/java/automatic/
* https://opentelemetry.io/docs/instrumentation/java/automatic/annotations/
* https://github.com/open-telemetry/opentelemetry-java/blob/main/sdk-extensions/autoconfigure/README.md#prometheus-exporter
