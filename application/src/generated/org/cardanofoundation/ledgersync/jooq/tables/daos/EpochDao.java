/*
 * This file is generated by jOOQ.
 */
package org.cardanofoundation.ledgersync.jooq.tables.daos;


import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.cardanofoundation.ledgersync.jooq.AbstractSpringDAOImpl;
import org.cardanofoundation.ledgersync.jooq.tables.Epoch;
import org.cardanofoundation.ledgersync.jooq.tables.records.EpochRecord;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Repository
public class EpochDao extends AbstractSpringDAOImpl<EpochRecord, org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch, Long> {

    /**
     * Create a new EpochDao without any configuration
     */
    public EpochDao() {
        super(Epoch.EPOCH, org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch.class);
    }

    /**
     * Create a new EpochDao with an attached configuration
     */
    @Autowired
    public EpochDao(Configuration configuration) {
        super(Epoch.EPOCH, org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch.class, configuration);
    }

    @Override
    public Long getId(org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchRangeOfId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(Epoch.EPOCH.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchById(Long... values) {
        return fetch(Epoch.EPOCH.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch fetchOneById(Long value) {
        return fetchOne(Epoch.EPOCH.ID, value);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public Optional<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchOptionalById(Long value) {
        return fetchOptional(Epoch.EPOCH.ID, value);
    }

    /**
     * Fetch records that have <code>blk_count BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchRangeOfBlkCount(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Epoch.EPOCH.BLK_COUNT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>blk_count IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchByBlkCount(Integer... values) {
        return fetch(Epoch.EPOCH.BLK_COUNT, values);
    }

    /**
     * Fetch records that have <code>end_time BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchRangeOfEndTime(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Epoch.EPOCH.END_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>end_time IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchByEndTime(LocalDateTime... values) {
        return fetch(Epoch.EPOCH.END_TIME, values);
    }

    /**
     * Fetch records that have <code>fees BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchRangeOfFees(BigInteger lowerInclusive, BigInteger upperInclusive) {
        return fetchRange(Epoch.EPOCH.FEES, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>fees IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchByFees(BigInteger... values) {
        return fetch(Epoch.EPOCH.FEES, values);
    }

    /**
     * Fetch records that have <code>max_slot BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchRangeOfMaxSlot(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Epoch.EPOCH.MAX_SLOT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>max_slot IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchByMaxSlot(Integer... values) {
        return fetch(Epoch.EPOCH.MAX_SLOT, values);
    }

    /**
     * Fetch records that have <code>no BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchRangeOfNo(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Epoch.EPOCH.NO, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>no IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchByNo(Integer... values) {
        return fetch(Epoch.EPOCH.NO, values);
    }

    /**
     * Fetch a unique record that has <code>no = value</code>
     */
    public org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch fetchOneByNo(Integer value) {
        return fetchOne(Epoch.EPOCH.NO, value);
    }

    /**
     * Fetch a unique record that has <code>no = value</code>
     */
    public Optional<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchOptionalByNo(Integer value) {
        return fetchOptional(Epoch.EPOCH.NO, value);
    }

    /**
     * Fetch records that have <code>out_sum BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchRangeOfOutSum(BigInteger lowerInclusive, BigInteger upperInclusive) {
        return fetchRange(Epoch.EPOCH.OUT_SUM, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>out_sum IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchByOutSum(BigInteger... values) {
        return fetch(Epoch.EPOCH.OUT_SUM, values);
    }

    /**
     * Fetch records that have <code>start_time BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchRangeOfStartTime(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Epoch.EPOCH.START_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>start_time IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchByStartTime(LocalDateTime... values) {
        return fetch(Epoch.EPOCH.START_TIME, values);
    }

    /**
     * Fetch records that have <code>tx_count BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchRangeOfTxCount(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Epoch.EPOCH.TX_COUNT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>tx_count IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchByTxCount(Integer... values) {
        return fetch(Epoch.EPOCH.TX_COUNT, values);
    }

    /**
     * Fetch records that have <code>era BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchRangeOfEra(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Epoch.EPOCH.ERA, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>era IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchByEra(Integer... values) {
        return fetch(Epoch.EPOCH.ERA, values);
    }

    /**
     * Fetch records that have <code>rewards_distributed BETWEEN lowerInclusive
     * AND upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchRangeOfRewardsDistributed(BigInteger lowerInclusive, BigInteger upperInclusive) {
        return fetchRange(Epoch.EPOCH.REWARDS_DISTRIBUTED, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>rewards_distributed IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.Epoch> fetchByRewardsDistributed(BigInteger... values) {
        return fetch(Epoch.EPOCH.REWARDS_DISTRIBUTED, values);
    }
}
