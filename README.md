# Ledger Sync


## Build Jar

```bash
./gradlew clean build -x test
```

## Update databse details

1. Edit ``config/application.properties`` to add datasource url, username & password.

```
spring.datasource.url=jdbc:postgresql://localhost:5432/<db>?currentSchema=<schema>

spring.datasource.username=user

spring.datasource.password=
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

Edit env file with database and network details

```shell
docker run -p 8080:8080 --env-file env cardanofoundation/ledger-sync:<version>
```
