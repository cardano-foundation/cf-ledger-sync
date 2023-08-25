package org.cardanofoundation.ledgersync.explorerconsumer.configuration.redis.sentinel;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "redis-sentinel")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Profile("redis-sentinel")
public class RedisSentinelProperties {

    Integer databaseIndex;

    String master;

    String password;

    List<SentinelNode> sentinels;

    Boolean testOnBorrow;

    Integer maxTotal;

    Integer maxIdle;

    Integer minIdle;

    Boolean testOnReturn;

    Boolean testWhileIdle;

    Long connectTimeOut;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SentinelNode {

        String host;

        Integer port;

    }
}
