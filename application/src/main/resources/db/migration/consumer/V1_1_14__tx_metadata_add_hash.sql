CREATE TABLE IF NOT EXISTS tx_metadata_hash
(
    id    bigserial   NOT NULL
        PRIMARY KEY,
    hash  VARCHAR(64) NOT NULL,
    tx_id bigint      NOT NULL
);

CREATE INDEX IF NOT EXISTS tx_metadata_hash_idx
    ON tx_metadata_hash (hash);

CREATE INDEX IF NOT EXISTS tx_metadata_tx_id_idx
    ON tx_metadata_hash (tx_id);

ALTER TABLE tx
    ADD COLUMN IF NOT EXISTS tx_metadata_hash_id bigint NULL;

CREATE INDEX IF NOT EXISTS tx__tx_metadata_hash_idx
    ON tx (tx_metadata_hash_id);
