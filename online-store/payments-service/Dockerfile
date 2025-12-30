# Build stage
FROM gradle:8.11.1-jdk21 AS build
WORKDIR /app

# Copy project files (keeps Gradle cache effective inside the image layer)
COPY --chown=gradle:gradle . .

# Build fat Spring Boot jar (tests skipped for faster container build)
RUN gradle clean bootJar -x test --no-daemon

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the generated Spring Boot jar (there will be exactly one *-SNAPSHOT.jar by default)
COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
