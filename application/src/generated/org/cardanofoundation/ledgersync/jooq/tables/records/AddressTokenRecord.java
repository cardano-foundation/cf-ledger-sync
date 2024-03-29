/*
 * This file is generated by jOOQ.
 */
package org.cardanofoundation.ledgersync.jooq.tables.records;


import java.math.BigInteger;

import org.cardanofoundation.ledgersync.jooq.tables.AddressToken;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AddressTokenRecord extends UpdatableRecordImpl<AddressTokenRecord> implements Record5<Long, BigInteger, Long, Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>address_token.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>address_token.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>address_token.balance</code>.
     */
    public void setBalance(BigInteger value) {
        set(1, value);
    }

    /**
     * Getter for <code>address_token.balance</code>.
     */
    public BigInteger getBalance() {
        return (BigInteger) get(1);
    }

    /**
     * Setter for <code>address_token.ident</code>.
     */
    public void setIdent(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>address_token.ident</code>.
     */
    public Long getIdent() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>address_token.tx_id</code>.
     */
    public void setTxId(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>address_token.tx_id</code>.
     */
    public Long getTxId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>address_token.address_id</code>.
     */
    public void setAddressId(Long value) {
        set(4, value);
    }

    /**
     * Getter for <code>address_token.address_id</code>.
     */
    public Long getAddressId() {
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
    public Row5<Long, BigInteger, Long, Long, Long> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Long, BigInteger, Long, Long, Long> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return AddressToken.ADDRESS_TOKEN.ID;
    }

    @Override
    public Field<BigInteger> field2() {
        return AddressToken.ADDRESS_TOKEN.BALANCE;
    }

    @Override
    public Field<Long> field3() {
        return AddressToken.ADDRESS_TOKEN.IDENT;
    }

    @Override
    public Field<Long> field4() {
        return AddressToken.ADDRESS_TOKEN.TX_ID;
    }

    @Override
    public Field<Long> field5() {
        return AddressToken.ADDRESS_TOKEN.ADDRESS_ID;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public BigInteger component2() {
        return getBalance();
    }

    @Override
    public Long component3() {
        return getIdent();
    }

    @Override
    public Long component4() {
        return getTxId();
    }

    @Override
    public Long component5() {
        return getAddressId();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public BigInteger value2() {
        return getBalance();
    }

    @Override
    public Long value3() {
        return getIdent();
    }

    @Override
    public Long value4() {
        return getTxId();
    }

    @Override
    public Long value5() {
        return getAddressId();
    }

    @Override
    public AddressTokenRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public AddressTokenRecord value2(BigInteger value) {
        setBalance(value);
        return this;
    }

    @Override
    public AddressTokenRecord value3(Long value) {
        setIdent(value);
        return this;
    }

    @Override
    public AddressTokenRecord value4(Long value) {
        setTxId(value);
        return this;
    }

    @Override
    public AddressTokenRecord value5(Long value) {
        setAddressId(value);
        return this;
    }

    @Override
    public AddressTokenRecord values(Long value1, BigInteger value2, Long value3, Long value4, Long value5) {
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
     * Create a detached AddressTokenRecord
     */
    public AddressTokenRecord() {
        super(AddressToken.ADDRESS_TOKEN);
    }

    /**
     * Create a detached, initialised AddressTokenRecord
     */
    public AddressTokenRecord(Long id, BigInteger balance, Long ident, Long txId, Long addressId) {
        super(AddressToken.ADDRESS_TOKEN);

        setId(id);
        setBalance(balance);
        setIdent(ident);
        setTxId(txId);
        setAddressId(addressId);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised AddressTokenRecord
     */
    public AddressTokenRecord(org.cardanofoundation.ledgersync.jooq.tables.pojos.AddressToken value) {
        super(AddressToken.ADDRESS_TOKEN);

        if (value != null) {
            setId(value.getId());
            setBalance(value.getBalance());
            setIdent(value.getIdent());
            setTxId(value.getTxId());
            setAddressId(value.getAddressId());
            resetChangedOnNotNull();
        }
    }
}
