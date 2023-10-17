FROM openjdk:17

WORKDIR /app
COPY ./build/libs/micrometer-spring-otlp-registry-app-*.jar /app/micrometer-spring-otlp-registry-app.jar
ENTRYPOINT [ \
  "java", \
  "-jar", "/app/micrometer-spring-otlp-registry-app.jar" \
]
