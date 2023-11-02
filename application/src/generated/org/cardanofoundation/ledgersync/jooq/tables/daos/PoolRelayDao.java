/*
 * This file is generated by jOOQ.
 */
package org.cardanofoundation.ledgersync.jooq.tables.daos;


import java.util.List;
import java.util.Optional;

import org.cardanofoundation.ledgersync.jooq.AbstractSpringDAOImpl;
import org.cardanofoundation.ledgersync.jooq.tables.PoolRelay;
import org.cardanofoundation.ledgersync.jooq.tables.records.PoolRelayRecord;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Repository
public class PoolRelayDao extends AbstractSpringDAOImpl<PoolRelayRecord, org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay, Long> {

    /**
     * Create a new PoolRelayDao without any configuration
     */
    public PoolRelayDao() {
        super(PoolRelay.POOL_RELAY, org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay.class);
    }

    /**
     * Create a new PoolRelayDao with an attached configuration
     */
    @Autowired
    public PoolRelayDao(Configuration configuration) {
        super(PoolRelay.POOL_RELAY, org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay.class, configuration);
    }

    @Override
    public Long getId(org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay> fetchRangeOfId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(PoolRelay.POOL_RELAY.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay> fetchById(Long... values) {
        return fetch(PoolRelay.POOL_RELAY.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay fetchOneById(Long value) {
        return fetchOne(PoolRelay.POOL_RELAY.ID, value);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public Optional<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay> fetchOptionalById(Long value) {
        return fetchOptional(PoolRelay.POOL_RELAY.ID, value);
    }

    /**
     * Fetch records that have <code>dns_name BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay> fetchRangeOfDnsName(String lowerInclusive, String upperInclusive) {
        return fetchRange(PoolRelay.POOL_RELAY.DNS_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>dns_name IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay> fetchByDnsName(String... values) {
        return fetch(PoolRelay.POOL_RELAY.DNS_NAME, values);
    }

    /**
     * Fetch records that have <code>dns_srv_name BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay> fetchRangeOfDnsSrvName(String lowerInclusive, String upperInclusive) {
        return fetchRange(PoolRelay.POOL_RELAY.DNS_SRV_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>dns_srv_name IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay> fetchByDnsSrvName(String... values) {
        return fetch(PoolRelay.POOL_RELAY.DNS_SRV_NAME, values);
    }

    /**
     * Fetch records that have <code>ipv4 BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay> fetchRangeOfIpv4(String lowerInclusive, String upperInclusive) {
        return fetchRange(PoolRelay.POOL_RELAY.IPV4, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>ipv4 IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay> fetchByIpv4(String... values) {
        return fetch(PoolRelay.POOL_RELAY.IPV4, values);
    }

    /**
     * Fetch records that have <code>ipv6 BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay> fetchRangeOfIpv6(String lowerInclusive, String upperInclusive) {
        return fetchRange(PoolRelay.POOL_RELAY.IPV6, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>ipv6 IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay> fetchByIpv6(String... values) {
        return fetch(PoolRelay.POOL_RELAY.IPV6, values);
    }

    /**
     * Fetch records that have <code>port BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay> fetchRangeOfPort(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(PoolRelay.POOL_RELAY.PORT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>port IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay> fetchByPort(Integer... values) {
        return fetch(PoolRelay.POOL_RELAY.PORT, values);
    }

    /**
     * Fetch records that have <code>update_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay> fetchRangeOfUpdateId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(PoolRelay.POOL_RELAY.UPDATE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>update_id IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRelay> fetchByUpdateId(Long... values) {
        return fetch(PoolRelay.POOL_RELAY.UPDATE_ID, values);
    }
}