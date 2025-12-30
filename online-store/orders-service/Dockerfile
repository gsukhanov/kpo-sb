# Build stage
FROM gradle:8.11.1-jdk21 AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle clean bootJar -x test --no-daemon

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
