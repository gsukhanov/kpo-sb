# Build stage
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy Gradle wrapper + build files first (better layer caching)
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts settings.gradle.kts ./

# Ensure wrapper is executable
RUN chmod +x gradlew

# Copy sources
COPY src/ src/

# Build fat jar (Spring Boot)
RUN ./gradlew bootJar -x test --no-daemon

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
