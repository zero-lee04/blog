# Build Stage
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

# Run Stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

ENV TZ=Asia/Seoul

ENTRYPOINT ["java", "-jar", "app.jar"]