FROM openjdk:17-jdk-slim AS runtime
WORKDIR /app
COPY application/build/libs/ledger-sync-application*.jar /app/ledger-sync-application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ledger-sync-application.jar"]
