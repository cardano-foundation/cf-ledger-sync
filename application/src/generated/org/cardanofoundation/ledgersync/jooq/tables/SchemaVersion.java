/*
 * This file is generated by jOOQ.
 */
package org.cardanofoundation.ledgersync.jooq.tables;


import java.util.function.Function;

import org.cardanofoundation.ledgersync.jooq.DefaultSchema;
import org.cardanofoundation.ledgersync.jooq.Keys;
import org.cardanofoundation.ledgersync.jooq.tables.records.SchemaVersionRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function4;
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
public class SchemaVersion extends TableImpl<SchemaVersionRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>schema_version</code>
     */
    public static final SchemaVersion SCHEMA_VERSION = new SchemaVersion();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SchemaVersionRecord> getRecordType() {
        return SchemaVersionRecord.class;
    }

    /**
     * The column <code>schema_version.id</code>.
     */
    public final TableField<SchemaVersionRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>schema_version.stage_one</code>.
     */
    public final TableField<SchemaVersionRecord, Long> STAGE_ONE = createField(DSL.name("stage_one"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>schema_version.stage_three</code>.
     */
    public final TableField<SchemaVersionRecord, Long> STAGE_THREE = createField(DSL.name("stage_three"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>schema_version.stage_two</code>.
     */
    public final TableField<SchemaVersionRecord, Long> STAGE_TWO = createField(DSL.name("stage_two"), SQLDataType.BIGINT.nullable(false), this, "");

    private SchemaVersion(Name alias, Table<SchemaVersionRecord> aliased) {
        this(alias, aliased, null);
    }

    private SchemaVersion(Name alias, Table<SchemaVersionRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>schema_version</code> table reference
     */
    public SchemaVersion(String alias) {
        this(DSL.name(alias), SCHEMA_VERSION);
    }

    /**
     * Create an aliased <code>schema_version</code> table reference
     */
    public SchemaVersion(Name alias) {
        this(alias, SCHEMA_VERSION);
    }

    /**
     * Create a <code>schema_version</code> table reference
     */
    public SchemaVersion() {
        this(DSL.name("schema_version"), null);
    }

    public <O extends Record> SchemaVersion(Table<O> child, ForeignKey<O, SchemaVersionRecord> key) {
        super(child, key, SCHEMA_VERSION);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<SchemaVersionRecord> getPrimaryKey() {
        return Keys.SCHEMA_VERSION_PKEY;
    }

    @Override
    public SchemaVersion as(String alias) {
        return new SchemaVersion(DSL.name(alias), this);
    }

    @Override
    public SchemaVersion as(Name alias) {
        return new SchemaVersion(alias, this);
    }

    @Override
    public SchemaVersion as(Table<?> alias) {
        return new SchemaVersion(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public SchemaVersion rename(String name) {
        return new SchemaVersion(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public SchemaVersion rename(Name name) {
        return new SchemaVersion(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public SchemaVersion rename(Table<?> name) {
        return new SchemaVersion(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, Long, Long, Long> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super Long, ? super Long, ? super Long, ? super Long, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function4<? super Long, ? super Long, ? super Long, ? super Long, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
