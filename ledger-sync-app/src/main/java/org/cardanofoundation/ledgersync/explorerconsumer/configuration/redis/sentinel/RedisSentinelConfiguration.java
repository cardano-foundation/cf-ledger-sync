package org.cardanofoundation.ledgersync.explorerconsumer.configuration.redis.sentinel;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.explorerconsumer.configuration.redis.sentinel.RedisSentinelProperties.SentinelNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Profile("redis-sentinel")
public class RedisSentinelConfiguration {

    /**
     * Redis properties config
     */
    RedisSentinelProperties redisProperties;

    @Autowired
    RedisSentinelConfiguration(RedisSentinelProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    @Primary
    JedisPoolConfig poolConfig() {
        var jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setTestOnBorrow(redisProperties.getTestOnBorrow());
        jedisPoolConfig.setMaxTotal(redisProperties.getMaxTotal());
        jedisPoolConfig.setMaxIdle(redisProperties.getMaxIdle());
        jedisPoolConfig.setMinIdle(redisProperties.getMinIdle());
        jedisPoolConfig.setTestOnReturn(redisProperties.getTestOnReturn());
        jedisPoolConfig.setTestWhileIdle(redisProperties.getTestWhileIdle());
        return jedisPoolConfig;
    }


    @Bean
    @Primary
    org.springframework.data.redis.connection.RedisSentinelConfiguration sentinelConfig() {
        var sentinelConfig = new org.springframework.data.redis.connection.RedisSentinelConfiguration();

        sentinelConfig.master(redisProperties.getMaster());
        sentinelConfig.setSentinelPassword(RedisPassword.of(redisProperties.getPassword()));
        sentinelConfig.setDatabase(redisProperties.getDatabaseIndex());
        var sentinels = redisProperties.getSentinels()
                .stream()
                .map(getSentinelNodeRedisNodeFunction())
                .collect(Collectors.toSet());

        sentinelConfig.setSentinels(sentinels);
        return sentinelConfig;
    }

    private static Function<SentinelNode, RedisNode> getSentinelNodeRedisNodeFunction() {
        return sentinel -> new RedisNode(sentinel.getHost(), sentinel.getPort());
    }

    /**
     * jedis connection factory configuration
     *
     * @return JedisConnectionFactory
     */
    @Bean(name = "jedisConnectionFactory")
    @Autowired
    JedisConnectionFactory jedisConnectionFactory(
            org.springframework.data.redis.connection.RedisSentinelConfiguration sentinelConfig) {

        return new JedisConnectionFactory(sentinelConfig, poolConfig());
    }

    /**
     * Lettuce connection factory configuration
     *
     * @return LettuceConnectionFactory
     */
    @Bean(name = "lettuceConnectionFactory")
    @Autowired
    LettuceConnectionFactory lettuceConnectionFactory(
            org.springframework.data.redis.connection.RedisSentinelConfiguration sentinelConfig) {
        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
                .clientName(UUID.randomUUID().toString())
                .commandTimeout(Duration.ofMillis(redisProperties.getConnectTimeOut()))
                .build();

        return new LettuceConnectionFactory(sentinelConfig, clientConfiguration);
    }

}
