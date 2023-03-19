FROM openjdk:9-jre-slim

COPY ./build/libs/* /app/udp-server.jar
ENTRYPOINT ["java", "-jar", "/app/udp-server.jar"]
