[![Clean, Build](https://github.com/cardano-foundation/cf-ledger-sync/actions/workflows/build.yml/badge.svg)](https://github.com/cardano-foundation/cf-ledger-sync/actions/workflows/build.yml)

This repository contains the following applications:

1. **Ledger Sync:** This is the main indexer application that reads data from the Cardano blockchain and writes to a PostgreSQL database.

2. **Ledger Sync - Scheduler App:** This is an optional application for scheduling jobs in Ledger Sync. By default, specific Ledger Sync schedule jobs are executed as part of the main Ledger Sync application, but you can also run them independently with this application. 

3. **Ledger Sync - Streaming App:** This app reads data from a Cardano node and publishes blockchain data to messaging middleware like Kafka or RabbitMQ. It publishes blockchain data in the form of events. There are various types of events that can be published by the streaming app, but you can configure which events you want to publish. This is useful when you want to listen to blockchain events but build your own storage layer.

For more details about Streaming App, please check [here](https://github.com/cardano-foundation/cf-ledger-sync/tree/main/streamer-app)

# Ledger Sync

## Pre-requisites
- Java 21
- Cardano Node or connect to a remote Cardano node
- PostgreSQL DB

## Build Jar

```bash
./gradlew clean build -x test
```

## Update databse details

1. Edit ``config/application.properties`` to add datasource url, username & password.

```
spring.datasource.url=jdbc:postgresql://localhost:5432/<db>

spring.datasource.username=user

spring.datasource.password=

SCHEMA=<schema>
```

2. Run

```bash
java -jar application/build/libs/ledger-sync-application-<version>-SNAPSHOT.jar
```

## Docker Build & Run

Build the jar file first

```bash
./gradlew clean build -x test
```

Build docker image


```shell
docker build -t cardanofoundation/ledger-sync:<version> .
```

### Docker Run

Copy env.example to env

Edit ``env`` file with database and network details

```shell
docker run -p 8080:8080 --env-file env cardanofoundation/ledger-sync:<version>
```
