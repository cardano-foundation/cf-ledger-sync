package org.cardanofoundation.ledgersync.explorerconsumer.config;

import org.springframework.boot.test.context.TestConfiguration;

import redis.embedded.RedisServer;

@TestConfiguration
public class RedisTestConfig {

  private static final RedisServer redisServer;

  static {
    redisServer = RedisServer.builder().port(6379).build();
    redisServer.start();
  }
}
