# тюнинг под нагрузку: JDK 17 -> 21
FROM gradle:9.5.1-jdk21-alpine AS build
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY gradlew ./

RUN chmod +x ./gradlew && ./gradlew dependencies --no-daemon

COPY src ./src
RUN gradle bootJar --no-daemon

# тюнинг под нагрузку: JDK 17 -> 21 (для 21 нет focal-тега, дефолтный 21-jre)
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN addgroup --system --gid 1001 appgroup && \
    adduser --system --uid 1001 --gid 1001 appuser

COPY --from=build /app/build/libs/*.jar app.jar

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]


