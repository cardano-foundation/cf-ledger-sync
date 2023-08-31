package org.cardanofoundation.ledgersync.explorerconsumer.integration.kafka;

import org.cardanofoundation.ledgersync.common.common.kafka.CommonBlock;
import java.util.concurrent.CountDownLatch;
import lombok.Getter;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Getter
@Profile("test-integration")
public class KafkaConsumer {
  private CountDownLatch latch = new CountDownLatch(1);
  private CommonBlock payload;

  //TODO -- refactor fix tests
//  @KafkaListener(topics = "${test.topic}")
//  public void receive(ConsumerRecord<String, CommonBlock> consumerRecord) {
//    payload = consumerRecord.value();
//    latch.countDown();
//  }

  public void resetLatch() {
    latch = new CountDownLatch(1);
  }
}
