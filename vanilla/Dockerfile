FROM openjdk:17

ADD build/libs/vanilla-all.jar /app.jar
ENTRYPOINT [ \
  "java", "-Xmx150m", "-Xms50m", "-XX:MetaspaceSize=128m", "-XX:+PrintCommandLineFlags", \
  "-XX:MaxRAM=536870912", \
  "-cp", "/app.jar", "vanillajavaexamples.resources.ThreadMemoryUsageMain" \
]
