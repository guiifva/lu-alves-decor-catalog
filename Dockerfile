FROM eclipse-temurin:21-jdk AS build

WORKDIR /workspace

COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties* ./
COPY gradle ./gradle

RUN chmod +x gradlew \
    && ./gradlew --no-daemon --version \
    && ./gradlew --no-daemon dependencies || true

COPY . .
RUN chmod +x gradlew \
 && ./gradlew --no-daemon clean bootJar -x test \
 && bash -lc 'set -e; JAR=$(ls build/libs/*-SNAPSHOT.jar 2>/dev/null || ls build/libs/*.jar | grep -v plain | head -n1); cp "$JAR" /workspace/app.jar'

FROM eclipse-temurin:21-jdk AS otel

# OpenTelemetry Java agent (traces only), pinned and checksum-verified.
ARG OTEL_JAVAAGENT_VERSION=2.32.0
ARG OTEL_JAVAAGENT_SHA256=f787eb6c7f3d18e69a431e108a15278d25ee37f83d68b678f621e063f3988f82
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_JAVAAGENT_VERSION}/opentelemetry-javaagent.jar /otel/opentelemetry-javaagent.jar
RUN echo "${OTEL_JAVAAGENT_SHA256}  /otel/opentelemetry-javaagent.jar" | sha256sum -c - \
 && chmod 644 /otel/opentelemetry-javaagent.jar

FROM eclipse-temurin:21-jre

# OTEL_* defaults can be overridden at runtime (Coolify env); endpoint, sampler and resource
# attributes come from the environment. JAVA_OPTS stays free for app/JVM flags.
ENV TZ=Etc/UTC \
    JAVA_OPTS="" \
    JAVA_TOOL_OPTIONS="-javaagent:/otel/opentelemetry-javaagent.jar" \
    OTEL_SERVICE_NAME=dm-catalog \
    OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf \
    OTEL_METRICS_EXPORTER=none \
    OTEL_LOGS_EXPORTER=none

WORKDIR /app

COPY --from=otel /otel/opentelemetry-javaagent.jar /otel/opentelemetry-javaagent.jar
COPY --from=build /workspace/app.jar /app/app.jar

# Coolify passes the commit SHA as a build arg; used as the Sentry release fallback.
ARG SOURCE_COMMIT
ENV SOURCE_COMMIT=${SOURCE_COMMIT}

EXPOSE 8080
# Without OTEL_EXPORTER_OTLP_ENDPOINT the OTel SDK defaults to disabled (no export-error spam);
# an explicit OTEL_SDK_DISABLED always wins.
ENTRYPOINT ["sh", "-c", "[ -n \"${OTEL_EXPORTER_OTLP_ENDPOINT:-}\" ] || export OTEL_SDK_DISABLED=\"${OTEL_SDK_DISABLED:-true}\"; exec java $JAVA_OPTS -jar /app/app.jar"]
