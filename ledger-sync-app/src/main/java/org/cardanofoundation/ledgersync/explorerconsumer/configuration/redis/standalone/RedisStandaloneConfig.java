package org.cardanofoundation.ledgersync.explorerconsumer.configuration.redis.standalone;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Slf4j
@Configuration
@Profile("redis-standalone")
public class RedisStandaloneConfig {

    @Value("${spring.redis.standalone.host}")
    private String hostname;

    @Value("${spring.redis.standalone.port}")
    private Integer port;

    @Value("${spring.redis.standalone.password}")
    private String password;

    @Value("${spring.redis.standalone.useSsl}")
    private boolean useSsl;

    @Bean
    RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostname, port);
        redisStandaloneConfiguration.setPassword(password);
        return redisStandaloneConfiguration;
    }

    @Bean(name = "lettuceConnectionFactory")
    LettuceConnectionFactory lettuceConnectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration) {
        if (useSsl) {
            return new LettuceConnectionFactory(redisStandaloneConfiguration, LettuceClientConfiguration.builder().useSsl().build());
        } else {
            return new LettuceConnectionFactory(redisStandaloneConfiguration);
        }
    }

    @Bean(name = "jedisConnectionFactory")
    @Autowired
    JedisConnectionFactory jedisConnectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration) {
        if (useSsl) {
            return new JedisConnectionFactory(redisStandaloneConfiguration, JedisClientConfiguration.builder().useSsl().build());

        } else {
            return new JedisConnectionFactory(redisStandaloneConfiguration);
        }
    }

}
