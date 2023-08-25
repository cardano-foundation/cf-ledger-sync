# Ledger Sync


## Build

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
