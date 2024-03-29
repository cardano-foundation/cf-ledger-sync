/*
 * This file is generated by jOOQ.
 */
package org.cardanofoundation.ledgersync.jooq.tables.records;


import java.math.BigInteger;

import org.cardanofoundation.ledgersync.jooq.tables.FailedTxOut;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record13;
import org.jooq.Row13;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FailedTxOutRecord extends UpdatableRecordImpl<FailedTxOutRecord> implements Record13<Long, String, Boolean, byte[], String, Short, String, String, BigInteger, Long, Long, Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>failed_tx_out.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>failed_tx_out.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>failed_tx_out.address</code>.
     */
    public void setAddress(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>failed_tx_out.address</code>.
     */
    public String getAddress() {
        return (String) get(1);
    }

    /**
     * Setter for <code>failed_tx_out.address_has_script</code>.
     */
    public void setAddressHasScript(Boolean value) {
        set(2, value);
    }

    /**
     * Getter for <code>failed_tx_out.address_has_script</code>.
     */
    public Boolean getAddressHasScript() {
        return (Boolean) get(2);
    }

    /**
     * Setter for <code>failed_tx_out.address_raw</code>.
     */
    public void setAddressRaw(byte[] value) {
        set(3, value);
    }

    /**
     * Getter for <code>failed_tx_out.address_raw</code>.
     */
    public byte[] getAddressRaw() {
        return (byte[]) get(3);
    }

    /**
     * Setter for <code>failed_tx_out.data_hash</code>.
     */
    public void setDataHash(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>failed_tx_out.data_hash</code>.
     */
    public String getDataHash() {
        return (String) get(4);
    }

    /**
     * Setter for <code>failed_tx_out.index</code>.
     */
    public void setIndex(Short value) {
        set(5, value);
    }

    /**
     * Getter for <code>failed_tx_out.index</code>.
     */
    public Short getIndex() {
        return (Short) get(5);
    }

    /**
     * Setter for <code>failed_tx_out.multi_assets_descr</code>.
     */
    public void setMultiAssetsDescr(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>failed_tx_out.multi_assets_descr</code>.
     */
    public String getMultiAssetsDescr() {
        return (String) get(6);
    }

    /**
     * Setter for <code>failed_tx_out.payment_cred</code>.
     */
    public void setPaymentCred(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>failed_tx_out.payment_cred</code>.
     */
    public String getPaymentCred() {
        return (String) get(7);
    }

    /**
     * Setter for <code>failed_tx_out.value</code>.
     */
    public void setValue(BigInteger value) {
        set(8, value);
    }

    /**
     * Getter for <code>failed_tx_out.value</code>.
     */
    public BigInteger getValue() {
        return (BigInteger) get(8);
    }

    /**
     * Setter for <code>failed_tx_out.inline_datum_id</code>.
     */
    public void setInlineDatumId(Long value) {
        set(9, value);
    }

    /**
     * Getter for <code>failed_tx_out.inline_datum_id</code>.
     */
    public Long getInlineDatumId() {
        return (Long) get(9);
    }

    /**
     * Setter for <code>failed_tx_out.reference_script_id</code>.
     */
    public void setReferenceScriptId(Long value) {
        set(10, value);
    }

    /**
     * Getter for <code>failed_tx_out.reference_script_id</code>.
     */
    public Long getReferenceScriptId() {
        return (Long) get(10);
    }

    /**
     * Setter for <code>failed_tx_out.stake_address_id</code>.
     */
    public void setStakeAddressId(Long value) {
        set(11, value);
    }

    /**
     * Getter for <code>failed_tx_out.stake_address_id</code>.
     */
    public Long getStakeAddressId() {
        return (Long) get(11);
    }

    /**
     * Setter for <code>failed_tx_out.tx_id</code>.
     */
    public void setTxId(Long value) {
        set(12, value);
    }

    /**
     * Getter for <code>failed_tx_out.tx_id</code>.
     */
    public Long getTxId() {
        return (Long) get(12);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record13 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row13<Long, String, Boolean, byte[], String, Short, String, String, BigInteger, Long, Long, Long, Long> fieldsRow() {
        return (Row13) super.fieldsRow();
    }

    @Override
    public Row13<Long, String, Boolean, byte[], String, Short, String, String, BigInteger, Long, Long, Long, Long> valuesRow() {
        return (Row13) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return FailedTxOut.FAILED_TX_OUT.ID;
    }

    @Override
    public Field<String> field2() {
        return FailedTxOut.FAILED_TX_OUT.ADDRESS;
    }

    @Override
    public Field<Boolean> field3() {
        return FailedTxOut.FAILED_TX_OUT.ADDRESS_HAS_SCRIPT;
    }

    @Override
    public Field<byte[]> field4() {
        return FailedTxOut.FAILED_TX_OUT.ADDRESS_RAW;
    }

    @Override
    public Field<String> field5() {
        return FailedTxOut.FAILED_TX_OUT.DATA_HASH;
    }

    @Override
    public Field<Short> field6() {
        return FailedTxOut.FAILED_TX_OUT.INDEX;
    }

    @Override
    public Field<String> field7() {
        return FailedTxOut.FAILED_TX_OUT.MULTI_ASSETS_DESCR;
    }

    @Override
    public Field<String> field8() {
        return FailedTxOut.FAILED_TX_OUT.PAYMENT_CRED;
    }

    @Override
    public Field<BigInteger> field9() {
        return FailedTxOut.FAILED_TX_OUT.VALUE;
    }

    @Override
    public Field<Long> field10() {
        return FailedTxOut.FAILED_TX_OUT.INLINE_DATUM_ID;
    }

    @Override
    public Field<Long> field11() {
        return FailedTxOut.FAILED_TX_OUT.REFERENCE_SCRIPT_ID;
    }

    @Override
    public Field<Long> field12() {
        return FailedTxOut.FAILED_TX_OUT.STAKE_ADDRESS_ID;
    }

    @Override
    public Field<Long> field13() {
        return FailedTxOut.FAILED_TX_OUT.TX_ID;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getAddress();
    }

    @Override
    public Boolean component3() {
        return getAddressHasScript();
    }

    @Override
    public byte[] component4() {
        return getAddressRaw();
    }

    @Override
    public String component5() {
        return getDataHash();
    }

    @Override
    public Short component6() {
        return getIndex();
    }

    @Override
    public String component7() {
        return getMultiAssetsDescr();
    }

    @Override
    public String component8() {
        return getPaymentCred();
    }

    @Override
    public BigInteger component9() {
        return getValue();
    }

    @Override
    public Long component10() {
        return getInlineDatumId();
    }

    @Override
    public Long component11() {
        return getReferenceScriptId();
    }

    @Override
    public Long component12() {
        return getStakeAddressId();
    }

    @Override
    public Long component13() {
        return getTxId();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getAddress();
    }

    @Override
    public Boolean value3() {
        return getAddressHasScript();
    }

    @Override
    public byte[] value4() {
        return getAddressRaw();
    }

    @Override
    public String value5() {
        return getDataHash();
    }

    @Override
    public Short value6() {
        return getIndex();
    }

    @Override
    public String value7() {
        return getMultiAssetsDescr();
    }

    @Override
    public String value8() {
        return getPaymentCred();
    }

    @Override
    public BigInteger value9() {
        return getValue();
    }

    @Override
    public Long value10() {
        return getInlineDatumId();
    }

    @Override
    public Long value11() {
        return getReferenceScriptId();
    }

    @Override
    public Long value12() {
        return getStakeAddressId();
    }

    @Override
    public Long value13() {
        return getTxId();
    }

    @Override
    public FailedTxOutRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public FailedTxOutRecord value2(String value) {
        setAddress(value);
        return this;
    }

    @Override
    public FailedTxOutRecord value3(Boolean value) {
        setAddressHasScript(value);
        return this;
    }

    @Override
    public FailedTxOutRecord value4(byte[] value) {
        setAddressRaw(value);
        return this;
    }

    @Override
    public FailedTxOutRecord value5(String value) {
        setDataHash(value);
        return this;
    }

    @Override
    public FailedTxOutRecord value6(Short value) {
        setIndex(value);
        return this;
    }

    @Override
    public FailedTxOutRecord value7(String value) {
        setMultiAssetsDescr(value);
        return this;
    }

    @Override
    public FailedTxOutRecord value8(String value) {
        setPaymentCred(value);
        return this;
    }

    @Override
    public FailedTxOutRecord value9(BigInteger value) {
        setValue(value);
        return this;
    }

    @Override
    public FailedTxOutRecord value10(Long value) {
        setInlineDatumId(value);
        return this;
    }

    @Override
    public FailedTxOutRecord value11(Long value) {
        setReferenceScriptId(value);
        return this;
    }

    @Override
    public FailedTxOutRecord value12(Long value) {
        setStakeAddressId(value);
        return this;
    }

    @Override
    public FailedTxOutRecord value13(Long value) {
        setTxId(value);
        return this;
    }

    @Override
    public FailedTxOutRecord values(Long value1, String value2, Boolean value3, byte[] value4, String value5, Short value6, String value7, String value8, BigInteger value9, Long value10, Long value11, Long value12, Long value13) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached FailedTxOutRecord
     */
    public FailedTxOutRecord() {
        super(FailedTxOut.FAILED_TX_OUT);
    }

    /**
     * Create a detached, initialised FailedTxOutRecord
     */
    public FailedTxOutRecord(Long id, String address, Boolean addressHasScript, byte[] addressRaw, String dataHash, Short index, String multiAssetsDescr, String paymentCred, BigInteger value, Long inlineDatumId, Long referenceScriptId, Long stakeAddressId, Long txId) {
        super(FailedTxOut.FAILED_TX_OUT);

        setId(id);
        setAddress(address);
        setAddressHasScript(addressHasScript);
        setAddressRaw(addressRaw);
        setDataHash(dataHash);
        setIndex(index);
        setMultiAssetsDescr(multiAssetsDescr);
        setPaymentCred(paymentCred);
        setValue(value);
        setInlineDatumId(inlineDatumId);
        setReferenceScriptId(referenceScriptId);
        setStakeAddressId(stakeAddressId);
        setTxId(txId);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised FailedTxOutRecord
     */
    public FailedTxOutRecord(org.cardanofoundation.ledgersync.jooq.tables.pojos.FailedTxOut value) {
        super(FailedTxOut.FAILED_TX_OUT);

        if (value != null) {
            setId(value.getId());
            setAddress(value.getAddress());
            setAddressHasScript(value.getAddressHasScript());
            setAddressRaw(value.getAddressRaw());
            setDataHash(value.getDataHash());
            setIndex(value.getIndex());
            setMultiAssetsDescr(value.getMultiAssetsDescr());
            setPaymentCred(value.getPaymentCred());
            setValue(value.getValue());
            setInlineDatumId(value.getInlineDatumId());
            setReferenceScriptId(value.getReferenceScriptId());
            setStakeAddressId(value.getStakeAddressId());
            setTxId(value.getTxId());
            resetChangedOnNotNull();
        }
    }
}
