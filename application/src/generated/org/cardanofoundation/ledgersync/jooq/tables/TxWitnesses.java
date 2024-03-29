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
import org.cardanofoundation.ledgersync.jooq.tables.records.TxWitnessesRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function7;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row7;
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
public class TxWitnesses extends TableImpl<TxWitnessesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>tx_witnesses</code>
     */
    public static final TxWitnesses TX_WITNESSES = new TxWitnesses();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TxWitnessesRecord> getRecordType() {
        return TxWitnessesRecord.class;
    }

    /**
     * The column <code>tx_witnesses.id</code>.
     */
    public final TableField<TxWitnessesRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>tx_witnesses.tx_id</code>.
     */
    public final TableField<TxWitnessesRecord, Long> TX_ID = createField(DSL.name("tx_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>tx_witnesses.key</code>.
     */
    public final TableField<TxWitnessesRecord, String> KEY = createField(DSL.name("key"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>tx_witnesses.signature</code>.
     */
    public final TableField<TxWitnessesRecord, String> SIGNATURE = createField(DSL.name("signature"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>tx_witnesses.index_arr</code>.
     */
    public final TableField<TxWitnessesRecord, Integer[]> INDEX_ARR = createField(DSL.name("index_arr"), SQLDataType.INTEGER.array(), this, "");

    /**
     * The column <code>tx_witnesses.index_arr_size</code>.
     */
    public final TableField<TxWitnessesRecord, Integer> INDEX_ARR_SIZE = createField(DSL.name("index_arr_size"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>tx_witnesses.type</code>.
     */
    public final TableField<TxWitnessesRecord, String> TYPE = createField(DSL.name("type"), SQLDataType.VARCHAR(50), this, "");

    private TxWitnesses(Name alias, Table<TxWitnessesRecord> aliased) {
        this(alias, aliased, null);
    }

    private TxWitnesses(Name alias, Table<TxWitnessesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>tx_witnesses</code> table reference
     */
    public TxWitnesses(String alias) {
        this(DSL.name(alias), TX_WITNESSES);
    }

    /**
     * Create an aliased <code>tx_witnesses</code> table reference
     */
    public TxWitnesses(Name alias) {
        this(alias, TX_WITNESSES);
    }

    /**
     * Create a <code>tx_witnesses</code> table reference
     */
    public TxWitnesses() {
        this(DSL.name("tx_witnesses"), null);
    }

    public <O extends Record> TxWitnesses(Table<O> child, ForeignKey<O, TxWitnessesRecord> key) {
        super(child, key, TX_WITNESSES);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.TX_WITNESSES_TX_ID_IDX);
    }

    @Override
    public Identity<TxWitnessesRecord, Long> getIdentity() {
        return (Identity<TxWitnessesRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<TxWitnessesRecord> getPrimaryKey() {
        return Keys.TX_WITNESSES_PKEY;
    }

    @Override
    public TxWitnesses as(String alias) {
        return new TxWitnesses(DSL.name(alias), this);
    }

    @Override
    public TxWitnesses as(Name alias) {
        return new TxWitnesses(alias, this);
    }

    @Override
    public TxWitnesses as(Table<?> alias) {
        return new TxWitnesses(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public TxWitnesses rename(String name) {
        return new TxWitnesses(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TxWitnesses rename(Name name) {
        return new TxWitnesses(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public TxWitnesses rename(Table<?> name) {
        return new TxWitnesses(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, Long, String, String, Integer[], Integer, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function7<? super Long, ? super Long, ? super String, ? super String, ? super Integer[], ? super Integer, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function7<? super Long, ? super Long, ? super String, ? super String, ? super Integer[], ? super Integer, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
