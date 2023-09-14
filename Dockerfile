FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY gradlew /app
COPY gradle /app/gradle
COPY build.gradle /app
COPY settings.gradle /app
COPY application/build.gradle /app/application/
COPY application/src /app/application/src
RUN ./gradlew clean build -x test


FROM openjdk:17-jdk-slim AS runtime
WORKDIR /app
COPY --from=build application/build/libs/ledger-sync-application*.jar /app/ledger-sync-application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ledger-sync-application.jar"]
