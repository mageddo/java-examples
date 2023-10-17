FROM openjdk:17

WORKDIR /app
COPY ./build/libs/micrometer-spring-otlp-registry-app-*.jar /app/micrometer-spring-otlp-registry-app.jar

ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.31.0/opentelemetry-javaagent.jar /app/opentelemetry-javaagent.jar

# disabling metrics exporter at the agent as the metrics are being exported by spring micrometer exporter bridge
ENV OTEL_METRICS_EXPORTER="none"
ENV OTEL_SERVICE_NAME="micrometer-spring-otlp-registry-app-from-agent"
ENV OTEL_EXPORTER_OTLP_TRACES_ENDPOINT="http://otel-collector.docker:4317"

ENTRYPOINT [ \
  "java", \
  "-javaagent:/app/opentelemetry-javaagent.jar", \
  "-jar", "/app/micrometer-spring-otlp-registry-app.jar" \
]
