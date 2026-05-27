FROM eclipse-temurin:24-jre AS build

COPY . /src

RUN test -f /src/build/libs/djcarl.jar || /src/gradlew --project-dir /src bootJar

FROM eclipse-temurin:24-jre

COPY --from=build /src/build/libs/djcarl.jar /app/djcarl.jar

CMD java -jar /app/djcarl.jar