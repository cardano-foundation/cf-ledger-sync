/*
 * This file is generated by jOOQ.
 */
package org.cardanofoundation.ledgersync.jooq.tables;


import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.cardanofoundation.ledgersync.jooq.DefaultSchema;
import org.cardanofoundation.ledgersync.jooq.Indexes;
import org.cardanofoundation.ledgersync.jooq.Keys;
import org.cardanofoundation.ledgersync.jooq.tables.records.TxMetadataRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function5;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TxMetadata extends TableImpl<TxMetadataRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>tx_metadata</code>
     */
    public static final TxMetadata TX_METADATA = new TxMetadata();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TxMetadataRecord> getRecordType() {
        return TxMetadataRecord.class;
    }

    /**
     * The column <code>tx_metadata.id</code>.
     */
    public final TableField<TxMetadataRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>tx_metadata.bytes</code>.
     */
    public final TableField<TxMetadataRecord, byte[]> BYTES = createField(DSL.name("bytes"), SQLDataType.BLOB, this, "");

    /**
     * The column <code>tx_metadata.json</code>.
     */
    public final TableField<TxMetadataRecord, String> JSON = createField(DSL.name("json"), SQLDataType.VARCHAR(65535), this, "");

    /**
     * The column <code>tx_metadata.key</code>.
     */
    public final TableField<TxMetadataRecord, BigInteger> KEY = createField(DSL.name("key"), SQLDataType.DECIMAL_INTEGER(20).nullable(false), this, "");

    /**
     * The column <code>tx_metadata.tx_id</code>.
     */
    public final TableField<TxMetadataRecord, Long> TX_ID = createField(DSL.name("tx_id"), SQLDataType.BIGINT.nullable(false), this, "");

    private TxMetadata(Name alias, Table<TxMetadataRecord> aliased) {
        this(alias, aliased, null);
    }

    private TxMetadata(Name alias, Table<TxMetadataRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>tx_metadata</code> table reference
     */
    public TxMetadata(String alias) {
        this(DSL.name(alias), TX_METADATA);
    }

    /**
     * Create an aliased <code>tx_metadata</code> table reference
     */
    public TxMetadata(Name alias) {
        this(alias, TX_METADATA);
    }

    /**
     * Create a <code>tx_metadata</code> table reference
     */
    public TxMetadata() {
        this(DSL.name("tx_metadata"), null);
    }

    public <O extends Record> TxMetadata(Table<O> child, ForeignKey<O, TxMetadataRecord> key) {
        super(child, key, TX_METADATA);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.IDX_TX_METADATA_TX_ID);
    }

    @Override
    public Identity<TxMetadataRecord, Long> getIdentity() {
        return (Identity<TxMetadataRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<TxMetadataRecord> getPrimaryKey() {
        return Keys.TX_METADATA_PKEY;
    }

    @Override
    public List<UniqueKey<TxMetadataRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.UNIQUE_TX_METADATA);
    }

    @Override
    public TxMetadata as(String alias) {
        return new TxMetadata(DSL.name(alias), this);
    }

    @Override
    public TxMetadata as(Name alias) {
        return new TxMetadata(alias, this);
    }

    @Override
    public TxMetadata as(Table<?> alias) {
        return new TxMetadata(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public TxMetadata rename(String name) {
        return new TxMetadata(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TxMetadata rename(Name name) {
        return new TxMetadata(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public TxMetadata rename(Table<?> name) {
        return new TxMetadata(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, byte[], String, BigInteger, Long> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function5<? super Long, ? super byte[], ? super String, ? super BigInteger, ? super Long, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function5<? super Long, ? super byte[], ? super String, ? super BigInteger, ? super Long, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}