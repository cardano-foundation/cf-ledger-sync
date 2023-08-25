package org.cardanofoundation.ledgersync.explorerconsumer.configuration.kafka;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.TopicBuilder;
//import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@Slf4j
//@EnableKafka
@Profile("!test-integration & !test-unit")
public class KafkaConfiguration {

//  private final KafkaProperties kafkaProperties;
//
//  @Autowired
//  public KafkaConfiguration(KafkaProperties kafkaProperties) {
//    this.kafkaProperties = kafkaProperties;
//  }
//
//  @Bean
//  public KafkaAdmin kafkaAdmin() {
//    var configs = kafkaAdminConfigs(kafkaProperties);
//
//    var kafkaAdmin = new KafkaAdmin(configs);
//
//    if (Boolean.TRUE.equals(kafkaProperties.getAutoCreateTopics())) {
//
//      var topicBuilders = buildTopics(kafkaProperties);
//
//      kafkaAdmin.createOrModifyTopics(topicBuilders.toArray(NewTopic[]::new));
//    }
//
//    return kafkaAdmin;
//  }
//
//  private Set<NewTopic> buildTopics(KafkaProperties kafkaProperties) {
//    var topics = kafkaProperties.getTopics();
//
//    var newTopics = new HashSet<NewTopic>();
//    topics.forEach((k, v) -> {
//      var newTopic = TopicBuilder.name(v.getName())
//          .partitions(v.getPartitions())
//          .replicas(v.getReplicationFactor())
//          .configs(v.getConfigs())
//          .compact()
//          .build();
//      newTopics.add(newTopic);
//    });
//    return newTopics;
//
//  }
//
//  private Map<String, Object> kafkaAdminConfigs(KafkaProperties kafkaProperties) {
//    var admin = kafkaProperties.getAdmin();
//    var bootstrapServers = admin.getBootstrapServers();
//
//    var configs = new HashMap<String, Object>();
//
//    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//
//    return configs;
//  }
}
