package org.cardanofoundation.ledgersync.explorerconsumer.configuration.redis.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisTemplateConfig {

    /**
     * RedisTemplate configuration
     *
     * @return redisTemplate
     */
    @Bean
    @Autowired
    @Primary
    RedisTemplate<String, ?> // NOSONAR
    redisTemplate(final LettuceConnectionFactory lettuceConnectionFactory) {
        var redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        // Enable transaction support for Spring's @Transactional annotation
        redisTemplate.setEnableTransactionSupport(true);

        return redisTemplate;
    }

    /**
     * Config bean hashOperations
     *
     * @param redisTemplate bean
     * @param <HK>          hash key type
     * @param <V>           value type
     * @return bean hashOperations
     */
    @Bean
    <HK, V> HashOperations<String, HK, V> // NOSONAR
    hashOperations(final RedisTemplate<String, V> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * ListOperations bean configuration
     *
     * @param redisTemplate inject bean
     * @param <V>           value type
     * @return listOperations
     */
    @Bean
    <V> ListOperations<String, V> listOperations(final RedisTemplate<String, V> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * ZSetOperations configuration
     *
     * @param redisTemplate inject bean
     * @param <V>           value type
     * @return ZSetOperations<String, V>
     */
    @Bean
    <V> ZSetOperations<String, V> zSetOperations(final RedisTemplate<String, V> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    /**
     * SetOperations configuration
     *
     * @param redisTemplate inject bean
     * @param <V>           value type
     * @return SetOperations<String, V>
     */
    @Bean
    <V> SetOperations<String, V> setOperations(final RedisTemplate<String, V> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * ValueOperations configuration
     *
     * @param redisTemplate inject bean
     * @param <V>           value type
     * @return ValueOperations<String, V>
     */
    @Bean
    <V> ValueOperations<String, V> valueOperations(final RedisTemplate<String, V> redisTemplate) {
        return redisTemplate.opsForValue();
    }
}
