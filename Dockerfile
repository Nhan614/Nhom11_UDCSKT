# Giai đoạn build
FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -Dmaven.test.skip=true

# Giai đoạn runtime
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy file JAR đã build
COPY --from=build /app/target/shareEdu-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Railway sẽ gán PORT ngẫu nhiên qua biến môi trường $PORT)
EXPOSE 8080

# Tùy chỉnh bộ nhớ JVM (Railway có giới hạn RAM)
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Khởi chạy ứng dụng Spring Boot
ENTRYPOINT sh -c 'java $JAVA_OPTS -jar app.jar --server.port=$PORT'
