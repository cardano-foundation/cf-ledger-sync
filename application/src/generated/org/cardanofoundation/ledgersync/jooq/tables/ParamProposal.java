/*
 * This file is generated by jOOQ.
 */
package org.cardanofoundation.ledgersync.jooq.tables;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.cardanofoundation.ledgersync.jooq.DefaultSchema;
import org.cardanofoundation.ledgersync.jooq.Indexes;
import org.cardanofoundation.ledgersync.jooq.Keys;
import org.cardanofoundation.ledgersync.jooq.tables.records.ParamProposalRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
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
public class ParamProposal extends TableImpl<ParamProposalRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>param_proposal</code>
     */
    public static final ParamProposal PARAM_PROPOSAL = new ParamProposal();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ParamProposalRecord> getRecordType() {
        return ParamProposalRecord.class;
    }

    /**
     * The column <code>param_proposal.id</code>.
     */
    public final TableField<ParamProposalRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>param_proposal.coins_per_utxo_size</code>.
     */
    public final TableField<ParamProposalRecord, BigDecimal> COINS_PER_UTXO_SIZE = createField(DSL.name("coins_per_utxo_size"), SQLDataType.NUMERIC(19, 2), this, "");

    /**
     * The column <code>param_proposal.collateral_percent</code>.
     */
    public final TableField<ParamProposalRecord, Integer> COLLATERAL_PERCENT = createField(DSL.name("collateral_percent"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>param_proposal.decentralisation</code>.
     */
    public final TableField<ParamProposalRecord, Double> DECENTRALISATION = createField(DSL.name("decentralisation"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>param_proposal.entropy</code>.
     */
    public final TableField<ParamProposalRecord, String> ENTROPY = createField(DSL.name("entropy"), SQLDataType.VARCHAR(64), this, "");

    /**
     * The column <code>param_proposal.epoch_no</code>.
     */
    public final TableField<ParamProposalRecord, Integer> EPOCH_NO = createField(DSL.name("epoch_no"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>param_proposal.influence</code>.
     */
    public final TableField<ParamProposalRecord, Double> INFLUENCE = createField(DSL.name("influence"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>param_proposal.key</code>.
     */
    public final TableField<ParamProposalRecord, String> KEY = createField(DSL.name("key"), SQLDataType.VARCHAR(56).nullable(false), this, "");

    /**
     * The column <code>param_proposal.key_deposit</code>.
     */
    public final TableField<ParamProposalRecord, BigDecimal> KEY_DEPOSIT = createField(DSL.name("key_deposit"), SQLDataType.NUMERIC(19, 2), this, "");

    /**
     * The column <code>param_proposal.max_bh_size</code>.
     */
    public final TableField<ParamProposalRecord, BigInteger> MAX_BH_SIZE = createField(DSL.name("max_bh_size"), SQLDataType.DECIMAL_INTEGER(20), this, "");

    /**
     * The column <code>param_proposal.max_block_ex_mem</code>.
     */
    public final TableField<ParamProposalRecord, BigInteger> MAX_BLOCK_EX_MEM = createField(DSL.name("max_block_ex_mem"), SQLDataType.DECIMAL_INTEGER(20), this, "");

    /**
     * The column <code>param_proposal.max_block_ex_steps</code>.
     */
    public final TableField<ParamProposalRecord, BigInteger> MAX_BLOCK_EX_STEPS = createField(DSL.name("max_block_ex_steps"), SQLDataType.DECIMAL_INTEGER(20), this, "");

    /**
     * The column <code>param_proposal.max_block_size</code>.
     */
    public final TableField<ParamProposalRecord, BigInteger> MAX_BLOCK_SIZE = createField(DSL.name("max_block_size"), SQLDataType.DECIMAL_INTEGER(20), this, "");

    /**
     * The column <code>param_proposal.max_collateral_inputs</code>.
     */
    public final TableField<ParamProposalRecord, Integer> MAX_COLLATERAL_INPUTS = createField(DSL.name("max_collateral_inputs"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>param_proposal.max_epoch</code>.
     */
    public final TableField<ParamProposalRecord, BigInteger> MAX_EPOCH = createField(DSL.name("max_epoch"), SQLDataType.DECIMAL_INTEGER(20), this, "");

    /**
     * The column <code>param_proposal.max_tx_ex_mem</code>.
     */
    public final TableField<ParamProposalRecord, BigInteger> MAX_TX_EX_MEM = createField(DSL.name("max_tx_ex_mem"), SQLDataType.DECIMAL_INTEGER(20), this, "");

    /**
     * The column <code>param_proposal.max_tx_ex_steps</code>.
     */
    public final TableField<ParamProposalRecord, BigInteger> MAX_TX_EX_STEPS = createField(DSL.name("max_tx_ex_steps"), SQLDataType.DECIMAL_INTEGER(20), this, "");

    /**
     * The column <code>param_proposal.max_tx_size</code>.
     */
    public final TableField<ParamProposalRecord, BigInteger> MAX_TX_SIZE = createField(DSL.name("max_tx_size"), SQLDataType.DECIMAL_INTEGER(20), this, "");

    /**
     * The column <code>param_proposal.max_val_size</code>.
     */
    public final TableField<ParamProposalRecord, BigInteger> MAX_VAL_SIZE = createField(DSL.name("max_val_size"), SQLDataType.DECIMAL_INTEGER(20), this, "");

    /**
     * The column <code>param_proposal.min_fee_a</code>.
     */
    public final TableField<ParamProposalRecord, BigInteger> MIN_FEE_A = createField(DSL.name("min_fee_a"), SQLDataType.DECIMAL_INTEGER(20), this, "");

    /**
     * The column <code>param_proposal.min_fee_b</code>.
     */
    public final TableField<ParamProposalRecord, BigInteger> MIN_FEE_B = createField(DSL.name("min_fee_b"), SQLDataType.DECIMAL_INTEGER(20), this, "");

    /**
     * The column <code>param_proposal.min_pool_cost</code>.
     */
    public final TableField<ParamProposalRecord, BigInteger> MIN_POOL_COST = createField(DSL.name("min_pool_cost"), SQLDataType.DECIMAL_INTEGER(20), this, "");

    /**
     * The column <code>param_proposal.min_utxo_value</code>.
     */
    public final TableField<ParamProposalRecord, BigInteger> MIN_UTXO_VALUE = createField(DSL.name("min_utxo_value"), SQLDataType.DECIMAL_INTEGER(20), this, "");

    /**
     * The column <code>param_proposal.monetary_expand_rate</code>.
     */
    public final TableField<ParamProposalRecord, Double> MONETARY_EXPAND_RATE = createField(DSL.name("monetary_expand_rate"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>param_proposal.optimal_pool_count</code>.
     */
    public final TableField<ParamProposalRecord, BigInteger> OPTIMAL_POOL_COUNT = createField(DSL.name("optimal_pool_count"), SQLDataType.DECIMAL_INTEGER(20), this, "");

    /**
     * The column <code>param_proposal.pool_deposit</code>.
     */
    public final TableField<ParamProposalRecord, BigInteger> POOL_DEPOSIT = createField(DSL.name("pool_deposit"), SQLDataType.DECIMAL_INTEGER(20), this, "");

    /**
     * The column <code>param_proposal.price_mem</code>.
     */
    public final TableField<ParamProposalRecord, Double> PRICE_MEM = createField(DSL.name("price_mem"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>param_proposal.price_step</code>.
     */
    public final TableField<ParamProposalRecord, Double> PRICE_STEP = createField(DSL.name("price_step"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>param_proposal.protocol_major</code>.
     */
    public final TableField<ParamProposalRecord, Integer> PROTOCOL_MAJOR = createField(DSL.name("protocol_major"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>param_proposal.protocol_minor</code>.
     */
    public final TableField<ParamProposalRecord, Integer> PROTOCOL_MINOR = createField(DSL.name("protocol_minor"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>param_proposal.treasury_growth_rate</code>.
     */
    public final TableField<ParamProposalRecord, Double> TREASURY_GROWTH_RATE = createField(DSL.name("treasury_growth_rate"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>param_proposal.cost_model_id</code>.
     */
    public final TableField<ParamProposalRecord, Long> COST_MODEL_ID = createField(DSL.name("cost_model_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>param_proposal.registered_tx_id</code>.
     */
    public final TableField<ParamProposalRecord, Long> REGISTERED_TX_ID = createField(DSL.name("registered_tx_id"), SQLDataType.BIGINT.nullable(false), this, "");

    private ParamProposal(Name alias, Table<ParamProposalRecord> aliased) {
        this(alias, aliased, null);
    }

    private ParamProposal(Name alias, Table<ParamProposalRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>param_proposal</code> table reference
     */
    public ParamProposal(String alias) {
        this(DSL.name(alias), PARAM_PROPOSAL);
    }

    /**
     * Create an aliased <code>param_proposal</code> table reference
     */
    public ParamProposal(Name alias) {
        this(alias, PARAM_PROPOSAL);
    }

    /**
     * Create a <code>param_proposal</code> table reference
     */
    public ParamProposal() {
        this(DSL.name("param_proposal"), null);
    }

    public <O extends Record> ParamProposal(Table<O> child, ForeignKey<O, ParamProposalRecord> key) {
        super(child, key, PARAM_PROPOSAL);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.IDX_PARAM_PROPOSAL_COST_MODEL_ID, Indexes.IDX_PARAM_PROPOSAL_REGISTERED_TX_ID);
    }

    @Override
    public Identity<ParamProposalRecord, Long> getIdentity() {
        return (Identity<ParamProposalRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<ParamProposalRecord> getPrimaryKey() {
        return Keys.PARAM_PROPOSAL_PKEY;
    }

    @Override
    public List<UniqueKey<ParamProposalRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.UNIQUE_PARAM_PROPOSAL);
    }

    @Override
    public ParamProposal as(String alias) {
        return new ParamProposal(DSL.name(alias), this);
    }

    @Override
    public ParamProposal as(Name alias) {
        return new ParamProposal(alias, this);
    }

    @Override
    public ParamProposal as(Table<?> alias) {
        return new ParamProposal(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public ParamProposal rename(String name) {
        return new ParamProposal(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ParamProposal rename(Name name) {
        return new ParamProposal(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public ParamProposal rename(Table<?> name) {
        return new ParamProposal(name.getQualifiedName(), null);
    }
}
