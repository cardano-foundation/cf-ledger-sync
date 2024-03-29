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
import org.cardanofoundation.ledgersync.jooq.tables.records.AddressTokenRecord;
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
public class AddressToken extends TableImpl<AddressTokenRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>address_token</code>
     */
    public static final AddressToken ADDRESS_TOKEN = new AddressToken();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AddressTokenRecord> getRecordType() {
        return AddressTokenRecord.class;
    }

    /**
     * The column <code>address_token.id</code>.
     */
    public final TableField<AddressTokenRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>address_token.balance</code>.
     */
    public final TableField<AddressTokenRecord, BigInteger> BALANCE = createField(DSL.name("balance"), SQLDataType.DECIMAL_INTEGER(39).nullable(false), this, "");

    /**
     * The column <code>address_token.ident</code>.
     */
    public final TableField<AddressTokenRecord, Long> IDENT = createField(DSL.name("ident"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>address_token.tx_id</code>.
     */
    public final TableField<AddressTokenRecord, Long> TX_ID = createField(DSL.name("tx_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>address_token.address_id</code>.
     */
    public final TableField<AddressTokenRecord, Long> ADDRESS_ID = createField(DSL.name("address_id"), SQLDataType.BIGINT, this, "");

    private AddressToken(Name alias, Table<AddressTokenRecord> aliased) {
        this(alias, aliased, null);
    }

    private AddressToken(Name alias, Table<AddressTokenRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>address_token</code> table reference
     */
    public AddressToken(String alias) {
        this(DSL.name(alias), ADDRESS_TOKEN);
    }

    /**
     * Create an aliased <code>address_token</code> table reference
     */
    public AddressToken(Name alias) {
        this(alias, ADDRESS_TOKEN);
    }

    /**
     * Create a <code>address_token</code> table reference
     */
    public AddressToken() {
        this(DSL.name("address_token"), null);
    }

    public <O extends Record> AddressToken(Table<O> child, ForeignKey<O, AddressTokenRecord> key) {
        super(child, key, ADDRESS_TOKEN);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.ADDRESS_TOKEN_IDENT_STAKE_TX_ID_BALANCE_IDX, Indexes.IDX_ADDRESS_TOKEN_ADDRESS_ID, Indexes.IDX_ADDRESS_TOKEN_IDENT, Indexes.IDX_ADDRESS_TOKEN_TX_ID);
    }

    @Override
    public Identity<AddressTokenRecord, Long> getIdentity() {
        return (Identity<AddressTokenRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<AddressTokenRecord> getPrimaryKey() {
        return Keys.ADDRESS_TOKEN_PKEY;
    }

    @Override
    public AddressToken as(String alias) {
        return new AddressToken(DSL.name(alias), this);
    }

    @Override
    public AddressToken as(Name alias) {
        return new AddressToken(alias, this);
    }

    @Override
    public AddressToken as(Table<?> alias) {
        return new AddressToken(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public AddressToken rename(String name) {
        return new AddressToken(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public AddressToken rename(Name name) {
        return new AddressToken(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public AddressToken rename(Table<?> name) {
        return new AddressToken(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, BigInteger, Long, Long, Long> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function5<? super Long, ? super BigInteger, ? super Long, ? super Long, ? super Long, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function5<? super Long, ? super BigInteger, ? super Long, ? super Long, ? super Long, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
