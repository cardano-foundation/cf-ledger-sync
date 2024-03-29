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
import org.cardanofoundation.ledgersync.jooq.tables.records.PoolRetireRecord;
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
public class PoolRetire extends TableImpl<PoolRetireRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>pool_retire</code>
     */
    public static final PoolRetire POOL_RETIRE = new PoolRetire();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PoolRetireRecord> getRecordType() {
        return PoolRetireRecord.class;
    }

    /**
     * The column <code>pool_retire.id</code>.
     */
    public final TableField<PoolRetireRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>pool_retire.cert_index</code>.
     */
    public final TableField<PoolRetireRecord, Integer> CERT_INDEX = createField(DSL.name("cert_index"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>pool_retire.retiring_epoch</code>.
     */
    public final TableField<PoolRetireRecord, Integer> RETIRING_EPOCH = createField(DSL.name("retiring_epoch"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>pool_retire.announced_tx_id</code>.
     */
    public final TableField<PoolRetireRecord, Long> ANNOUNCED_TX_ID = createField(DSL.name("announced_tx_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>pool_retire.hash_id</code>.
     */
    public final TableField<PoolRetireRecord, Long> HASH_ID = createField(DSL.name("hash_id"), SQLDataType.BIGINT.nullable(false), this, "");

    private PoolRetire(Name alias, Table<PoolRetireRecord> aliased) {
        this(alias, aliased, null);
    }

    private PoolRetire(Name alias, Table<PoolRetireRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>pool_retire</code> table reference
     */
    public PoolRetire(String alias) {
        this(DSL.name(alias), POOL_RETIRE);
    }

    /**
     * Create an aliased <code>pool_retire</code> table reference
     */
    public PoolRetire(Name alias) {
        this(alias, POOL_RETIRE);
    }

    /**
     * Create a <code>pool_retire</code> table reference
     */
    public PoolRetire() {
        this(DSL.name("pool_retire"), null);
    }

    public <O extends Record> PoolRetire(Table<O> child, ForeignKey<O, PoolRetireRecord> key) {
        super(child, key, POOL_RETIRE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.IDX_POOL_RETIRE_ANNOUNCED_TX_ID, Indexes.IDX_POOL_RETIRE_HASH_ID);
    }

    @Override
    public Identity<PoolRetireRecord, Long> getIdentity() {
        return (Identity<PoolRetireRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<PoolRetireRecord> getPrimaryKey() {
        return Keys.POOL_RETIRE_PKEY;
    }

    @Override
    public List<UniqueKey<PoolRetireRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.UNIQUE_POOL_RETIRING);
    }

    @Override
    public PoolRetire as(String alias) {
        return new PoolRetire(DSL.name(alias), this);
    }

    @Override
    public PoolRetire as(Name alias) {
        return new PoolRetire(alias, this);
    }

    @Override
    public PoolRetire as(Table<?> alias) {
        return new PoolRetire(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public PoolRetire rename(String name) {
        return new PoolRetire(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PoolRetire rename(Name name) {
        return new PoolRetire(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public PoolRetire rename(Table<?> name) {
        return new PoolRetire(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, Integer, Integer, Long, Long> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function5<? super Long, ? super Integer, ? super Integer, ? super Long, ? super Long, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function5<? super Long, ? super Integer, ? super Integer, ? super Long, ? super Long, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
