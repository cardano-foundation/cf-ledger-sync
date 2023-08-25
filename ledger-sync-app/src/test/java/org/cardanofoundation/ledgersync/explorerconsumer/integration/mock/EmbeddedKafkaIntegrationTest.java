package org.cardanofoundation.ledgersync.explorerconsumer.integration.mock;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import org.cardanofoundation.ledgersync.common.common.Block;
import org.cardanofoundation.ledgersync.common.common.BlockHeader;
import org.cardanofoundation.ledgersync.common.common.Epoch;
import org.cardanofoundation.ledgersync.common.common.Era;
import org.cardanofoundation.ledgersync.common.common.HeaderBody;
import org.cardanofoundation.ledgersync.common.common.kafka.CommonBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.config.RedisTestConfig;
import org.cardanofoundation.ledgersync.explorerconsumer.integration.kafka.KafkaConsumer;
import org.cardanofoundation.ledgersync.explorerconsumer.integration.kafka.KafkaProducer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@Profile("test-integration")
@ActiveProfiles({"test-integration", "redis-standalone"})
@EnableAutoConfiguration(exclude = RedisAutoConfiguration.class)
@SpringBootTest(classes = RedisTestConfig.class)
@EmbeddedKafka(partitions = 1,
    brokerProperties = {"listeners=PLAINTEXT://localhost:39999", "port=39999"})
@EnabledIf(value = "#{environment['spring.profiles.active'] == 'test-integration'}", loadContext = true)
@TestInstance(Lifecycle.PER_CLASS)
class EmbeddedKafkaIntegrationTest {

  @Autowired
  private KafkaConsumer consumer;

  @Autowired
  private KafkaProducer producer;

  @Value("${test.topic}")
  private String topic;

  @Test
  void givenEmbeddedKafkaBroker_whenSendingWithSimpleProducer_thenMessageReceived()
      throws Exception {
    CommonBlock data = Block.builder()
        .header(BlockHeader.builder()
            .headerBody(HeaderBody.builder()
                .blockBodyHash("2")
                .prevHash("2")
                .blockNumber(2)
                .blockBodySize(2)
                .slotId(Epoch.builder()
                    .slotId(0)
                    .slotOfEpoch(1)
                    .value(2)
                    .build())
                .build())
            .build())
        .build();
    data.setEraType(Era.ALONZO);
    producer.send(topic, data);

    boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
    Assertions.assertTrue(messageConsumed);
    Assertions.assertEquals(consumer.getPayload().toString(), data.toString());
  }
}
