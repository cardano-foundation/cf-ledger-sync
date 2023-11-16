ALTER TABLE unconsume_tx_in ADD COLUMN redeemer_id bigint;

CREATE INDEX IF NOT EXISTS idx_unconsume_tx_in_redeemer_id ON unconsume_tx_in(redeemer_id);
