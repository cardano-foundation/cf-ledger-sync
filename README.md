[![Clean, Build](https://github.com/cardano-foundation/cf-ledger-sync/actions/workflows/build.yml/badge.svg)](https://github.com/cardano-foundation/cf-ledger-sync/actions/workflows/build.yml)

# Ledger Sync

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
