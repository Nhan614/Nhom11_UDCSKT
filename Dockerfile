# Giai đoạn build
FROM maven:3-openjdk-17 AS build

WORKDIR /app

# Copy toàn bộ project
COPY . .

# Build và bỏ qua test, in ra file build trong target
RUN mvn clean package -Dmaven.test.skip=true && ls -la target

# Giai đoạn runtime
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy WAR đã build từ target
COPY --from=build /app/target/shareEdu-0.0.1-SNAPSHOT.war app.war

# Expose port cho ứng dụng Spring Boot
EXPOSE 8080

# Chạy WAR (chỉ hoạt động nếu WAR là Spring Boot executable)
ENTRYPOINT ["java", "-jar", "app.war"]

