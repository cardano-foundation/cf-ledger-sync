/*
 * This file is generated by jOOQ.
 */
package org.cardanofoundation.ledgersync.jooq.tables.daos;


import java.util.List;
import java.util.Optional;

import org.cardanofoundation.ledgersync.jooq.AbstractSpringDAOImpl;
import org.cardanofoundation.ledgersync.jooq.tables.TxWitnesses;
import org.cardanofoundation.ledgersync.jooq.tables.records.TxWitnessesRecord;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Repository
public class TxWitnessesDao extends AbstractSpringDAOImpl<TxWitnessesRecord, org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses, Long> {

    /**
     * Create a new TxWitnessesDao without any configuration
     */
    public TxWitnessesDao() {
        super(TxWitnesses.TX_WITNESSES, org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses.class);
    }

    /**
     * Create a new TxWitnessesDao with an attached configuration
     */
    @Autowired
    public TxWitnessesDao(Configuration configuration) {
        super(TxWitnesses.TX_WITNESSES, org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses.class, configuration);
    }

    @Override
    public Long getId(org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses> fetchRangeOfId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(TxWitnesses.TX_WITNESSES.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses> fetchById(Long... values) {
        return fetch(TxWitnesses.TX_WITNESSES.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses fetchOneById(Long value) {
        return fetchOne(TxWitnesses.TX_WITNESSES.ID, value);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public Optional<org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses> fetchOptionalById(Long value) {
        return fetchOptional(TxWitnesses.TX_WITNESSES.ID, value);
    }

    /**
     * Fetch records that have <code>tx_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses> fetchRangeOfTxId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(TxWitnesses.TX_WITNESSES.TX_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>tx_id IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses> fetchByTxId(Long... values) {
        return fetch(TxWitnesses.TX_WITNESSES.TX_ID, values);
    }

    /**
     * Fetch records that have <code>key BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses> fetchRangeOfKey(String lowerInclusive, String upperInclusive) {
        return fetchRange(TxWitnesses.TX_WITNESSES.KEY, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>key IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses> fetchByKey(String... values) {
        return fetch(TxWitnesses.TX_WITNESSES.KEY, values);
    }

    /**
     * Fetch records that have <code>signature BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses> fetchRangeOfSignature(String lowerInclusive, String upperInclusive) {
        return fetchRange(TxWitnesses.TX_WITNESSES.SIGNATURE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>signature IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses> fetchBySignature(String... values) {
        return fetch(TxWitnesses.TX_WITNESSES.SIGNATURE, values);
    }

    /**
     * Fetch records that have <code>index_arr BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses> fetchRangeOfIndexArr(Integer[] lowerInclusive, Integer[] upperInclusive) {
        return fetchRange(TxWitnesses.TX_WITNESSES.INDEX_ARR, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>index_arr IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses> fetchByIndexArr(Integer[]... values) {
        return fetch(TxWitnesses.TX_WITNESSES.INDEX_ARR, values);
    }

    /**
     * Fetch records that have <code>index_arr_size BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses> fetchRangeOfIndexArrSize(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(TxWitnesses.TX_WITNESSES.INDEX_ARR_SIZE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>index_arr_size IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses> fetchByIndexArrSize(Integer... values) {
        return fetch(TxWitnesses.TX_WITNESSES.INDEX_ARR_SIZE, values);
    }

    /**
     * Fetch records that have <code>type BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses> fetchRangeOfType(String lowerInclusive, String upperInclusive) {
        return fetchRange(TxWitnesses.TX_WITNESSES.TYPE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>type IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.TxWitnesses> fetchByType(String... values) {
        return fetch(TxWitnesses.TX_WITNESSES.TYPE, values);
    }
}