FROM openjdk:17

WORKDIR /app
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.28.0/opentelemetry-javaagent.jar /app/opentelemetry-javaagent.jar
COPY ./build/libs/coffee-maker-checkout-all.jar /app/
COPY opentelemetry.properties /app/

#ENV JAVA_TOOL_OPTIONS="-javaagent:/app/opentelemetry-javaagent.jar"
ENV OTEL_SERVICE_NAME="coffee-maker-checkout"

ENTRYPOINT [ \
  "java", "-javaagent:/app/opentelemetry-javaagent.jar", \
  "-Dotel.javaagent.configuration-file=/app/opentelemetry.properties", \
  "-jar", "/app/coffee-maker-checkout-all.jar" \
]
