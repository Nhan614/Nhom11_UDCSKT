FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/shareEdu-0.0.1-SNAPSHOT.war app.war

EXPOSE 8080

ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.war --server.port=$PORT"]
