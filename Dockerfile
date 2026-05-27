FROM gradle:8.7-jdk17 AS build

WORKDIR /src
COPY . .

RUN chmod +x gradlew
RUN ./gradlew clean bootJar

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /src/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]