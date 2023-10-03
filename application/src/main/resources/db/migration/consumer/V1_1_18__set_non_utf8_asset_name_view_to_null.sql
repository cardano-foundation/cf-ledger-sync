CREATE FUNCTION pg_temp.utf8_asset_name_bytea_to_name_view(bytea) RETURNS varchar(64)
    LANGUAGE plpgsql AS
$$
BEGIN
    RETURN convert_from(decode(replace(encode($1, 'hex'), '00', ''), 'hex'), 'UTF-8');
EXCEPTION
    WHEN character_not_in_repertoire THEN
        RETURN NULL;
END;
$$;

ALTER TABLE multi_asset
    ALTER COLUMN name_view DROP NOT NULL;
UPDATE multi_asset
SET name_view = pg_temp.utf8_asset_name_bytea_to_name_view(name);
