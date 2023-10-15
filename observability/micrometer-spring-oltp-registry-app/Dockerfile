FROM openjdk:17

WORKDIR /app
COPY ./build/libs/micrometer-spring-otlp-registry-app.jar /app/

ENV OTEL_SERVICE_NAME="micrometer-spring-otlp-registry-app"
ENV JAVA_TOOL_OPTIONS="-Dotlp.url=http://otel-collector.docker:4318/v1/metrics"

ENTRYPOINT [ \
  "java", \
  "-jar", "/app/micrometer-spring-otlp-registry-app.jar" \
]
