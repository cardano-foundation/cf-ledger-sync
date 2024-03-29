/*
 * This file is generated by jOOQ.
 */
package org.cardanofoundation.ledgersync.jooq.tables.daos;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.cardanofoundation.ledgersync.jooq.AbstractSpringDAOImpl;
import org.cardanofoundation.ledgersync.jooq.tables.RollbackHistory;
import org.cardanofoundation.ledgersync.jooq.tables.records.RollbackHistoryRecord;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Repository
public class RollbackHistoryDao extends AbstractSpringDAOImpl<RollbackHistoryRecord, org.cardanofoundation.ledgersync.jooq.tables.pojos.RollbackHistory, Long> {

    /**
     * Create a new RollbackHistoryDao without any configuration
     */
    public RollbackHistoryDao() {
        super(RollbackHistory.ROLLBACK_HISTORY, org.cardanofoundation.ledgersync.jooq.tables.pojos.RollbackHistory.class);
    }

    /**
     * Create a new RollbackHistoryDao with an attached configuration
     */
    @Autowired
    public RollbackHistoryDao(Configuration configuration) {
        super(RollbackHistory.ROLLBACK_HISTORY, org.cardanofoundation.ledgersync.jooq.tables.pojos.RollbackHistory.class, configuration);
    }

    @Override
    public Long getId(org.cardanofoundation.ledgersync.jooq.tables.pojos.RollbackHistory object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.RollbackHistory> fetchRangeOfId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(RollbackHistory.ROLLBACK_HISTORY.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.RollbackHistory> fetchById(Long... values) {
        return fetch(RollbackHistory.ROLLBACK_HISTORY.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public org.cardanofoundation.ledgersync.jooq.tables.pojos.RollbackHistory fetchOneById(Long value) {
        return fetchOne(RollbackHistory.ROLLBACK_HISTORY.ID, value);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public Optional<org.cardanofoundation.ledgersync.jooq.tables.pojos.RollbackHistory> fetchOptionalById(Long value) {
        return fetchOptional(RollbackHistory.ROLLBACK_HISTORY.ID, value);
    }

    /**
     * Fetch records that have <code>block_hash BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.RollbackHistory> fetchRangeOfBlockHash(String lowerInclusive, String upperInclusive) {
        return fetchRange(RollbackHistory.ROLLBACK_HISTORY.BLOCK_HASH, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>block_hash IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.RollbackHistory> fetchByBlockHash(String... values) {
        return fetch(RollbackHistory.ROLLBACK_HISTORY.BLOCK_HASH, values);
    }

    /**
     * Fetch records that have <code>block_no BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.RollbackHistory> fetchRangeOfBlockNo(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(RollbackHistory.ROLLBACK_HISTORY.BLOCK_NO, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>block_no IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.RollbackHistory> fetchByBlockNo(Long... values) {
        return fetch(RollbackHistory.ROLLBACK_HISTORY.BLOCK_NO, values);
    }

    /**
     * Fetch records that have <code>rollback_time BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.RollbackHistory> fetchRangeOfRollbackTime(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(RollbackHistory.ROLLBACK_HISTORY.ROLLBACK_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>rollback_time IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.RollbackHistory> fetchByRollbackTime(LocalDateTime... values) {
        return fetch(RollbackHistory.ROLLBACK_HISTORY.ROLLBACK_TIME, values);
    }

    /**
     * Fetch records that have <code>slot_no BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.RollbackHistory> fetchRangeOfSlotNo(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(RollbackHistory.ROLLBACK_HISTORY.SLOT_NO, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>slot_no IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.RollbackHistory> fetchBySlotNo(Long... values) {
        return fetch(RollbackHistory.ROLLBACK_HISTORY.SLOT_NO, values);
    }
}
