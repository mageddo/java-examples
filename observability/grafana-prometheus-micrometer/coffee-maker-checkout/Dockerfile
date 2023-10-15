FROM openjdk:17

ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.30.0/opentelemetry-javaagent.jar /app/opentelemetry-javaagent.jar
WORKDIR /app
COPY ./build/libs/coffee-maker-checkout-micrometer.jar /app/

# para fins de instrumentação e mandar metricas da jvm e libs dela automaticamente
ENV OTEL_SERVICE_NAME="coffee-maker-checkout-micrometer"
ENV OTEL_EXPORTER_OTLP_ENDPOINT=http://otel-collector.docker:4317

ENTRYPOINT [ \
  "java", \
  "-javaagent:/app/opentelemetry-javaagent.jar", \
  "-jar", "/app/coffee-maker-checkout-micrometer.jar" \
]
