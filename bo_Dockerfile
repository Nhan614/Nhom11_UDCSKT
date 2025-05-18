# Runtime only - không build Maven trong container
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy JAR đã build từ local
COPY target/shareEdu-0.0.1-SNAPSHOT.jar app.jar

# Railway exposes a dynamic port via $PORT
EXPOSE 8080

# Giới hạn bộ nhớ JVM (rất quan trọng trên Railway Free)
ENV JAVA_OPTS="-Xmx350m -Xms128m -XX:MaxMetaspaceSize=96m -XX:+UseSerialGC"

ENTRYPOINT sh -c "java $JAVA_OPTS -jar app.jar --server.port=\$PORT"
