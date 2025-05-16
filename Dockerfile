FROM maven:3-openjdk-17 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/shareEdu-0.0.1-SNAPSHOT.jar app.jar

ENV PORT 8080

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=$PORT"]
