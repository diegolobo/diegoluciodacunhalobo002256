# =============================================================================
# Rockstars API - Multi-stage Dockerfile
# =============================================================================

# -----------------------------------------------------------------------------
# Stage 1: Build
# -----------------------------------------------------------------------------
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy gradle wrapper and configuration
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle gradle.properties ./

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies (cached if no changes)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build the application
RUN ./gradlew build -x test --no-daemon

# -----------------------------------------------------------------------------
# Stage 2: Runtime
# -----------------------------------------------------------------------------
FROM registry.access.redhat.com/ubi9/openjdk-21:1.23

ENV LANGUAGE='en_US:en'

# Copy built application from build stage
COPY --from=build --chown=185 /app/build/quarkus-app/lib/ /deployments/lib/
COPY --from=build --chown=185 /app/build/quarkus-app/*.jar /deployments/
COPY --from=build --chown=185 /app/build/quarkus-app/app/ /deployments/app/
COPY --from=build --chown=185 /app/build/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080

USER 185

ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]
