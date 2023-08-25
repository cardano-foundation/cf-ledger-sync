CREATE FUNCTION pg_temp.asset_name_bytea_to_name_view(bytea) RETURNS varchar(64)
    LANGUAGE plpgsql AS
$$
BEGIN
    RETURN convert_from(decode(replace(encode($1, 'hex'), '00', ''), 'hex'), 'UTF-8');
EXCEPTION
    WHEN character_not_in_repertoire THEN
        RETURN encode($1, 'hex');
END;
$$;

ALTER TABLE multi_asset
    ADD COLUMN name_view varchar(64);
UPDATE multi_asset
SET name_view = pg_temp.asset_name_bytea_to_name_view(name);
ALTER TABLE multi_asset
    ALTER COLUMN name_view SET NOT NULL;

CREATE INDEX IF NOT EXISTS idx_multi_asset_name_view ON multi_asset (name_view);
