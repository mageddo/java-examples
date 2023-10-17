App using Micrometer API and exporting metrics and traces using OLTP exporter configured via application.properties
using Spring

## Build

```bash
$ ./gradlew build && docker-compose build && docker-compose up --force-recreate
```

## Reference 
* https://github.com/micrometer-metrics/micrometer-samples/blob/main/micrometer-samples-boot3-database/build.gradle
