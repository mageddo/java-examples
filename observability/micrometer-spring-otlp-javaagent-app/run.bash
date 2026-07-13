#!bin/bash

# para fins de instrumentação e mandar metricas da jvm e libs dela automaticamente
export OTEL_SERVICE_NAME="micrometer-spring-otlp-javaagent-app"
export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4318
export OTEL_RESOURCE_ATTRIBUTES="service.namespace=com.mageddo,service.id=1231,service.version=1.0.0-alpha"
export OTEL_TRACES_SAMPLER="always_on"

java \
  -javaagent:./tmp/otel-agent-2.jar \
  -Dotel.javaagent.extensions=span-metrics/build/libs/span-metrics.jar \
  -jar build/libs/micrometer-spring-otlp-javaagent-app.jar
