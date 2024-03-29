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
import org.cardanofoundation.ledgersync.jooq.tables.records.ReferenceTxInRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function4;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row4;
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
public class ReferenceTxIn extends TableImpl<ReferenceTxInRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>reference_tx_in</code>
     */
    public static final ReferenceTxIn REFERENCE_TX_IN = new ReferenceTxIn();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ReferenceTxInRecord> getRecordType() {
        return ReferenceTxInRecord.class;
    }

    /**
     * The column <code>reference_tx_in.id</code>.
     */
    public final TableField<ReferenceTxInRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>reference_tx_in.tx_out_index</code>.
     */
    public final TableField<ReferenceTxInRecord, Short> TX_OUT_INDEX = createField(DSL.name("tx_out_index"), SQLDataType.SMALLINT.nullable(false), this, "");

    /**
     * The column <code>reference_tx_in.tx_in_id</code>.
     */
    public final TableField<ReferenceTxInRecord, Long> TX_IN_ID = createField(DSL.name("tx_in_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>reference_tx_in.tx_out_id</code>.
     */
    public final TableField<ReferenceTxInRecord, Long> TX_OUT_ID = createField(DSL.name("tx_out_id"), SQLDataType.BIGINT.nullable(false), this, "");

    private ReferenceTxIn(Name alias, Table<ReferenceTxInRecord> aliased) {
        this(alias, aliased, null);
    }

    private ReferenceTxIn(Name alias, Table<ReferenceTxInRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>reference_tx_in</code> table reference
     */
    public ReferenceTxIn(String alias) {
        this(DSL.name(alias), REFERENCE_TX_IN);
    }

    /**
     * Create an aliased <code>reference_tx_in</code> table reference
     */
    public ReferenceTxIn(Name alias) {
        this(alias, REFERENCE_TX_IN);
    }

    /**
     * Create a <code>reference_tx_in</code> table reference
     */
    public ReferenceTxIn() {
        this(DSL.name("reference_tx_in"), null);
    }

    public <O extends Record> ReferenceTxIn(Table<O> child, ForeignKey<O, ReferenceTxInRecord> key) {
        super(child, key, REFERENCE_TX_IN);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.REFERENCE_TX_IN_TX_OUT_ID_IDX);
    }

    @Override
    public Identity<ReferenceTxInRecord, Long> getIdentity() {
        return (Identity<ReferenceTxInRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<ReferenceTxInRecord> getPrimaryKey() {
        return Keys.REFERENCE_TX_IN_PKEY;
    }

    @Override
    public List<UniqueKey<ReferenceTxInRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.UNIQUE_REF_TXIN);
    }

    @Override
    public ReferenceTxIn as(String alias) {
        return new ReferenceTxIn(DSL.name(alias), this);
    }

    @Override
    public ReferenceTxIn as(Name alias) {
        return new ReferenceTxIn(alias, this);
    }

    @Override
    public ReferenceTxIn as(Table<?> alias) {
        return new ReferenceTxIn(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public ReferenceTxIn rename(String name) {
        return new ReferenceTxIn(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ReferenceTxIn rename(Name name) {
        return new ReferenceTxIn(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public ReferenceTxIn rename(Table<?> name) {
        return new ReferenceTxIn(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, Short, Long, Long> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super Long, ? super Short, ? super Long, ? super Long, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function4<? super Long, ? super Short, ? super Long, ? super Long, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
