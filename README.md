<div align="center">

[![Clean, Build](https://github.com/cardano-foundation/cf-ledger-sync/actions/workflows/build.yml/badge.svg)](https://github.com/cardano-foundation/cf-ledger-sync/actions/workflows/build.yml)
[![License](https://img.shields.io:/github/license/cardano-foundation/cf-ledger-sync?color=blue&label=license)](https://github.com/cardano-foundation/cf-ledger-sync/blob/master/LICENSE)

</div>

This repository contains the following applications:

1. **Ledger Sync:** The main indexer application that reads data from the Cardano blockchain and writes to a PostgreSQL database.

2. **Ledger Sync - Scheduler App:** An optional application for scheduling jobs in Ledger Sync. By default, specific Ledger Sync scheduled jobs are executed as part of the main Ledger Sync application, but you can also run them independently using this application.

3. **Ledger Sync - Streaming App:** This app reads data from a Cardano node and publishes blockchain data to messaging middleware like Kafka or RabbitMQ. It publishes blockchain data in the form of events. Various types of events can be published by the streaming app, and you can configure which events you want to publish. This is useful if you want to listen to blockchain events but build your own storage layer.

For more details about the Streaming App, please check [here](https://cardano-foundation.github.io/cf-ledger-sync/applications/streamer_app).

# Documentation

Check out [**Ledger Sync documentation site**](https://cardano-foundation.github.io/cf-ledger-sync/) for more information.

# Building from source

## Prerequisites

- Java 21
- Cardano Node or connection to a remote Cardano node
- PostgreSQL database

## Build JAR

```bash
./gradlew clean build -x test
```

## Contributing to Ledger Sync

We love your input! We want to make contributing to this project as easy and transparent as possible, whether it's:

- Reporting a bug
- Discussing the current state of the code
- Submitting a fix
- Proposing new features

Please check the details [here](CONTRIBUTING.md).
