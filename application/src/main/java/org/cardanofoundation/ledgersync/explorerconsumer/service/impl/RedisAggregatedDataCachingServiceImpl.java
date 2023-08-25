package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.explorer.consumercommon.entity.Block;
import org.cardanofoundation.explorer.consumercommon.entity.Epoch;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.constant.ConsumerConstant;
import org.cardanofoundation.ledgersync.explorerconsumer.dto.cache.CurrentEpochObject;
import org.cardanofoundation.ledgersync.explorerconsumer.dto.cache.LatestTxObject;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.LatestTxInProjection;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.LatestTxOutProjection;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.UniqueAccountTxCountProjection;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.*;
import org.cardanofoundation.ledgersync.explorerconsumer.service.AggregatedDataCachingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Profile("caching")
public class RedisAggregatedDataCachingServiceImpl implements AggregatedDataCachingService {

    private static final String TEMP_PREFIX = "TEMP_";
    private static final String AGGREGATED_CACHE_KEY = "AGGREGATED_CACHE";
    private static final String BLOCK_COUNT_HASH_KEY = "TOTAL_BLOCK_COUNT";
    private static final String TX_COUNT_HASH_KEY = "TOTAL_TX_COUNT";
    private static final String TOKEN_COUNT_HASH_KEY = "TOTAL_TOKEN_COUNT";
    private static final String LATEST_TXS_HASH_KEY = "LATEST_TXS";
    private static final String CURRENT_EPOCH_HASH_KEY = "CURRENT_EPOCH";
    private static final String UNIQUE_ACCOUNTS_KEY = "UNIQUE_ACCOUNTS";
    private static final String TEMPORARY_KEYS = "TEMPORARY_KEYS";
    private static final long ONE_EPOCH_PERIOD = 5L;

    final RedisTemplate<String, String> redisTemplate;
    final ObjectMapper objectMapper;
    final TxRepository txRepository;
    final TxInRepository txInRepository;
    final TxOutRepository txOutRepository;
    final BlockRepository blockRepository;
    final EpochRepository epochRepository;
    final MultiAssetRepository multiAssetRepository;

    @Value("${SCHEMA}")
    String network;

    @PostConstruct
    @Transactional
    public void postConstruct() {
        redisTemplate.delete(getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY)));

        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        String redisKey = getRedisKey(TEMPORARY_KEYS);
        Set<String> keys = zSetOperations.range(redisKey, 0, -1);
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }

        redisTemplate.delete(redisKey);
        initializeData();
    }

    private void initializeData() {
        String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);

        long blockCount = blockRepository.count();
        redisTemplate.opsForHash().put(redisKey, BLOCK_COUNT_HASH_KEY, String.valueOf(blockCount));

        long txCount = txRepository.count();
        redisTemplate.opsForHash().put(redisKey, TX_COUNT_HASH_KEY, String.valueOf(txCount));

        long tokenCount = multiAssetRepository.count();
        redisTemplate.opsForHash().put(redisKey, TOKEN_COUNT_HASH_KEY, String.valueOf(tokenCount));

        String latestTxsJson = getLatestTxsJson();
        if (StringUtils.hasText(latestTxsJson)) {
            redisTemplate.opsForHash().put(redisKey, LATEST_TXS_HASH_KEY, latestTxsJson);
        }

        Optional<Epoch> currentEpochOptional = epochRepository.findFirstByOrderByNoDesc();
        currentEpochOptional.ifPresent(currentEpoch -> {
            CurrentEpochObject currentEpochObject = epochToCurrentEpochObject(currentEpoch);
            redisTemplate.opsForHash()
                    .put(redisKey, CURRENT_EPOCH_HASH_KEY, valueToJson(currentEpochObject));

            Integer epochNo = currentEpoch.getNo();
            Map<String, Integer> uniqueAccounts = epochRepository
                    .findUniqueAccountsInEpoch(epochNo)
                    .stream()
                    .collect(Collectors.toMap(
                            UniqueAccountTxCountProjection::getAccount,
                            UniqueAccountTxCountProjection::getTxCount
                    ));
            if (!CollectionUtils.isEmpty(uniqueAccounts)) {
                String uniqueAccountRedisKey = String.join(
                        ConsumerConstant.UNDERSCORE,
                        getRedisKey(UNIQUE_ACCOUNTS_KEY),
                        epochNo.toString());
                redisTemplate.opsForHash().putAll(uniqueAccountRedisKey, uniqueAccounts);
            }
        });
    }

    @Override
    public void addBlockCount(int extraBlockCount) {
        String redisValue = getRedisHashValue(BLOCK_COUNT_HASH_KEY);
        int blockCount = StringUtils.hasText(redisValue) ? Integer.parseInt(redisValue) : 0;
        blockCount += extraBlockCount;
        saveTempRedisHashValue(BLOCK_COUNT_HASH_KEY, String.valueOf(blockCount));
    }

    @Override
    public void subtractBlockCount(int excessBlockCount) {
        String redisValue = getRedisHashValue(BLOCK_COUNT_HASH_KEY);
        int blockCount = StringUtils.hasText(redisValue) ? Integer.parseInt(redisValue) : 0;
        blockCount = Math.max(blockCount - excessBlockCount, 0);
        saveTempRedisHashValue(BLOCK_COUNT_HASH_KEY, String.valueOf(blockCount));
    }

    @Override
    public void addTxCount(int extraTxCount) {
        String redisValue = getRedisHashValue(TX_COUNT_HASH_KEY);
        int txCount = StringUtils.hasText(redisValue) ? Integer.parseInt(redisValue) : 0;
        txCount += extraTxCount;
        saveTempRedisHashValue(TX_COUNT_HASH_KEY, String.valueOf(txCount));
    }

    @Override
    public void subtractTxCount(int excessTxCount) {
        String redisValue = getRedisHashValue(TX_COUNT_HASH_KEY);
        int txCount = StringUtils.hasText(redisValue) ? Integer.parseInt(redisValue) : 0;
        txCount = Math.max(txCount - excessTxCount, 0);
        saveTempRedisHashValue(TX_COUNT_HASH_KEY, String.valueOf(txCount));
    }

    @Override
    public void addTokenCount(int extraTokenCount) {
        String redisValue = getRedisHashValue(TOKEN_COUNT_HASH_KEY);
        int tokenCount = StringUtils.hasText(redisValue) ? Integer.parseInt(redisValue) : 0;
        tokenCount += extraTokenCount;
        saveTempRedisHashValue(TOKEN_COUNT_HASH_KEY, String.valueOf(tokenCount));
    }

    @Override
    public void addAccountTxCountAtEpoch(int epoch, String account, int extraTxCount) {
        Integer txCount = getUniqueAccountTxCount(epoch, account);
        if (Objects.isNull(txCount)) {
            txCount = 0;
        }

        txCount += extraTxCount;
        saveUniqueAccountTxCount(epoch, account, txCount);
    }

    @Override
    public void subtractAccountTxCountAtEpoch(int epoch, String account, int excessTxCount) {
        Integer txCount = getUniqueAccountTxCount(epoch, account);
        if (Objects.isNull(txCount)) {
            txCount = 0;
        }

        txCount = Math.max(txCount - excessTxCount, 0);
        saveUniqueAccountTxCount(epoch, account, txCount);
    }

    @Override
    public void saveLatestTxs() {
        String redisValue = getLatestTxsJson();
        if (StringUtils.hasText(redisValue)) {
            saveTempRedisHashValue(LATEST_TXS_HASH_KEY, redisValue);
        }
    }

    private String getLatestTxsJson() {
        // Find 10 latest txs
        Pageable pageable = PageRequest.of(0, 10);
        List<Tx> txs = txRepository.findAllByOrderByBlockIdDescBlockIndexAsc(pageable);

        // Skip if no txs were found
        if (CollectionUtils.isEmpty(txs)) {
            return null;
        }

        Map<Long, Set<String>> latestTxInsMap = txInRepository.findAllByTxInputIn(txs)
                .stream()
                .collect(Collectors.groupingBy(
                        LatestTxInProjection::getTxId,
                        Collectors.mapping(LatestTxInProjection::getFromAddress, Collectors.toSet())
                ));
        Map<Long, Set<String>> latestTxOutsMap = txOutRepository.findAllByTxIn(txs)
                .stream()
                .collect(Collectors.groupingBy(
                        LatestTxOutProjection::getTxId,
                        Collectors.mapping(LatestTxOutProjection::getToAddress, Collectors.toSet())
                ));

        List<LatestTxObject> latestTxs = new ArrayList<>();
        txs.forEach(tx -> {
            Block block = tx.getBlock();
            LatestTxObject latestTx = new LatestTxObject(
                    tx.getHash(), block.getBlockNo(),
                    block.getEpochNo(), block.getSlotNo(),
                    latestTxInsMap.get(tx.getId()),
                    latestTxOutsMap.get(tx.getId()),
                    block.getTime().toString()
            );
            latestTxs.add(latestTx);
        });

        return valueToJson(latestTxs);
    }

    @Override
    public void saveCurrentEpoch(Epoch currentEpoch) {
        CurrentEpochObject currentEpochObject = epochToCurrentEpochObject(currentEpoch);
        saveTempRedisHashValue(CURRENT_EPOCH_HASH_KEY, valueToJson(currentEpochObject));
    }

    private static CurrentEpochObject epochToCurrentEpochObject(Epoch currentEpoch) {
        LocalDateTime epochStartTime = currentEpoch.getStartTime().toLocalDateTime();
        return new CurrentEpochObject(
                currentEpoch.getNo(),
                currentEpoch.getMaxSlot(),
                epochStartTime.toString(),
                epochStartTime.plusDays(ONE_EPOCH_PERIOD).toString()
        );
    }

    @Override
    @Transactional
    public void commit() {
        // Commit aggregated cache data
        String tmpAggregatedRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
        Map<Object, Object> tmpDataEntries = redisTemplate.opsForHash().entries(tmpAggregatedRedisKey);
        redisTemplate.opsForHash().putAll(getRedisKey(AGGREGATED_CACHE_KEY), tmpDataEntries);

        // Commit unique accounts data
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        String tempKeysRedisKey = getRedisKey(TEMPORARY_KEYS);
        Set<String> keys = zSetOperations.range(tempKeysRedisKey, 0, -1);
        if (!CollectionUtils.isEmpty(keys)) {
            Set<String> uniqueAccountTempKeys = new HashSet<>();

            keys.stream()
                    .filter(redisKey -> redisKey.contains(UNIQUE_ACCOUNTS_KEY))
                    .forEach(redisKey -> {
                        String actualKey = redisKey.replace(TEMP_PREFIX, "");
                        Map<Object, Object> entries = redisTemplate.opsForHash().entries(redisKey);
                        redisTemplate.opsForHash().putAll(actualKey, entries);

                        // In case of rollbacks, if tx count is zero, it means this account has not made
                        // any transaction in this epoch at all, so remove it
                        Set<String> accountsWithZeroTxCount = entries.entrySet().stream()
                                .filter(entry -> ((Integer) entry.getValue()) == 0)
                                .map(entry -> entry.getKey().toString())
                                .collect(Collectors.toSet());
                        redisTemplate.opsForHash().delete(actualKey, accountsWithZeroTxCount);
                        uniqueAccountTempKeys.add(redisKey);
                    });

            // Delete temp unique accounts keys
            redisTemplate.delete(uniqueAccountTempKeys);
        }

        // Delete temp keys
        redisTemplate.delete(tempKeysRedisKey);
        redisTemplate.delete(tmpAggregatedRedisKey);
    }

    private Integer getUniqueAccountTxCount(int epoch, String account) {
        HashOperations<String, String, Integer> hashOperations = redisTemplate.opsForHash();

        // Get from both temp and real data
        String tempRedisKey = getTempRedisKey(UNIQUE_ACCOUNTS_KEY);
        tempRedisKey = String.join(ConsumerConstant.UNDERSCORE, tempRedisKey, String.valueOf(epoch));
        Integer txCount = hashOperations.get(getRedisKey(tempRedisKey), account);
        if (Objects.nonNull(txCount)) {
            return txCount;
        }

        String redisKey = String.join(ConsumerConstant.UNDERSCORE,
                UNIQUE_ACCOUNTS_KEY, String.valueOf(epoch));
        return hashOperations.get(getRedisKey(redisKey), account);
    }

    private void saveUniqueAccountTxCount(int epoch, String account, int txCount) {
        String key = String.join(ConsumerConstant.UNDERSCORE,
                getTempRedisKey(UNIQUE_ACCOUNTS_KEY), String.valueOf(epoch));
        String redisKey = getRedisKey(key);
        redisTemplate.opsForHash().put(redisKey, account, txCount);
        redisTemplate.opsForZSet().add(getRedisKey(TEMPORARY_KEYS), redisKey, 1);
    }

    private String getRedisHashValue(String hashKey) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();

        // Get from both temp and real data
        String tempRedisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
        String data = hashOperations.get(tempRedisKey, hashKey);
        if (StringUtils.hasText(data)) {
            return data;
        }

        String redisKey = getRedisKey(AGGREGATED_CACHE_KEY);
        return hashOperations.get(redisKey, hashKey);
    }

    private void saveTempRedisHashValue(String hashKey, String value) {
        String redisKey = getRedisKey(getTempRedisKey(AGGREGATED_CACHE_KEY));
        redisTemplate.opsForHash().put(redisKey, hashKey, value);
    }

    private String getRedisKey(String key) {
        return String.join(ConsumerConstant.UNDERSCORE, network.toUpperCase(), key);
    }

    private String getTempRedisKey(String key) {
        return TEMP_PREFIX + key;
    }

    private <T> String valueToJson(T value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot convert Redis value to JSON", e);
        }
    }
}
