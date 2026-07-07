FROM gradle:9.5.1-jdk17-alpine AS build
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY gradlew ./

RUN chmod +x ./gradlew && ./gradlew dependencies --no-daemon

COPY src ./src
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:17-jre-focal
WORKDIR /app

RUN addgroup --system --gid 1001 appgroup && \
    adduser --system --uid 1001 --gid 1001 appuser

COPY --from=build /app/build/libs/*.jar app.jar

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]


