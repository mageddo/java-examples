FROM openjdk:17

WORKDIR /app
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.30.0/opentelemetry-javaagent.jar /app/opentelemetry-javaagent.jar
COPY ./build/libs/otlp-api-spring-javaagent-app.jar /app/otlp-api-spring-javaagent-app.jar

ENV OTEL_SERVICE_NAME="coffee-maker-checkout"
ENV OTEL_TRACES_SAMPLER="always_on"
ENV OTEL_EXPORTER_OTLP_ENDPOINT=http://otel-collector.docker:4317

ENTRYPOINT [ \
  "java", "-javaagent:/app/opentelemetry-javaagent.jar", \
  "-jar", "/app/otlp-api-spring-javaagent-app.jar" \
]
#  "-Dotel.javaagent.configuration-file=/app/opentelemetry.properties", \
