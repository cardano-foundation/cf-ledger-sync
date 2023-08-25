package org.cardanofoundation.ledgersync.explorerconsumer.configuration.redis;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
//TODO yaci
//@Configuration
//@EnableCaching
public class RedisCachingConfig implements CachingConfigurer {


    /**
     * Customize rules for generating keys
     *
     * @return KeyGenerator
     */
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            val sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            Arrays.stream(params).sequential().forEach(sb::append);
            log.info("call Redis cache Key : " + sb);
            return sb.toString();
        };
    }

    /**
     * Customize for redis cache manager
     *
     * @return RedisCacheManagerBuilderCustomizer
     */
    @Bean
    RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> builder.withCacheConfiguration("monolithic",
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(5)));
    }

    /**
     * Cache manager configuration
     *
     * @param redisConnectionFactory bean inject
     * @return CacheManager
     */
    @Bean
    CacheManager cacheManager(
            @Qualifier("jedisConnectionFactory") final RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.create(redisConnectionFactory);
    }

}
