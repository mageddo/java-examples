FROM openjdk:17

ADD build/libs/vanilla-all.jar /app.jar
ENTRYPOINT [ \
  "java", "-Xmx500m", "-Xms50m", "-Xmn50m", "-XX:MetaspaceSize=128m", "-XX:+PrintCommandLineFlags", \
  "-cp", "/app.jar", "vanillajavaexamples.resources.ThreadMemoryUsageMain" \
]
