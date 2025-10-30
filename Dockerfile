FROM eclipse-temurin:24-jdk AS build

WORKDIR /workspace

COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties* ./
COPY gradle ./gradle

RUN chmod +x gradlew \
    && ./gradlew --no-daemon --version \
    && ./gradlew --no-daemon dependencies || true

COPY . .
RUN ./gradlew --no-daemon clean bootJar -x test \
 && bash -lc 'set -e; JAR=$(ls build/libs/*-SNAPSHOT.jar 2>/dev/null || ls build/libs/*.jar | grep -v plain | head -n1); cp "$JAR" /workspace/app.jar'

FROM eclipse-temurin:24-jre

ENV TZ=Etc/UTC \
    JAVA_OPTS=""

WORKDIR /app

COPY --from=build /workspace/app.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
