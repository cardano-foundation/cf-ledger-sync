DROP INDEX IF EXISTS tx_metadata_ts_json_idx;
ALTER TABLE tx_metadata DROP COLUMN IF EXISTS ts_json;
ALTER TABLE datum ALTER COLUMN value TYPE text USING value::text;
ALTER TABLE tx_metadata ALTER COLUMN "json" TYPE text USING "json"::text;
ALTER TABLE cost_model ALTER COLUMN costs TYPE text USING costs::text;
ALTER TABLE script ALTER COLUMN "json" TYPE text USING "json"::text;