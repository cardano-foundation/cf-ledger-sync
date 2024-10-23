FROM eclipse-temurin:21-jdk AS build
ARG GRADLE_BUILD_ARGS=clean build -x test
WORKDIR /app
COPY . /app
RUN ./gradlew ${GRADLE_BUILD_ARGS}

FROM eclipse-temurin:21-jdk AS ledger-sync
WORKDIR /app
COPY --from=build /app/application/build/libs/ledger-sync-application*.jar /app/ledger-sync-application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ledger-sync-application.jar"]

FROM eclipse-temurin:21-jdk AS scheduler
WORKDIR /app
COPY --from=build /app/scheduler-app/build/libs/ledger-sync-scheduler-app*.jar /app/ledger-sync-scheduler-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ledger-sync-scheduler-app.jar"]

FROM eclipse-temurin:21-jdk AS streamer
WORKDIR /app
COPY --from=build /app/streamer-app/build/libs/ledger-sync-streamer-app*.jar /app/ledger-sync-streamer-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ledger-sync-streamer-app.jar"]

FROM eclipse-temurin:21-jdk AS aggregation
WORKDIR /app
COPY --from=build /app/aggregation-app/build/libs/ledger-sync-aggregation-app*.jar /app/ledger-sync-aggregation-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ledger-sync-aggregation-app.jar"]
