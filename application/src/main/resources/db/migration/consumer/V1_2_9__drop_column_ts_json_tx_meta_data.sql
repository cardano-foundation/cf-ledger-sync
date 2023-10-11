DROP INDEX IF EXISTS tx_metadata_ts_json_idx;
ALTER TABLE tx_metadata DROP COLUMN IF EXISTS ts_json;