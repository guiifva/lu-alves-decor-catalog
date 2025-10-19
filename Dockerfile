# Stage 1: Build the application
FROM gradle:jdk24 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN gradle build --no-daemon

# Stage 2: Create the runtime image
FROM openjdk:24-jre-slim
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
