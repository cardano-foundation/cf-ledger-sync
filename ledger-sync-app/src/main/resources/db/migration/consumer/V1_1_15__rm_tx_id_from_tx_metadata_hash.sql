ALTER TABLE tx_metadata_hash
    DROP COLUMN IF EXISTS tx_id;

DROP INDEX IF EXISTS tx_metadata_tx_id_idx;



