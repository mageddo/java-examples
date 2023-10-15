FROM openjdk:17

WORKDIR /app
COPY ./build/libs/micrometer-spring-otlp-registry-app-*.jar /app/micrometer-spring-otlp-registry-app.jar

ENV OTEL_SERVICE_NAME="micrometer-spring-otlp-registry-app"
ENTRYPOINT [ \
  "java", \
  "-jar", "/app/micrometer-spring-otlp-registry-app.jar" \
]
