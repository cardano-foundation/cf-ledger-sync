package org.cardanofoundation.ledgersync.explorerconsumer.configuration.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Slf4j
@Profile("!test-integration & !test-unit")
public class ProducerConfiguration {

//  public static final String JSON_SERIALIZER = "json-producer";
//  private final KafkaProperties kafkaProperties;
//
//  public ProducerConfiguration(KafkaProperties kafkaProperties) {
//    this.kafkaProperties = kafkaProperties;
//  }
//
//  @Bean
//  @Primary
//  public KafkaTemplate<?, ?> kafkaTemplate() {
//    return new KafkaTemplate<>(producerFactory());
//  }
//
//  @Bean
//  public ProducerFactory<String, Object> producerFactory() {
//    var jsonConfigs = kafkaProperties.getProducers().get(JSON_SERIALIZER);
//
//    return new DefaultKafkaProducerFactory<>(jsonProducerConfigs(jsonConfigs));
//  }
//
//  @SneakyThrows
//  private Map<String, Object> jsonProducerConfigs(KafkaProperties.ProducerConfig jsonConfigs) {
//    Map<String, Object> props;
//    props = new HashMap<>();
//    props.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
//        jsonConfigs.getBootstrapServers());
//    props.put(org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG,
//        jsonConfigs.getClientId());
//    props.put(org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG, jsonConfigs.getAcks());
//    props.put(
//        org.apache.kafka.clients.producer.ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
//        jsonConfigs.getMaxInFlightRequestsPerConnection());
//    props.put(org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG,
//        jsonConfigs.getRetries());
//    props.put(org.apache.kafka.clients.producer.ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,
//        jsonConfigs.getRequestTimeoutMs());
//    props.put(org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG,
//        jsonConfigs.getBatchSize());
//    props.put(org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG,
//        jsonConfigs.getLingerMs());
//    props.put(org.apache.kafka.clients.producer.ProducerConfig.BUFFER_MEMORY_CONFIG,
//        jsonConfigs.getBufferMemory());
//    props.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
//        Class.forName(jsonConfigs.getKeySerializer()));
//    props.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
//        Class.forName(jsonConfigs.getValueSerializer()));
//    props.put(org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,
//        jsonConfigs.getEnableIdempotence());
//
//    if (kafkaProperties.getAdmin().getUseSsl()) {
//      props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
//    }
//
//    return props;
//  }
}
