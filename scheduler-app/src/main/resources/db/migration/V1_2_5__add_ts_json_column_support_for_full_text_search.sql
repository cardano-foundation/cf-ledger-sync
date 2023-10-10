ALTER TABLE tx_metadata ADD COLUMN ts_json tsvector
GENERATED ALWAYS AS (to_tsvector('simple', tx_metadata."json")) STORED;

CREATE INDEX IF NOT EXISTS tx_metadata_ts_json_idx ON tx_metadata USING GIN (ts_json);