/*
 * This file is generated by jOOQ.
 */
package org.cardanofoundation.ledgersync.jooq.tables.records;


import org.cardanofoundation.ledgersync.jooq.tables.TxMetadataHash;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TxMetadataHashRecord extends UpdatableRecordImpl<TxMetadataHashRecord> implements Record2<Long, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>tx_metadata_hash.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>tx_metadata_hash.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>tx_metadata_hash.hash</code>.
     */
    public void setHash(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>tx_metadata_hash.hash</code>.
     */
    public String getHash() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Long, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return TxMetadataHash.TX_METADATA_HASH.ID;
    }

    @Override
    public Field<String> field2() {
        return TxMetadataHash.TX_METADATA_HASH.HASH;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getHash();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getHash();
    }

    @Override
    public TxMetadataHashRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public TxMetadataHashRecord value2(String value) {
        setHash(value);
        return this;
    }

    @Override
    public TxMetadataHashRecord values(Long value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TxMetadataHashRecord
     */
    public TxMetadataHashRecord() {
        super(TxMetadataHash.TX_METADATA_HASH);
    }

    /**
     * Create a detached, initialised TxMetadataHashRecord
     */
    public TxMetadataHashRecord(Long id, String hash) {
        super(TxMetadataHash.TX_METADATA_HASH);

        setId(id);
        setHash(hash);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised TxMetadataHashRecord
     */
    public TxMetadataHashRecord(org.cardanofoundation.ledgersync.jooq.tables.pojos.TxMetadataHash value) {
        super(TxMetadataHash.TX_METADATA_HASH);

        if (value != null) {
            setId(value.getId());
            setHash(value.getHash());
            resetChangedOnNotNull();
        }
    }
}
