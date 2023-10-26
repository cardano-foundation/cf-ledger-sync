## Ledger Sync Streamer App

Ledger Sync Streamer app reads data from a Cardano node and publishes blockchain data to a messaging middleware like Kafka or RabbitMQ.
It publishes blockchain data in the form of events. There are various types of events that can be published by the streamer app, but you can configure which events you want to publish.

As this app uses Spring Cloud Stream, it can support other supported binders in Spring Cloud Stream with some minor changes. 
(https://spring.io/projects/spring-cloud-stream)

### Pre-requisites
To run the streamer app, you need to have following components:

1. Cardano Node or connect to a remote Cardano node
2. Database (PostgreSQL, MySQL, H2) : To store cursor/checkpoint data
3. Messaging middleware (Kafka, RabbitMQ) : To publish events

### Events

Events are published as JSON messages, and each event contains two fields:

- metadata: Contains metadata about the event, such as block number, slot number, era, etc.
- ``<payload>``: Contains the actual data of the event.

The supported events are:

- **blockEvent** - Shelley and Post Shelley Blocks data (Includes everything transactions, witnesses..)
- **rollbackEvent** - Rollback event with rollback point
- **byronMainBlockEvent** - Byron Main Block data
- **byronEbBlockEvent** - Byron Epoch Boundary Block data
- **blockHeaderEvent** - Shelley and Post Shelley Block Header data
- **genesisBlockEvent** - Genesis Block data
- **certificateEvent** - Certificate data in a block
- **transactionEvent** - Transaction data. One transactionEvent with all transactions in a block
- **auxDataEvent** - Auxiliary data in a block
- **mintBurnEvent** - Mint and Burn data in a block
- **scriptEvent** - Script data of all transactions in a block

### Channels

By default, the events are published to default channels, but you also have the flexibility to 
configure custom channels as needed.

- **blockEvent** - blockTopic
- **rollbackEvent** - rollbackTopic
- **byronMainBlockEvent** - byronMainBlockTopic
- **byronEbBlockEvent** - byronEbBlockTopic
- **blockHeaderEvent** - blockHeaderTopic
- **genesisBlockEvent** - genesisBlockTopic
- **certificateEvent** - certificateTopic
- **transactionEvent** - transactionTopic
- **auxDataEvent** - auxDataTopic
- **mintBurnEvent** - mintBurnTopic
- **scriptEvent** - scriptTopic

The default channel can be changed by changing the following properties in application.properties file or docker environment file.

```
spring.cloud.stream.bindings.<event_name>-out-0.destination=<topic_name>
```

For more details on different configuration options, please refer to the application-kafka.properties or application-rabbit.properties file in config folder.

## How to run (Jar file)

To run the streamer app, you need Java 21 or a newer version. 
After building the project, you can execute the JAR file using the following command.

To run with **Kafka** binder:

1. Go to top level directory of the project.
2. Edit ``config/application-kafka.properties`` file and provide required information for cardano node, datasource, kafka.
3. Run the streamer jar file with kafka profile.

```
java -jar -Dspring.profiles.active=kafka streamer-app/build/libs/ledger-sync-streamer-app-<version>.jar
```

To run with **Rabbit** binder:

1. Go to top level directory of the project.
2. Edit ``config/application-rabbit.properties`` file and provide required information for cardano node, datasource, RabbitMQ.
3. Run the streamer jar file with rabbit profile.

```
java -jar -Dspring.profiles.active=rabbit streamer-app/build/libs/ledger-sync-streamer-app-<version>.jar
```

**Important:** If you want to start the sync again from start, you need to delete the schema from the database and start the sync again.

## How to run (Docker)

There are two docker compose files available to run the streamer app with Kafka or RabbitMQ in **streamer-app** folder.

To run with **Kafka** :

```shell
docker compose -f docker-compose-kafka.yml up
```

To run with **RabbitMQ** :

```shell
docker compose -f docker-compose-rabbit.yml up
```

**Important:** If you want to start the sync again from start, you need to delete the schema from the database and start the sync again.


### To start sync from a specific points

Set the following properties in config file to start the sync from a specific point.

```
store.cardano.sync-start-slot=<absolute_slot>
store.cardano.sync-start-blockhash=<block_hash>
```

For docker environment, you can set the following environment variables.

```
STORE_CARDANO_SYNC-START-SLOT=<absolute_slot>
STORE_CARDANO_SYNC-START-BLOCKHASH=<block_hash>
```

### Kafka Configuration
The default message size in Kafka is 1 MB. To accommodate larger JSON payloads, which can exceed 1 MB in the case of some blocks, you will need to increase this value.

```
KAFKA_MESSAGE_MAX_BYTES=20000000
```
