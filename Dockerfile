# Giai đoạn build
FROM maven:3-openjdk-17 AS build

WORKDIR /app

# Copy toàn bộ project
COPY . .

# Build và bỏ qua test, in ra file build trong target
RUN mvn clean package -DskipTests && ls -la target

# Giai đoạn runtime
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy JAR đã build từ target
COPY --from=build /app/target/shareEdu-0.0.1-SNAPSHOT.jar app.jar

# Expose port cho ứng dụng Spring Boot
EXPOSE 8080

# Chạy JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
