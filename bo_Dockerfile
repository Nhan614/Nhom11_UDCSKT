# Giai đoạn build
FROM maven:3-openjdk-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -Dmaven.test.skip=true

# Giai đoạn runtime
FROM openjdk:17-jre-slim
WORKDIR /app

COPY --from=build /app/target/shareEdu-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Giảm bộ nhớ JVM để tránh bị kill trên Railway
ENV JAVA_OPTS="-Xmx256m -Xms128m -XX:MaxMetaspaceSize=64m -XX:+UseSerialGC -XX:+HeapDumpOnOutOfMemoryError"

ENTRYPOINT sh -c "java $JAVA_OPTS -jar app.jar --server.port=$PORT"
