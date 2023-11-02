/*
 * This file is generated by jOOQ.
 */
package org.cardanofoundation.ledgersync.jooq.tables.daos;


import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.cardanofoundation.ledgersync.jooq.AbstractSpringDAOImpl;
import org.cardanofoundation.ledgersync.jooq.tables.FailedTxOut;
import org.cardanofoundation.ledgersync.jooq.tables.records.FailedTxOutRecord;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Repository
public class FailedTxOutDao extends AbstractSpringDAOImpl<FailedTxOutRecord, org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut, Long> {

    /**
     * Create a new FailedTxOutDao without any configuration
     */
    public FailedTxOutDao() {
        super(FailedTxOut.FAILED_TX_OUT, org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut.class);
    }

    /**
     * Create a new FailedTxOutDao with an attached configuration
     */
    @Autowired
    public FailedTxOutDao(Configuration configuration) {
        super(FailedTxOut.FAILED_TX_OUT, org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut.class, configuration);
    }

    @Override
    public Long getId(org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchRangeOfId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(FailedTxOut.FAILED_TX_OUT.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchById(Long... values) {
        return fetch(FailedTxOut.FAILED_TX_OUT.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut fetchOneById(Long value) {
        return fetchOne(FailedTxOut.FAILED_TX_OUT.ID, value);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public Optional<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchOptionalById(Long value) {
        return fetchOptional(FailedTxOut.FAILED_TX_OUT.ID, value);
    }

    /**
     * Fetch records that have <code>address BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchRangeOfAddress(String lowerInclusive, String upperInclusive) {
        return fetchRange(FailedTxOut.FAILED_TX_OUT.ADDRESS, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>address IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchByAddress(String... values) {
        return fetch(FailedTxOut.FAILED_TX_OUT.ADDRESS, values);
    }

    /**
     * Fetch records that have <code>address_has_script BETWEEN lowerInclusive
     * AND upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchRangeOfAddressHasScript(Boolean lowerInclusive, Boolean upperInclusive) {
        return fetchRange(FailedTxOut.FAILED_TX_OUT.ADDRESS_HAS_SCRIPT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>address_has_script IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchByAddressHasScript(Boolean... values) {
        return fetch(FailedTxOut.FAILED_TX_OUT.ADDRESS_HAS_SCRIPT, values);
    }

    /**
     * Fetch records that have <code>address_raw BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchRangeOfAddressRaw(byte[] lowerInclusive, byte[] upperInclusive) {
        return fetchRange(FailedTxOut.FAILED_TX_OUT.ADDRESS_RAW, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>address_raw IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchByAddressRaw(byte[]... values) {
        return fetch(FailedTxOut.FAILED_TX_OUT.ADDRESS_RAW, values);
    }

    /**
     * Fetch records that have <code>data_hash BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchRangeOfDataHash(String lowerInclusive, String upperInclusive) {
        return fetchRange(FailedTxOut.FAILED_TX_OUT.DATA_HASH, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>data_hash IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchByDataHash(String... values) {
        return fetch(FailedTxOut.FAILED_TX_OUT.DATA_HASH, values);
    }

    /**
     * Fetch records that have <code>index BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchRangeOfIndex(Short lowerInclusive, Short upperInclusive) {
        return fetchRange(FailedTxOut.FAILED_TX_OUT.INDEX, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>index IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchByIndex(Short... values) {
        return fetch(FailedTxOut.FAILED_TX_OUT.INDEX, values);
    }

    /**
     * Fetch records that have <code>multi_assets_descr BETWEEN lowerInclusive
     * AND upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchRangeOfMultiAssetsDescr(String lowerInclusive, String upperInclusive) {
        return fetchRange(FailedTxOut.FAILED_TX_OUT.MULTI_ASSETS_DESCR, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>multi_assets_descr IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchByMultiAssetsDescr(String... values) {
        return fetch(FailedTxOut.FAILED_TX_OUT.MULTI_ASSETS_DESCR, values);
    }

    /**
     * Fetch records that have <code>payment_cred BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchRangeOfPaymentCred(String lowerInclusive, String upperInclusive) {
        return fetchRange(FailedTxOut.FAILED_TX_OUT.PAYMENT_CRED, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>payment_cred IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchByPaymentCred(String... values) {
        return fetch(FailedTxOut.FAILED_TX_OUT.PAYMENT_CRED, values);
    }

    /**
     * Fetch records that have <code>value BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchRangeOfValue(BigInteger lowerInclusive, BigInteger upperInclusive) {
        return fetchRange(FailedTxOut.FAILED_TX_OUT.VALUE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>value IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchByValue(BigInteger... values) {
        return fetch(FailedTxOut.FAILED_TX_OUT.VALUE, values);
    }

    /**
     * Fetch records that have <code>inline_datum_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchRangeOfInlineDatumId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(FailedTxOut.FAILED_TX_OUT.INLINE_DATUM_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>inline_datum_id IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchByInlineDatumId(Long... values) {
        return fetch(FailedTxOut.FAILED_TX_OUT.INLINE_DATUM_ID, values);
    }

    /**
     * Fetch records that have <code>reference_script_id BETWEEN lowerInclusive
     * AND upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchRangeOfReferenceScriptId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(FailedTxOut.FAILED_TX_OUT.REFERENCE_SCRIPT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>reference_script_id IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchByReferenceScriptId(Long... values) {
        return fetch(FailedTxOut.FAILED_TX_OUT.REFERENCE_SCRIPT_ID, values);
    }

    /**
     * Fetch records that have <code>stake_address_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchRangeOfStakeAddressId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(FailedTxOut.FAILED_TX_OUT.STAKE_ADDRESS_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>stake_address_id IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchByStakeAddressId(Long... values) {
        return fetch(FailedTxOut.FAILED_TX_OUT.STAKE_ADDRESS_ID, values);
    }

    /**
     * Fetch records that have <code>tx_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchRangeOfTxId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(FailedTxOut.FAILED_TX_OUT.TX_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>tx_id IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut> fetchByTxId(Long... values) {
        return fetch(FailedTxOut.FAILED_TX_OUT.TX_ID, values);
    }
}