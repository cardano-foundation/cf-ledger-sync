/*
 * This file is generated by jOOQ.
 */
package org.cardanofoundation.ledgersync.jooq.tables;


import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.cardanofoundation.ledgersync.jooq.DefaultSchema;
import org.cardanofoundation.ledgersync.jooq.Indexes;
import org.cardanofoundation.ledgersync.jooq.Keys;
import org.cardanofoundation.ledgersync.jooq.tables.records.DelegationRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function8;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row8;
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
public class Delegation extends TableImpl<DelegationRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>delegation</code>
     */
    public static final Delegation DELEGATION = new Delegation();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<DelegationRecord> getRecordType() {
        return DelegationRecord.class;
    }

    /**
     * The column <code>delegation.id</code>.
     */
    public final TableField<DelegationRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>delegation.active_epoch_no</code>.
     */
    public final TableField<DelegationRecord, Long> ACTIVE_EPOCH_NO = createField(DSL.name("active_epoch_no"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>delegation.cert_index</code>.
     */
    public final TableField<DelegationRecord, Integer> CERT_INDEX = createField(DSL.name("cert_index"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>delegation.slot_no</code>.
     */
    public final TableField<DelegationRecord, Long> SLOT_NO = createField(DSL.name("slot_no"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>delegation.addr_id</code>.
     */
    public final TableField<DelegationRecord, Long> ADDR_ID = createField(DSL.name("addr_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>delegation.pool_hash_id</code>.
     */
    public final TableField<DelegationRecord, Long> POOL_HASH_ID = createField(DSL.name("pool_hash_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>delegation.redeemer_id</code>.
     */
    public final TableField<DelegationRecord, Long> REDEEMER_ID = createField(DSL.name("redeemer_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>delegation.tx_id</code>.
     */
    public final TableField<DelegationRecord, Long> TX_ID = createField(DSL.name("tx_id"), SQLDataType.BIGINT.nullable(false), this, "");

    private Delegation(Name alias, Table<DelegationRecord> aliased) {
        this(alias, aliased, null);
    }

    private Delegation(Name alias, Table<DelegationRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>delegation</code> table reference
     */
    public Delegation(String alias) {
        this(DSL.name(alias), DELEGATION);
    }

    /**
     * Create an aliased <code>delegation</code> table reference
     */
    public Delegation(Name alias) {
        this(alias, DELEGATION);
    }

    /**
     * Create a <code>delegation</code> table reference
     */
    public Delegation() {
        this(DSL.name("delegation"), null);
    }

    public <O extends Record> Delegation(Table<O> child, ForeignKey<O, DelegationRecord> key) {
        super(child, key, DELEGATION);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.IDX_DELEGATION_ACTIVE_EPOCH_NO, Indexes.IDX_DELEGATION_ADDR_ID, Indexes.IDX_DELEGATION_POOL_HASH_ID, Indexes.IDX_DELEGATION_REDEEMER_ID, Indexes.IDX_DELEGATION_TX_ID);
    }

    @Override
    public Identity<DelegationRecord, Long> getIdentity() {
        return (Identity<DelegationRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<DelegationRecord> getPrimaryKey() {
        return Keys.DELEGATION_PKEY;
    }

    @Override
    public List<UniqueKey<DelegationRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.UNIQUE_DELEGATION);
    }

    @Override
    public Delegation as(String alias) {
        return new Delegation(DSL.name(alias), this);
    }

    @Override
    public Delegation as(Name alias) {
        return new Delegation(alias, this);
    }

    @Override
    public Delegation as(Table<?> alias) {
        return new Delegation(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Delegation rename(String name) {
        return new Delegation(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Delegation rename(Name name) {
        return new Delegation(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Delegation rename(Table<?> name) {
        return new Delegation(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row8 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row8<Long, Long, Integer, Long, Long, Long, Long, Long> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function8<? super Long, ? super Long, ? super Integer, ? super Long, ? super Long, ? super Long, ? super Long, ? super Long, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function8<? super Long, ? super Long, ? super Integer, ? super Long, ? super Long, ? super Long, ? super Long, ? super Long, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
