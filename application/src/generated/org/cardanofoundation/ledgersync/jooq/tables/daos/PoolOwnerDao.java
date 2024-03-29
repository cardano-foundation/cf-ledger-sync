/*
 * This file is generated by jOOQ.
 */
package org.cardanofoundation.ledgersync.jooq.tables.daos;


import java.util.List;
import java.util.Optional;

import org.cardanofoundation.ledgersync.jooq.AbstractSpringDAOImpl;
import org.cardanofoundation.ledgersync.jooq.tables.PoolOwner;
import org.cardanofoundation.ledgersync.jooq.tables.records.PoolOwnerRecord;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Repository
public class PoolOwnerDao extends AbstractSpringDAOImpl<PoolOwnerRecord, org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolOwner, Long> {

    /**
     * Create a new PoolOwnerDao without any configuration
     */
    public PoolOwnerDao() {
        super(PoolOwner.POOL_OWNER, org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolOwner.class);
    }

    /**
     * Create a new PoolOwnerDao with an attached configuration
     */
    @Autowired
    public PoolOwnerDao(Configuration configuration) {
        super(PoolOwner.POOL_OWNER, org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolOwner.class, configuration);
    }

    @Override
    public Long getId(org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolOwner object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolOwner> fetchRangeOfId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(PoolOwner.POOL_OWNER.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolOwner> fetchById(Long... values) {
        return fetch(PoolOwner.POOL_OWNER.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolOwner fetchOneById(Long value) {
        return fetchOne(PoolOwner.POOL_OWNER.ID, value);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public Optional<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolOwner> fetchOptionalById(Long value) {
        return fetchOptional(PoolOwner.POOL_OWNER.ID, value);
    }

    /**
     * Fetch records that have <code>pool_update_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolOwner> fetchRangeOfPoolUpdateId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(PoolOwner.POOL_OWNER.POOL_UPDATE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>pool_update_id IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolOwner> fetchByPoolUpdateId(Long... values) {
        return fetch(PoolOwner.POOL_OWNER.POOL_UPDATE_ID, values);
    }

    /**
     * Fetch records that have <code>addr_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolOwner> fetchRangeOfAddrId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(PoolOwner.POOL_OWNER.ADDR_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>addr_id IN (values)</code>
     */
    public List<org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolOwner> fetchByAddrId(Long... values) {
        return fetch(PoolOwner.POOL_OWNER.ADDR_ID, values);
    }
}
