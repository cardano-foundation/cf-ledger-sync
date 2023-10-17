FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . /app
RUN ./gradlew clean build -x test

FROM eclipse-temurin:21-jdk AS runtime
WORKDIR /app
COPY --from=build /app/application/build/libs/ledger-sync-application*SNAPSHOT.jar /app/ledger-sync-application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ledger-sync-application.jar"]
