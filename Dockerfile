FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY . /app
RUN ./gradlew clean build -x test

FROM openjdk:17-jdk-slim AS runtime
WORKDIR /app
COPY --from=build application/build/libs/ledger-sync-application*SNAPSHOT.jar /app/ledger-sync-application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ledger-sync-application.jar"]
