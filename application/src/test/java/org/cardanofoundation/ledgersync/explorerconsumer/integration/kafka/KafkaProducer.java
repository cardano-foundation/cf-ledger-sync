package org.cardanofoundation.ledgersync.explorerconsumer.integration.kafka;

import org.cardanofoundation.ledgersync.common.common.kafka.CommonBlock;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile("test-integration")
public class KafkaProducer {

    //TODO -- refactor fix test
//  @Autowired
//  private KafkaTemplate<String, Object> kafkaTemplate;
//
//  public void send(String topic, CommonBlock payload) {
//    CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic,
//        UUID.randomUUID().toString(), payload);
//    future.whenComplete((result, ex) -> {
//      if (ex != null) {
//        log.error(ex.getMessage());
//      } else {
//        log.info("send:{}", result.getProducerRecord().value());
//      }
//    });
//  }
}
