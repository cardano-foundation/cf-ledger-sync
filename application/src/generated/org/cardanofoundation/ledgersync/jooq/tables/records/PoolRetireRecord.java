/*
 * This file is generated by jOOQ.
 */
package org.cardanofoundation.ledgersync.jooq.tables.records;


import org.cardanofoundation.ledgersync.jooq.tables.PoolRetire;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PoolRetireRecord extends UpdatableRecordImpl<PoolRetireRecord> implements Record5<Long, Integer, Integer, Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>pool_retire.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>pool_retire.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>pool_retire.cert_index</code>.
     */
    public void setCertIndex(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>pool_retire.cert_index</code>.
     */
    public Integer getCertIndex() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>pool_retire.retiring_epoch</code>.
     */
    public void setRetiringEpoch(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>pool_retire.retiring_epoch</code>.
     */
    public Integer getRetiringEpoch() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>pool_retire.announced_tx_id</code>.
     */
    public void setAnnouncedTxId(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>pool_retire.announced_tx_id</code>.
     */
    public Long getAnnouncedTxId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>pool_retire.hash_id</code>.
     */
    public void setHashId(Long value) {
        set(4, value);
    }

    /**
     * Getter for <code>pool_retire.hash_id</code>.
     */
    public Long getHashId() {
        return (Long) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, Integer, Integer, Long, Long> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Long, Integer, Integer, Long, Long> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return PoolRetire.POOL_RETIRE.ID;
    }

    @Override
    public Field<Integer> field2() {
        return PoolRetire.POOL_RETIRE.CERT_INDEX;
    }

    @Override
    public Field<Integer> field3() {
        return PoolRetire.POOL_RETIRE.RETIRING_EPOCH;
    }

    @Override
    public Field<Long> field4() {
        return PoolRetire.POOL_RETIRE.ANNOUNCED_TX_ID;
    }

    @Override
    public Field<Long> field5() {
        return PoolRetire.POOL_RETIRE.HASH_ID;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Integer component2() {
        return getCertIndex();
    }

    @Override
    public Integer component3() {
        return getRetiringEpoch();
    }

    @Override
    public Long component4() {
        return getAnnouncedTxId();
    }

    @Override
    public Long component5() {
        return getHashId();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Integer value2() {
        return getCertIndex();
    }

    @Override
    public Integer value3() {
        return getRetiringEpoch();
    }

    @Override
    public Long value4() {
        return getAnnouncedTxId();
    }

    @Override
    public Long value5() {
        return getHashId();
    }

    @Override
    public PoolRetireRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public PoolRetireRecord value2(Integer value) {
        setCertIndex(value);
        return this;
    }

    @Override
    public PoolRetireRecord value3(Integer value) {
        setRetiringEpoch(value);
        return this;
    }

    @Override
    public PoolRetireRecord value4(Long value) {
        setAnnouncedTxId(value);
        return this;
    }

    @Override
    public PoolRetireRecord value5(Long value) {
        setHashId(value);
        return this;
    }

    @Override
    public PoolRetireRecord values(Long value1, Integer value2, Integer value3, Long value4, Long value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PoolRetireRecord
     */
    public PoolRetireRecord() {
        super(PoolRetire.POOL_RETIRE);
    }

    /**
     * Create a detached, initialised PoolRetireRecord
     */
    public PoolRetireRecord(Long id, Integer certIndex, Integer retiringEpoch, Long announcedTxId, Long hashId) {
        super(PoolRetire.POOL_RETIRE);

        setId(id);
        setCertIndex(certIndex);
        setRetiringEpoch(retiringEpoch);
        setAnnouncedTxId(announcedTxId);
        setHashId(hashId);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised PoolRetireRecord
     */
    public PoolRetireRecord(org.cardanofoundation.ledgersync.jooq.tables.pojos.PoolRetire value) {
        super(PoolRetire.POOL_RETIRE);

        if (value != null) {
            setId(value.getId());
            setCertIndex(value.getCertIndex());
            setRetiringEpoch(value.getRetiringEpoch());
            setAnnouncedTxId(value.getAnnouncedTxId());
            setHashId(value.getHashId());
            resetChangedOnNotNull();
        }
    }
}
