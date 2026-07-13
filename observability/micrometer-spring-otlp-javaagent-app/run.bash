#!bin/bash

# para fins de instrumentação e mandar metricas da jvm e libs dela automaticamente
export OTEL_SERVICE_NAME="micrometer-spring-otlp-javaagent-app"
export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4318
export OTEL_RESOURCE_ATTRIBUTES="service.namespace=com.mageddo,service.id=1231,service.version=1.0.0-alpha"
export OTEL_TRACES_SAMPLER="always_on"

OTEL_AGENT_VERSION="2.29.0"
OTEL_AGENT_JAR="./tmp/otel-agent-2.jar"

if [ ! -f "$OTEL_AGENT_JAR" ]; then
  echo "OpenTelemetry Java agent not found, downloading v${OTEL_AGENT_VERSION}..."
  mkdir -p "$(dirname "$OTEL_AGENT_JAR")"
  curl -fSL \
    "https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_AGENT_VERSION}/opentelemetry-javaagent.jar" \
    -o "$OTEL_AGENT_JAR"
fi

java \
  -javaagent:"$OTEL_AGENT_JAR" \
  -Dotel.javaagent.extensions=span-metrics/build/libs/span-metrics.jar \
  -jar build/libs/micrometer-spring-otlp-javaagent-app.jar
