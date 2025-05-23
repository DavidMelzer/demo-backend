# 1. Stage: Build the application using Maven and Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. Stage: Runtime mit Java 21 JDK (oder JRE für schlankeres Image)
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /work
COPY --from=build /app/target/quarkus-app/ .
CMD ["java", "-jar", "quarkus-run.jar"]
