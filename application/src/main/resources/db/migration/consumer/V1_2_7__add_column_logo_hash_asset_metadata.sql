TRUNCATE TABLE asset_metadata;
ALTER SEQUENCE asset_metadata_id_seq RESTART;
ALTER TABLE asset_metadata ADD COLUMN IF NOT EXISTS logo_hash VARCHAR(64) NULL;