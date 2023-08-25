package org.cardanofoundation.ledgersync.explorerconsumer.unit.service.impl;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cardanofoundation.explorer.consumercommon.entity.Block;
import org.cardanofoundation.explorer.consumercommon.entity.Epoch;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.config.RedisTestConfig;
import org.cardanofoundation.ledgersync.explorerconsumer.configuration.redis.standalone.RedisStandaloneConfig;
import org.cardanofoundation.ledgersync.explorerconsumer.configuration.redis.template.RedisTemplateConfig;
import org.cardanofoundation.ledgersync.explorerconsumer.constant.ConsumerConstant;
import org.cardanofoundation.ledgersync.explorerconsumer.dto.cache.CurrentEpochObject;
import org.cardanofoundation.ledgersync.explorerconsumer.dto.cache.LatestTxObject;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.LatestTxInProjection;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.LatestTxOutProjection;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.UniqueAccountTxCountProjection;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.BlockRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.EpochRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.MultiAssetRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxInRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxOutRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.RedisAggregatedDataCachingServiceImpl;
import org.mockito.Mockito;
import org.testcontainers.shaded.org.apache.commons.lang.ArrayUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@ActiveProfiles({"test-unit", "redis-standalone","caching"})
@EnableAutoConfiguration(exclude = RedisAutoConfiguration.class)
@SpringBootTest(classes = {
    RedisTemplateConfig.class,
    RedisAggregatedDataCachingServiceImpl.class,
    RedisStandaloneConfig.class,
    RedisTestConfig.class
})
class RedisAggregatedDataCachingServiceImplTest {

  private static final String TEMP_PREFIX = "TEMP_";
  private static final String AGGREGATED_CACHE_KEY = "AGGREGATED_CACHE";
  private static final String BLOCK_COUNT_HASH_KEY = "TOTAL_BLOCK_COUNT";
  private static final String TX_COUNT_HASH_KEY = "TOTAL_TX_COUNT";
  private static final String TOKEN_COUNT_HASH_KEY = "TOTAL_TOKEN_COUNT";
  private static final String LATEST_TXS_HASH_KEY = "LATEST_TXS";
  private static final String CURRENT_EPOCH_HASH_KEY = "CURRENT_EPOCH";
  private static final String UNIQUE_ACCOUNTS_KEY = "UNIQUE_ACCOUNTS";
  private static final long ONE_EPOCH_PERIOD = 5L;

  @Autowired
  RedisTemplate<String, String> redisTemplate;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  TxRepository txRepository;

  @MockBean
  TxInRepository txInRepository;

  @MockBean
  TxOutRepository txOutRepository;

  @MockBean
  BlockRepository blockRepository;

  @MockBean
  EpochRepository epochRepository;

  @MockBean
  MultiAssetRepository multiAssetRepository;

  @MockBean
  Clock clock;

  @Autowired
  RedisAggregatedDataCachingServiceImpl victim;

  @Value("${SCHEMA}")
  String network;

  @BeforeEach
  void clearData() {
    Set<String> keys = redisTemplate.keys("*");
    if (!CollectionUtils.isEmpty(keys)) {
      redisTemplate.delete(keys);
    }
  }

  @Test
  @DisplayName("Initialize data test")
  void initializeDataTest() {
    final HashOperations<String, String, String> stringHashOperations = redisTemplate.opsForHash();
    final HashOperations<String, String, Integer> intHashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final UniqueAccountTxCountProjection uniqueAccountTxCountProjection =
        Mockito.mock(UniqueAccountTxCountProjection.class);
    final UniqueAccountTxCountProjection uniqueAccountTxCountProjection2 =
        Mockito.mock(UniqueAccountTxCountProjection.class);
    final LatestTxInProjection txInProjection = Mockito.mock(LatestTxInProjection.class);
    final LatestTxOutProjection txOutProjection = Mockito.mock(LatestTxOutProjection.class);
    final String fromAddress = "addr1wxmmuv2aufg48cqx37qmmvfysr4rm43rr4ajac333z7m37guzfn54";
    final String toAddress = "addr1wypk6usz8zq25md9hh69792snssf8cr54uryu5cvmy9a74cf43wj6";
    final String txHash = "5a30328c241158608cd3db295b4cf68f8213a98f564ada9c0ff1cc515dd1b7dd";
    final Epoch epoch = Mockito.mock(Epoch.class);
    final Block block = Mockito.mock(Block.class);
    final Tx tx = Mockito.mock(Tx.class);
    final long blockCount = 1;
    final long txCount = 1;
    final long tokenCount = 1;
    final long txId = 1L;
    final long blockNo = 1L;
    final int epochNo = 1;
    final int maxEpochSlot = 432000;
    final long slotNo = 1L;

    LocalDateTime nowLdt = LocalDateTime.now(ZoneOffset.UTC);
    Timestamp nowTimestamp = Timestamp.valueOf(nowLdt);
    Mockito.when(epoch.getNo()).thenReturn(epochNo);
    Mockito.when(epoch.getMaxSlot()).thenReturn(maxEpochSlot);
    Mockito.when(epoch.getStartTime()).thenReturn(nowTimestamp);
    Mockito.when(block.getBlockNo()).thenReturn(blockNo);
    Mockito.when(block.getEpochNo()).thenReturn(epochNo);
    Mockito.when(block.getSlotNo()).thenReturn(slotNo);
    Mockito.when(block.getTime()).thenReturn(nowTimestamp);
    Mockito.when(tx.getBlock()).thenReturn(block);
    Mockito.when(tx.getHash()).thenReturn(txHash);
    Mockito.when(tx.getId()).thenReturn(txId);
    Mockito.when(txInProjection.getTxId()).thenReturn(txId);
    Mockito.when(txInProjection.getFromAddress()).thenReturn(fromAddress);
    Mockito.when(txOutProjection.getTxId()).thenReturn(txId);
    Mockito.when(txOutProjection.getToAddress()).thenReturn(toAddress);
    Mockito.when(uniqueAccountTxCountProjection.getAccount())
        .thenReturn(fromAddress);
    Mockito.when(uniqueAccountTxCountProjection.getTxCount()).thenReturn(1);
    Mockito.when(uniqueAccountTxCountProjection2.getAccount())
        .thenReturn(toAddress);
    Mockito.when(uniqueAccountTxCountProjection2.getTxCount()).thenReturn(1);

    Mockito.when(blockRepository.count()).thenReturn(blockCount);
    Mockito.when(txRepository.count()).thenReturn(txCount);
    Mockito.when(multiAssetRepository.count()).thenReturn(tokenCount);
    Mockito.when(txRepository.findAllByOrderByBlockIdDescBlockIndexAsc(Mockito.any()))
        .thenReturn(List.of(tx));
    Mockito.when(txInRepository.findAllByTxInputIn(Mockito.anyCollection()))
        .thenReturn(List.of(txInProjection));
    Mockito.when(txOutRepository.findAllByTxIn(Mockito.anyCollection()))
        .thenReturn(List.of(txOutProjection));
    Mockito.when(epochRepository.findFirstByOrderByNoDesc()).thenReturn(Optional.of(epoch));
    Mockito.when(epochRepository.findUniqueAccountsInEpoch(Mockito.anyInt()))
        .thenReturn(List.of(uniqueAccountTxCountProjection, uniqueAccountTxCountProjection2));

    // Run post construct method (which triggers data initialization)
    Assertions.assertDoesNotThrow(victim::postConstruct);

    // Verify
    // Counters
    Assertions.assertEquals(
        String.valueOf(blockCount),
        stringHashOperations.get(redisKey, BLOCK_COUNT_HASH_KEY));
    Assertions.assertEquals(
        String.valueOf(txCount),
        stringHashOperations.get(redisKey, TX_COUNT_HASH_KEY));
    Assertions.assertEquals(
        String.valueOf(tokenCount),
        stringHashOperations.get(redisKey, TOKEN_COUNT_HASH_KEY));

    // Latest txs
    String latestTxsJson = stringHashOperations.get(redisKey, LATEST_TXS_HASH_KEY);
    Assertions.assertTrue(StringUtils.hasText(latestTxsJson));

    LatestTxObject[] latestTxs = jsonToValue(latestTxsJson, LatestTxObject[].class);
    Assertions.assertFalse(ArrayUtils.isEmpty(latestTxs));
    Assertions.assertEquals(1, latestTxs.length);

    LatestTxObject latestTxObject = latestTxs[0];
    Assertions.assertEquals(txHash, latestTxObject.getTxHash());
    Assertions.assertEquals(blockNo, latestTxObject.getBlockNo());
    Assertions.assertEquals(epochNo, latestTxObject.getEpochNo());
    Assertions.assertEquals(slotNo, latestTxObject.getSlotNo());
    Assertions.assertFalse(CollectionUtils.isEmpty(latestTxObject.getFromAddresses()));
    Assertions.assertEquals(1, latestTxObject.getFromAddresses().size());
    Assertions.assertTrue(latestTxObject.getFromAddresses().contains(fromAddress));
    Assertions.assertFalse(CollectionUtils.isEmpty(latestTxObject.getToAddresses()));
    Assertions.assertEquals(1, latestTxObject.getToAddresses().size());
    Assertions.assertTrue(latestTxObject.getToAddresses().contains(toAddress));

    // Current epoch stats
    String currentEpochJson = stringHashOperations.get(redisKey, CURRENT_EPOCH_HASH_KEY);
    CurrentEpochObject currentEpochObject = jsonToValue(currentEpochJson, CurrentEpochObject.class);
    Assertions.assertEquals(1, currentEpochObject.getEpochNo());
    Assertions.assertEquals(432000, currentEpochObject.getMaxEpochSlot());
    Assertions.assertEquals(nowLdt.toString(), currentEpochObject.getStartTime());
    Assertions.assertEquals(
        nowLdt.plusDays(ONE_EPOCH_PERIOD).toString(),
        currentEpochObject.getEndTime());

    // Unique accounts in epoch
    String uniqueAccountRedisKey = String.join(
        ConsumerConstant.UNDERSCORE,
        getRedisKey(UNIQUE_ACCOUNTS_KEY),
        String.valueOf(epochNo));
    Assertions.assertEquals(2, intHashOperations.size(uniqueAccountRedisKey));
    Assertions.assertEquals(1, intHashOperations.get(uniqueAccountRedisKey, fromAddress));
    Assertions.assertEquals(1, intHashOperations.get(uniqueAccountRedisKey, toAddress));

    Mockito.verify(blockRepository, Mockito.times(1)).count();
    Mockito.verifyNoMoreInteractions(blockRepository);
    Mockito.verify(multiAssetRepository, Mockito.times(1)).count();
    Mockito.verifyNoMoreInteractions(multiAssetRepository);
    Mockito.verify(txRepository, Mockito.times(1)).count();
    Mockito.verify(txRepository, Mockito.times(1))
        .findAllByOrderByBlockIdDescBlockIndexAsc(Mockito.any());
    Mockito.verifyNoMoreInteractions(txRepository);
    Mockito.verify(txInRepository, Mockito.times(1))
        .findAllByTxInputIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txInRepository);
    Mockito.verify(txOutRepository, Mockito.times(1))
        .findAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txOutRepository);
    Mockito.verify(epochRepository, Mockito.times(1))
        .findFirstByOrderByNoDesc();
    Mockito.verify(epochRepository, Mockito.times(1))
        .findUniqueAccountsInEpoch(Mockito.anyInt());
    Mockito.verifyNoMoreInteractions(epochRepository);
  }

  @Test
  @DisplayName("Should add block count successfully")
  void shouldAddBlockCountSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final int valueForTest = 1;

    // Add value
    Assertions.assertDoesNotThrow(() -> victim.addBlockCount(valueForTest));

    // Verify
    // Temp value should be "1"
    Assertions.assertEquals(
        String.valueOf(valueForTest),
        hashOperations.get(tempRedisKey, BLOCK_COUNT_HASH_KEY));

    // Real value should be null, because no commits were done
    Assertions.assertNull(hashOperations.get(redisKey, BLOCK_COUNT_HASH_KEY));
  }

  @Test
  @DisplayName("Should add block count with existing block count successfully")
  void shouldAddBlockCountWithExistingDataSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final int valueForTest = 1;

    // Prepare data
    hashOperations.put(redisKey, BLOCK_COUNT_HASH_KEY, String.valueOf(valueForTest));

    // Add value
    Assertions.assertDoesNotThrow(() -> victim.addBlockCount(valueForTest));

    // Verify
    // Temp value should be "2"
    Assertions.assertEquals(
        String.valueOf(valueForTest + 1),
        hashOperations.get(tempRedisKey, BLOCK_COUNT_HASH_KEY));

    // Real value should be 1, because no commits were done
    Assertions.assertEquals(
        String.valueOf(valueForTest),
        hashOperations.get(redisKey, BLOCK_COUNT_HASH_KEY));
  }

  @Test
  @DisplayName("Should add block count with existing data then commit successfully")
  void shouldAddBlockCountWithExistingDataThenCommitSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final int valueForTest = 1;

    // Prepare data
    hashOperations.put(redisKey, BLOCK_COUNT_HASH_KEY, String.valueOf(valueForTest));

    // Add value
    Assertions.assertDoesNotThrow(() -> victim.addBlockCount(valueForTest));

    // Verify
    // Temp value should be "2"
    Assertions.assertEquals(
        String.valueOf(valueForTest + 1),
        hashOperations.get(tempRedisKey, BLOCK_COUNT_HASH_KEY));

    // Real value should be 1, because no commits were done
    Assertions.assertEquals(
        String.valueOf(valueForTest),
        hashOperations.get(redisKey, BLOCK_COUNT_HASH_KEY));

    // Commit
    Assertions.assertDoesNotThrow(victim::commit);

    // Temp value should be null
    Assertions.assertNull(hashOperations.get(tempRedisKey, BLOCK_COUNT_HASH_KEY));

    // Real value should be 2, because we committed
    Assertions.assertEquals(
        String.valueOf(valueForTest + 1),
        hashOperations.get(redisKey, BLOCK_COUNT_HASH_KEY));
  }

  @Test
  @DisplayName("Should subtract block count successfully")
  void shouldSubtractBlockCountSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final int valueForTest = 1;

    // Subtract value
    Assertions.assertDoesNotThrow(() -> victim.subtractBlockCount(valueForTest));

    // Verify
    // Temp value should be "0", because minimum is always 0
    Assertions.assertEquals(
        String.valueOf(0),
        hashOperations.get(tempRedisKey, BLOCK_COUNT_HASH_KEY));

    // Real value should be null, because no commits were done
    Assertions.assertNull(hashOperations.get(redisKey, BLOCK_COUNT_HASH_KEY));
  }

  @Test
  @DisplayName("Should subtract block count with existing block count successfully")
  void shouldSubtractBlockCountWithExistingDataSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final int valueForTest = 1;

    // Prepare data
    hashOperations.put(redisKey, BLOCK_COUNT_HASH_KEY, String.valueOf(valueForTest));

    // Subtract value
    Assertions.assertDoesNotThrow(() -> victim.subtractBlockCount(valueForTest));

    // Verify
    // Temp value should be "0"
    Assertions.assertEquals(
        String.valueOf(0),
        hashOperations.get(tempRedisKey, BLOCK_COUNT_HASH_KEY));

    // Real value should be 1, because no commits were done
    Assertions.assertEquals(
        String.valueOf(valueForTest),
        hashOperations.get(redisKey, BLOCK_COUNT_HASH_KEY));
  }

  @Test
  @DisplayName("Should subtract block count with existing data then commit successfully")
  void shouldSubtractBlockCountWithExistingDataThenCommitSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final int valueForTest = 1;

    // Prepare data
    hashOperations.put(redisKey, BLOCK_COUNT_HASH_KEY, String.valueOf(valueForTest));

    // Add value
    Assertions.assertDoesNotThrow(() -> victim.subtractBlockCount(valueForTest));

    // Verify
    // Temp value should be "0"
    Assertions.assertEquals(
        String.valueOf(0),
        hashOperations.get(tempRedisKey, BLOCK_COUNT_HASH_KEY));

    // Real value should be 1, because no commits were done
    Assertions.assertEquals(
        String.valueOf(valueForTest),
        hashOperations.get(redisKey, BLOCK_COUNT_HASH_KEY));

    // Commit
    Assertions.assertDoesNotThrow(victim::commit);

    // Temp value should be null
    Assertions.assertNull(hashOperations.get(tempRedisKey, BLOCK_COUNT_HASH_KEY));

    // Real value should be 0, because we committed
    Assertions.assertEquals(
        String.valueOf(0),
        hashOperations.get(redisKey, BLOCK_COUNT_HASH_KEY));
  }

  @Test
  @DisplayName("Should add tx count successfully")
  void shouldAddTxCountSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final int valueForTest = 1;

    // Add value
    Assertions.assertDoesNotThrow(() -> victim.addTxCount(valueForTest));

    // Verify
    // Temp value should be "1"
    Assertions.assertEquals(
        String.valueOf(valueForTest),
        hashOperations.get(tempRedisKey, TX_COUNT_HASH_KEY));

    // Real value should be null, because no commits were done
    Assertions.assertNull(hashOperations.get(redisKey, TX_COUNT_HASH_KEY));
  }

  @Test
  @DisplayName("Should add tx count with existing tx count successfully")
  void shouldAddTxCountWithExistingDataSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final int valueForTest = 1;

    // Prepare data
    hashOperations.put(redisKey, TX_COUNT_HASH_KEY, String.valueOf(valueForTest));

    // Add value
    Assertions.assertDoesNotThrow(() -> victim.addTxCount(valueForTest));

    // Verify
    // Temp value should be "2"
    Assertions.assertEquals(
        String.valueOf(valueForTest + 1),
        hashOperations.get(tempRedisKey, TX_COUNT_HASH_KEY));

    // Real value should be 1, because no commits were done
    Assertions.assertEquals(
        String.valueOf(valueForTest),
        hashOperations.get(redisKey, TX_COUNT_HASH_KEY));
  }

  @Test
  @DisplayName("Should add tx count with existing data then commit successfully")
  void shouldAddTxCountWithExistingDataThenCommitSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final int valueForTest = 1;

    // Prepare data
    hashOperations.put(redisKey, TX_COUNT_HASH_KEY, String.valueOf(valueForTest));

    // Add value
    Assertions.assertDoesNotThrow(() -> victim.addTxCount(valueForTest));

    // Verify
    // Temp value should be "2"
    Assertions.assertEquals(
        String.valueOf(valueForTest + 1),
        hashOperations.get(tempRedisKey, TX_COUNT_HASH_KEY));

    // Real value should be 1, because no commits were done
    Assertions.assertEquals(
        String.valueOf(valueForTest),
        hashOperations.get(redisKey, TX_COUNT_HASH_KEY));

    // Commit
    Assertions.assertDoesNotThrow(victim::commit);

    // Temp value should be null
    Assertions.assertNull(hashOperations.get(tempRedisKey, TX_COUNT_HASH_KEY));

    // Real value should be 2, because we committed
    Assertions.assertEquals(
        String.valueOf(valueForTest + 1),
        hashOperations.get(redisKey, TX_COUNT_HASH_KEY));
  }

  @Test
  @DisplayName("Should subtract tx count successfully")
  void shouldSubtractTxCountSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final int valueForTest = 1;

    // Subtract value
    Assertions.assertDoesNotThrow(() -> victim.subtractTxCount(valueForTest));

    // Verify
    // Temp value should be "0", because minimum is always 0
    Assertions.assertEquals(
        String.valueOf(0),
        hashOperations.get(tempRedisKey, TX_COUNT_HASH_KEY));

    // Real value should be null, because no commits were done
    Assertions.assertNull(hashOperations.get(redisKey, TX_COUNT_HASH_KEY));
  }

  @Test
  @DisplayName("Should subtract tx count with existing tx count successfully")
  void shouldSubtractTxCountWithExistingDataSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final int valueForTest = 1;

    // Prepare data
    hashOperations.put(redisKey, TX_COUNT_HASH_KEY, String.valueOf(valueForTest));

    // Subtract value
    Assertions.assertDoesNotThrow(() -> victim.subtractTxCount(valueForTest));

    // Verify
    // Temp value should be "0"
    Assertions.assertEquals(
        String.valueOf(0),
        hashOperations.get(tempRedisKey, TX_COUNT_HASH_KEY));

    // Real value should be 1, because no commits were done
    Assertions.assertEquals(
        String.valueOf(valueForTest),
        hashOperations.get(redisKey, TX_COUNT_HASH_KEY));
  }

  @Test
  @DisplayName("Should subtract tx count with existing data then commit successfully")
  void shouldSubtractTxCountWithExistingDataThenCommitSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final int valueForTest = 1;

    // Prepare data
    hashOperations.put(redisKey, TX_COUNT_HASH_KEY, String.valueOf(valueForTest));

    // Add value
    Assertions.assertDoesNotThrow(() -> victim.subtractTxCount(valueForTest));

    // Verify
    // Temp value should be "0"
    Assertions.assertEquals(
        String.valueOf(0),
        hashOperations.get(tempRedisKey, TX_COUNT_HASH_KEY));

    // Real value should be 1, because no commits were done
    Assertions.assertEquals(
        String.valueOf(valueForTest),
        hashOperations.get(redisKey, TX_COUNT_HASH_KEY));

    // Commit
    Assertions.assertDoesNotThrow(victim::commit);

    // Temp value should be null
    Assertions.assertNull(hashOperations.get(tempRedisKey, TX_COUNT_HASH_KEY));

    // Real value should be 0, because we committed
    Assertions.assertEquals(
        String.valueOf(0),
        hashOperations.get(redisKey, TX_COUNT_HASH_KEY));
  }

  @Test
  @DisplayName("Should add token count successfully")
  void shouldAddTokenCountSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final int valueForTest = 1;

    // Add value
    Assertions.assertDoesNotThrow(() -> victim.addTokenCount(valueForTest));

    // Verify
    // Temp value should be "1"
    Assertions.assertEquals(
        String.valueOf(valueForTest),
        hashOperations.get(tempRedisKey, TOKEN_COUNT_HASH_KEY));

    // Real value should be null, because no commits were done
    Assertions.assertNull(hashOperations.get(redisKey, TOKEN_COUNT_HASH_KEY));
  }

  @Test
  @DisplayName("Should add token count with existing token count successfully")
  void shouldAddTokenCountWithExistingDataSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final int valueForTest = 1;

    // Prepare data
    hashOperations.put(redisKey, TOKEN_COUNT_HASH_KEY, String.valueOf(valueForTest));

    // Add value
    Assertions.assertDoesNotThrow(() -> victim.addTokenCount(valueForTest));

    // Verify
    // Temp value should be "2"
    Assertions.assertEquals(
        String.valueOf(valueForTest + 1),
        hashOperations.get(tempRedisKey, TOKEN_COUNT_HASH_KEY));

    // Real value should be 1, because no commits were done
    Assertions.assertEquals(
        String.valueOf(valueForTest),
        hashOperations.get(redisKey, TOKEN_COUNT_HASH_KEY));
  }

  @Test
  @DisplayName("Should add token count with existing data then commit successfully")
  void shouldAddTokenCountWithExistingDataThenCommitSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final int valueForTest = 1;

    // Prepare data
    hashOperations.put(redisKey, TOKEN_COUNT_HASH_KEY, String.valueOf(valueForTest));

    // Add value
    Assertions.assertDoesNotThrow(() -> victim.addTokenCount(valueForTest));

    // Verify
    // Temp value should be "2"
    Assertions.assertEquals(
        String.valueOf(valueForTest + 1),
        hashOperations.get(tempRedisKey, TOKEN_COUNT_HASH_KEY));

    // Real value should be 1, because no commits were done
    Assertions.assertEquals(
        String.valueOf(valueForTest),
        hashOperations.get(redisKey, TOKEN_COUNT_HASH_KEY));

    // Commit
    Assertions.assertDoesNotThrow(victim::commit);

    // Temp value should be null
    Assertions.assertNull(hashOperations.get(tempRedisKey, TOKEN_COUNT_HASH_KEY));

    // Real value should be 2, because we committed
    Assertions.assertEquals(
        String.valueOf(valueForTest + 1),
        hashOperations.get(redisKey, TOKEN_COUNT_HASH_KEY));
  }

  @Test
  @DisplayName("Should add unique account tx count successfully")
  void shouldAddUniqueAccountTxCountSuccessfullyTest() {
    final int epoch = 1;
    final int txCount = 1;
    final String account = "addr_test1vq939leq9h878n69n832rw0xz3jwcp4a04s743ah370mzvsms3r4y";
    final HashOperations<String, String, Integer> hashOperations = redisTemplate.opsForHash();
    final String tempKey = String.join(ConsumerConstant.UNDERSCORE,
        getTempRedisKey(UNIQUE_ACCOUNTS_KEY), String.valueOf(epoch));
    final String tempRedisKey = getRedisKey(tempKey);
    final String key = String.join(ConsumerConstant.UNDERSCORE,
        UNIQUE_ACCOUNTS_KEY, String.valueOf(epoch));
    final String redisKey = getRedisKey(key);

    // Add value
    Assertions.assertDoesNotThrow(() -> victim.addAccountTxCountAtEpoch(epoch, account, txCount));

    // Verify
    // Temp value should be "1"
    Assertions.assertEquals(txCount, hashOperations.get(tempRedisKey, account));

    // Real value should be null, because no commits were done
    Assertions.assertNull(hashOperations.get(redisKey, account));
  }

  @Test
  @DisplayName("Should add unique account tx count with existing data successfully")
  void shouldAddUniqueAccountTxCountWithExistingDataSuccessfullyTest() {
    final int epoch = 1;
    final int txCount = 1;
    final String account = "addr_test1vq939leq9h878n69n832rw0xz3jwcp4a04s743ah370mzvsms3r4y";
    final HashOperations<String, String, Integer> hashOperations = redisTemplate.opsForHash();
    final String tempKey = String.join(ConsumerConstant.UNDERSCORE,
        getTempRedisKey(UNIQUE_ACCOUNTS_KEY), String.valueOf(epoch));
    final String tempRedisKey = getRedisKey(tempKey);
    final String key = String.join(ConsumerConstant.UNDERSCORE,
        UNIQUE_ACCOUNTS_KEY, String.valueOf(epoch));
    final String redisKey = getRedisKey(key);

    // Prepare data
    hashOperations.put(redisKey, account, txCount);

    // Add value
    Assertions.assertDoesNotThrow(() -> victim.addAccountTxCountAtEpoch(epoch, account, txCount));

    // Verify
    // Temp value should be "2"
    Assertions.assertEquals(txCount + 1, hashOperations.get(tempRedisKey, account));

    // Real value should be 1, because no commits were done
    Assertions.assertEquals(txCount, hashOperations.get(redisKey, account));
  }

  @Test
  @DisplayName("Should add unique account tx count with existing data then commit successfully")
  void shouldAddUniqueAccountTxCountWithExistingDataThenCommitSuccessfullyTest() {
    final int epoch = 1;
    final int txCount = 1;
    final String account = "addr_test1vq939leq9h878n69n832rw0xz3jwcp4a04s743ah370mzvsms3r4y";
    final HashOperations<String, String, Integer> hashOperations = redisTemplate.opsForHash();
    final String tempKey = String.join(ConsumerConstant.UNDERSCORE,
        getTempRedisKey(UNIQUE_ACCOUNTS_KEY), String.valueOf(epoch));
    final String tempRedisKey = getRedisKey(tempKey);
    final String key = String.join(ConsumerConstant.UNDERSCORE,
        UNIQUE_ACCOUNTS_KEY, String.valueOf(epoch));
    final String redisKey = getRedisKey(key);

    // Prepare data
    hashOperations.put(redisKey, account, txCount);

    // Add value
    Assertions.assertDoesNotThrow(() -> victim.addAccountTxCountAtEpoch(epoch, account, txCount));

    // Verify
    // Temp value should be "2"
    Assertions.assertEquals(txCount + 1, hashOperations.get(tempRedisKey, account));

    // Real value should be 1, because no commits were done
    Assertions.assertEquals(txCount, hashOperations.get(redisKey, account));

    // Commit
    Assertions.assertDoesNotThrow(victim::commit);

    // Temp value should be null
    Assertions.assertNull(hashOperations.get(tempRedisKey, account));

    // Real value should be 2, because we committed
    Assertions.assertEquals(txCount + 1, hashOperations.get(redisKey, account));
  }

  @Test
  @DisplayName("Should subtract unique account tx count successfully")
  void shouldSubtractUniqueAccountTxCountSuccessfullyTest() {
    final int epoch = 1;
    final int txCount = 1;
    final String account = "addr_test1vq939leq9h878n69n832rw0xz3jwcp4a04s743ah370mzvsms3r4y";
    final HashOperations<String, String, Integer> hashOperations = redisTemplate.opsForHash();
    final String tempKey = String.join(ConsumerConstant.UNDERSCORE,
        getTempRedisKey(UNIQUE_ACCOUNTS_KEY), String.valueOf(epoch));
    final String tempRedisKey = getRedisKey(tempKey);
    final String key = String.join(ConsumerConstant.UNDERSCORE,
        UNIQUE_ACCOUNTS_KEY, String.valueOf(epoch));
    final String redisKey = getRedisKey(key);

    // Subtract value
    Assertions.assertDoesNotThrow(
        () -> victim.subtractAccountTxCountAtEpoch(epoch, account, txCount));

    // Verify
    // Temp value should be "0"
    Assertions.assertEquals(0, hashOperations.get(tempRedisKey, account));

    // Real value should be null, because no commits were done
    Assertions.assertNull(hashOperations.get(redisKey, account));
  }

  @Test
  @DisplayName("Should subtract unique account tx count with existing data successfully")
  void shouldSubtractUniqueAccountTxCountWithExistingDataSuccessfullyTest() {
    final int epoch = 1;
    final int txCount = 1;
    final int givenTxCount = 2;
    final String account = "addr_test1vq939leq9h878n69n832rw0xz3jwcp4a04s743ah370mzvsms3r4y";
    final HashOperations<String, String, Integer> hashOperations = redisTemplate.opsForHash();
    final String tempKey = String.join(ConsumerConstant.UNDERSCORE,
        getTempRedisKey(UNIQUE_ACCOUNTS_KEY), String.valueOf(epoch));
    final String tempRedisKey = getRedisKey(tempKey);
    final String key = String.join(ConsumerConstant.UNDERSCORE,
        UNIQUE_ACCOUNTS_KEY, String.valueOf(epoch));
    final String redisKey = getRedisKey(key);

    // Prepare data
    hashOperations.put(redisKey, account, givenTxCount);

    // Subtract value
    Assertions.assertDoesNotThrow(
        () -> victim.subtractAccountTxCountAtEpoch(epoch, account, txCount));

    // Verify
    // Temp value should be "1"
    Assertions.assertEquals(txCount, hashOperations.get(tempRedisKey, account));

    // Real value should be "2", because no commits were done
    Assertions.assertEquals(givenTxCount, hashOperations.get(redisKey, account));
  }

  @Test
  @DisplayName("Should subtract unique account tx count with existing data then commit successfully")
  void shouldSubtractUniqueAccountTxCountWithExistingDataThenCommitSuccessfullyTest() {
    final int epoch = 1;
    final int txCount = 1;
    final int givenTxCount = 2;
    final String account = "addr_test1vq939leq9h878n69n832rw0xz3jwcp4a04s743ah370mzvsms3r4y";
    final HashOperations<String, String, Integer> hashOperations = redisTemplate.opsForHash();
    final String tempKey = String.join(ConsumerConstant.UNDERSCORE,
        getTempRedisKey(UNIQUE_ACCOUNTS_KEY), String.valueOf(epoch));
    final String tempRedisKey = getRedisKey(tempKey);
    final String key = String.join(ConsumerConstant.UNDERSCORE,
        UNIQUE_ACCOUNTS_KEY, String.valueOf(epoch));
    final String redisKey = getRedisKey(key);

    // Prepare data
    hashOperations.put(redisKey, account, givenTxCount);

    // Subtract value
    Assertions.assertDoesNotThrow(
        () -> victim.subtractAccountTxCountAtEpoch(epoch, account, txCount));

    // Verify
    // Temp value should be "1"
    Assertions.assertEquals(txCount, hashOperations.get(tempRedisKey, account));

    // Real value should be "2", because no commits were done
    Assertions.assertEquals(givenTxCount, hashOperations.get(redisKey, account));

    // Commit
    Assertions.assertDoesNotThrow(victim::commit);

    // Temp value should be null
    Assertions.assertNull(hashOperations.get(tempRedisKey, account));

    // Real value should be 1, because we committed
    Assertions.assertEquals(txCount, hashOperations.get(redisKey, account));
  }

  @Test
  @DisplayName("Should save current epoch successfully")
  void shouldSaveCurrentEpochSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final Instant currentTimeInstant = Instant.now();
    final LocalDateTime currentTime = LocalDateTime.ofInstant(currentTimeInstant, ZoneOffset.UTC);
    final Epoch epoch = Mockito.mock(Epoch.class);
    final LocalDateTime epochStartTime = currentTime.minusDays(2);
    final Clock fixedClock = Clock.fixed(currentTimeInstant, ZoneOffset.UTC);

    Mockito.doReturn(fixedClock.instant()).when(clock).instant();
    Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
    Mockito.doReturn(fixedClock).when(clock).withZone(ZoneOffset.UTC);
    Mockito.when(epoch.getNo()).thenReturn(1);
    Timestamp epochStartTimestamp = Timestamp.valueOf(epochStartTime);
    Mockito.when(epoch.getStartTime()).thenReturn(epochStartTimestamp);
    Mockito.when(epoch.getMaxSlot()).thenReturn(432000);

    // Cache data
    Assertions.assertDoesNotThrow(() -> victim.saveCurrentEpoch(epoch));

    // Verify
    // Temp value should be what we saved
    final String json = hashOperations.get(tempRedisKey, CURRENT_EPOCH_HASH_KEY);
    CurrentEpochObject currentEpochObject = jsonToValue(json, CurrentEpochObject.class);
    Assertions.assertEquals(1, currentEpochObject.getEpochNo());
    Assertions.assertEquals(432000, currentEpochObject.getMaxEpochSlot());
    Assertions.assertEquals(epochStartTime.toString(), currentEpochObject.getStartTime());
    Assertions.assertEquals(
        epochStartTime.plusDays(ONE_EPOCH_PERIOD).toString(),
        currentEpochObject.getEndTime());

    // Real value should be null, because no commits were done
    Assertions.assertNull(hashOperations.get(redisKey, CURRENT_EPOCH_HASH_KEY));
  }

  @Test
  @DisplayName("Should save current epoch and commit successfully")
  void shouldSaveCurrentEpochAndCommitSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final Instant currentTimeInstant = Instant.now();
    final LocalDateTime currentTime = LocalDateTime.ofInstant(currentTimeInstant, ZoneOffset.UTC);
    final Epoch epoch = Mockito.mock(Epoch.class);
    final LocalDateTime epochStartTime = currentTime.minusDays(2);
    final Clock fixedClock = Clock.fixed(currentTimeInstant, ZoneOffset.UTC);

    Mockito.doReturn(fixedClock.instant()).when(clock).instant();
    Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
    Mockito.doReturn(fixedClock).when(clock).withZone(ZoneOffset.UTC);
    Mockito.when(epoch.getNo()).thenReturn(1);
    Timestamp epochStartTimestamp = Timestamp.valueOf(epochStartTime);
    Mockito.when(epoch.getStartTime()).thenReturn(epochStartTimestamp);
    Mockito.when(epoch.getMaxSlot()).thenReturn(432000);

    // Cache data
    Assertions.assertDoesNotThrow(() -> victim.saveCurrentEpoch(epoch));

    // Verify
    // Temp value should be what we saved
    String json = hashOperations.get(tempRedisKey, CURRENT_EPOCH_HASH_KEY);
    CurrentEpochObject currentEpochObject = jsonToValue(json, CurrentEpochObject.class);
    Assertions.assertEquals(1, currentEpochObject.getEpochNo());
    Assertions.assertEquals(432000, currentEpochObject.getMaxEpochSlot());
    Assertions.assertEquals(epochStartTime.toString(), currentEpochObject.getStartTime());
    Assertions.assertEquals(
        epochStartTime.plusDays(ONE_EPOCH_PERIOD).toString(),
        currentEpochObject.getEndTime());

    // Real value should be null, because no commits were done
    Assertions.assertNull(hashOperations.get(redisKey, CURRENT_EPOCH_HASH_KEY));

    // Commit
    Assertions.assertDoesNotThrow(victim::commit);

    // Temp value should be null
    Assertions.assertNull(hashOperations.get(tempRedisKey, CURRENT_EPOCH_HASH_KEY));

    // Real value should be what we saved, because we committed
    json = hashOperations.get(redisKey, CURRENT_EPOCH_HASH_KEY);
    currentEpochObject = jsonToValue(json, CurrentEpochObject.class);
    Assertions.assertEquals(1, currentEpochObject.getEpochNo());
    Assertions.assertEquals(432000, currentEpochObject.getMaxEpochSlot());
    Assertions.assertEquals(epochStartTime.toString(), currentEpochObject.getStartTime());
    Assertions.assertEquals(
        epochStartTime.plusDays(ONE_EPOCH_PERIOD).toString(),
        currentEpochObject.getEndTime());
  }

  @Test
  @DisplayName("Should skip saving latest txs if no txs were found")
  void shouldSkipSaveLatestTxsTest() {
    Mockito.when(txRepository.findAllByOrderByBlockIdDescBlockIndexAsc(Mockito.any()))
        .thenReturn(Collections.emptyList());

    Assertions.assertDoesNotThrow(victim::saveLatestTxs);

    Mockito.verify(txRepository, Mockito.times(1))
        .findAllByOrderByBlockIdDescBlockIndexAsc(Mockito.any());
    Mockito.verifyNoMoreInteractions(txRepository);
    Mockito.verifyNoInteractions(txInRepository);
    Mockito.verifyNoInteractions(txOutRepository);
  }

  @Test
  @DisplayName("Should save latest txs successfully")
  void shouldSaveLatestTxsSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final LatestTxInProjection txInProjection = Mockito.mock(LatestTxInProjection.class);
    final LatestTxOutProjection txOutProjection = Mockito.mock(LatestTxOutProjection.class);
    final String fromAddress = "addr1wxmmuv2aufg48cqx37qmmvfysr4rm43rr4ajac333z7m37guzfn54";
    final String toAddress = "addr1wypk6usz8zq25md9hh69792snssf8cr54uryu5cvmy9a74cf43wj6";
    final String txHash = "5a30328c241158608cd3db295b4cf68f8213a98f564ada9c0ff1cc515dd1b7dd";
    final Block block = Mockito.mock(Block.class);
    final Tx tx = Mockito.mock(Tx.class);
    final long txId = 1L;
    final long blockNo = 1L;
    final int epochNo = 1;
    final long slotNo = 1L;

    Timestamp now = Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC));
    Mockito.when(block.getBlockNo()).thenReturn(blockNo);
    Mockito.when(block.getEpochNo()).thenReturn(epochNo);
    Mockito.when(block.getSlotNo()).thenReturn(slotNo);
    Mockito.when(block.getTime()).thenReturn(now);
    Mockito.when(tx.getBlock()).thenReturn(block);
    Mockito.when(tx.getHash()).thenReturn(txHash);
    Mockito.when(tx.getId()).thenReturn(txId);
    Mockito.when(txInProjection.getTxId()).thenReturn(txId);
    Mockito.when(txInProjection.getFromAddress()).thenReturn(fromAddress);
    Mockito.when(txOutProjection.getTxId()).thenReturn(txId);
    Mockito.when(txOutProjection.getToAddress()).thenReturn(toAddress);

    Mockito.when(txRepository.findAllByOrderByBlockIdDescBlockIndexAsc(Mockito.any()))
        .thenReturn(List.of(tx));
    Mockito.when(txInRepository.findAllByTxInputIn(Mockito.anyCollection()))
        .thenReturn(List.of(txInProjection));
    Mockito.when(txOutRepository.findAllByTxIn(Mockito.anyCollection()))
        .thenReturn(List.of(txOutProjection));

    // Save temp data
    Assertions.assertDoesNotThrow(victim::saveLatestTxs);

    Mockito.verify(txRepository, Mockito.times(1))
        .findAllByOrderByBlockIdDescBlockIndexAsc(Mockito.any());
    Mockito.verifyNoMoreInteractions(txRepository);
    Mockito.verify(txInRepository, Mockito.times(1))
        .findAllByTxInputIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txInRepository);
    Mockito.verify(txOutRepository, Mockito.times(1))
        .findAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txOutRepository);

    // Get temp data and verify
    String latestTxsJson = hashOperations.get(tempRedisKey, LATEST_TXS_HASH_KEY);
    Assertions.assertTrue(StringUtils.hasText(latestTxsJson));

    LatestTxObject[] latestTxs = jsonToValue(latestTxsJson, LatestTxObject[].class);
    Assertions.assertFalse(ArrayUtils.isEmpty(latestTxs));
    Assertions.assertEquals(1, latestTxs.length);

    LatestTxObject latestTxObject = latestTxs[0];
    Assertions.assertEquals(txHash, latestTxObject.getTxHash());
    Assertions.assertEquals(blockNo, latestTxObject.getBlockNo());
    Assertions.assertEquals(epochNo, latestTxObject.getEpochNo());
    Assertions.assertEquals(slotNo, latestTxObject.getSlotNo());
    Assertions.assertFalse(CollectionUtils.isEmpty(latestTxObject.getFromAddresses()));
    Assertions.assertEquals(1, latestTxObject.getFromAddresses().size());
    Assertions.assertTrue(latestTxObject.getFromAddresses().contains(fromAddress));
    Assertions.assertFalse(CollectionUtils.isEmpty(latestTxObject.getToAddresses()));
    Assertions.assertEquals(1, latestTxObject.getToAddresses().size());
    Assertions.assertTrue(latestTxObject.getToAddresses().contains(toAddress));

    // Real value should be null, because no commits were done
    Assertions.assertNull(hashOperations.get(redisKey, LATEST_TXS_HASH_KEY));
  }

  @Test
  @DisplayName("Should save latest txs and commit successfully")
  void shouldSaveLatestTxsAndCommitSuccessfullyTest() {
    final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    final String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
    final String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
    final LatestTxInProjection txInProjection = Mockito.mock(LatestTxInProjection.class);
    final LatestTxOutProjection txOutProjection = Mockito.mock(LatestTxOutProjection.class);
    final String fromAddress = "addr1wxmmuv2aufg48cqx37qmmvfysr4rm43rr4ajac333z7m37guzfn54";
    final String toAddress = "addr1wypk6usz8zq25md9hh69792snssf8cr54uryu5cvmy9a74cf43wj6";
    final String txHash = "5a30328c241158608cd3db295b4cf68f8213a98f564ada9c0ff1cc515dd1b7dd";
    final Block block = Mockito.mock(Block.class);
    final Tx tx = Mockito.mock(Tx.class);
    final long txId = 1L;
    final long blockNo = 1L;
    final int epochNo = 1;
    final long slotNo = 1L;

    Timestamp now = Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC));
    Mockito.when(block.getBlockNo()).thenReturn(blockNo);
    Mockito.when(block.getEpochNo()).thenReturn(epochNo);
    Mockito.when(block.getSlotNo()).thenReturn(slotNo);
    Mockito.when(block.getTime()).thenReturn(now);
    Mockito.when(tx.getBlock()).thenReturn(block);
    Mockito.when(tx.getHash()).thenReturn(txHash);
    Mockito.when(tx.getId()).thenReturn(txId);
    Mockito.when(txInProjection.getTxId()).thenReturn(txId);
    Mockito.when(txInProjection.getFromAddress()).thenReturn(fromAddress);
    Mockito.when(txOutProjection.getTxId()).thenReturn(txId);
    Mockito.when(txOutProjection.getToAddress()).thenReturn(toAddress);

    Mockito.when(txRepository.findAllByOrderByBlockIdDescBlockIndexAsc(Mockito.any()))
        .thenReturn(List.of(tx));
    Mockito.when(txInRepository.findAllByTxInputIn(Mockito.anyCollection()))
        .thenReturn(List.of(txInProjection));
    Mockito.when(txOutRepository.findAllByTxIn(Mockito.anyCollection()))
        .thenReturn(List.of(txOutProjection));

    // Save temp data
    Assertions.assertDoesNotThrow(victim::saveLatestTxs);

    Mockito.verify(txRepository, Mockito.times(1))
        .findAllByOrderByBlockIdDescBlockIndexAsc(Mockito.any());
    Mockito.verifyNoMoreInteractions(txRepository);
    Mockito.verify(txInRepository, Mockito.times(1))
        .findAllByTxInputIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txInRepository);
    Mockito.verify(txOutRepository, Mockito.times(1))
        .findAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txOutRepository);

    // Get temp data and verify
    String latestTxsJson = hashOperations.get(tempRedisKey, LATEST_TXS_HASH_KEY);
    Assertions.assertTrue(StringUtils.hasText(latestTxsJson));

    LatestTxObject[] latestTxs = jsonToValue(latestTxsJson, LatestTxObject[].class);
    Assertions.assertFalse(ArrayUtils.isEmpty(latestTxs));
    Assertions.assertEquals(1, latestTxs.length);

    LatestTxObject latestTxObject = latestTxs[0];
    Assertions.assertEquals(txHash, latestTxObject.getTxHash());
    Assertions.assertEquals(blockNo, latestTxObject.getBlockNo());
    Assertions.assertEquals(epochNo, latestTxObject.getEpochNo());
    Assertions.assertEquals(slotNo, latestTxObject.getSlotNo());
    Assertions.assertFalse(CollectionUtils.isEmpty(latestTxObject.getFromAddresses()));
    Assertions.assertEquals(1, latestTxObject.getFromAddresses().size());
    Assertions.assertTrue(latestTxObject.getFromAddresses().contains(fromAddress));
    Assertions.assertFalse(CollectionUtils.isEmpty(latestTxObject.getToAddresses()));
    Assertions.assertEquals(1, latestTxObject.getToAddresses().size());
    Assertions.assertTrue(latestTxObject.getToAddresses().contains(toAddress));

    // Real value should be null, because no commits were done
    Assertions.assertNull(hashOperations.get(redisKey, LATEST_TXS_HASH_KEY));

    // Commit
    Assertions.assertDoesNotThrow(victim::commit);

    // Temp data should be null after committing
    Assertions.assertNull(hashOperations.get(tempRedisKey, LATEST_TXS_HASH_KEY));

    // Real value should be what we saved, because we committed
    latestTxsJson = hashOperations.get(redisKey, LATEST_TXS_HASH_KEY);
    Assertions.assertTrue(StringUtils.hasText(latestTxsJson));

    latestTxs = jsonToValue(latestTxsJson, LatestTxObject[].class);
    Assertions.assertFalse(ArrayUtils.isEmpty(latestTxs));
    Assertions.assertEquals(1, latestTxs.length);

    latestTxObject = latestTxs[0];
    Assertions.assertEquals(txHash, latestTxObject.getTxHash());
    Assertions.assertEquals(blockNo, latestTxObject.getBlockNo());
    Assertions.assertEquals(epochNo, latestTxObject.getEpochNo());
    Assertions.assertEquals(slotNo, latestTxObject.getSlotNo());
    Assertions.assertFalse(CollectionUtils.isEmpty(latestTxObject.getFromAddresses()));
    Assertions.assertEquals(1, latestTxObject.getFromAddresses().size());
    Assertions.assertTrue(latestTxObject.getFromAddresses().contains(fromAddress));
    Assertions.assertFalse(CollectionUtils.isEmpty(latestTxObject.getToAddresses()));
    Assertions.assertEquals(1, latestTxObject.getToAddresses().size());
    Assertions.assertTrue(latestTxObject.getToAddresses().contains(toAddress));
  }

  private String getRedisKey(String key) {
    return String.join(ConsumerConstant.UNDERSCORE, network.toUpperCase(), key);
  }

  private String getTempRedisKey(String key) {
    return TEMP_PREFIX + key;
  }

  private <T> T jsonToValue(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Cannot convert JSON to object", e);
    }
  }
}
