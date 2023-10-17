FROM openjdk:17

ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.31.0/opentelemetry-javaagent.jar /app/opentelemetry-javaagent.jar
WORKDIR /app
COPY ./build/libs/micrometer-spring-otlp-javaagent-app.jar /app/

# para fins de instrumentação e mandar metricas da jvm e libs dela automaticamente
ENV OTEL_SERVICE_NAME="micrometer-spring-otlp-javaagent-app"
ENV OTEL_EXPORTER_OTLP_ENDPOINT=http://otel-collector.docker:4317
ENV OTEL_RESOURCE_ATTRIBUTES="service.namespace=com.mageddo,service.id=1231,service.version=1.0.0-alpha"

ENTRYPOINT [ \
  "java", \
  "-javaagent:/app/opentelemetry-javaagent.jar", \
  "-jar", "/app/micrometer-spring-otlp-javaagent-app.jar" \
]
