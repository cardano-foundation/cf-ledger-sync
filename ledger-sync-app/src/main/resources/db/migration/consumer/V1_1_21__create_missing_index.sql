CREATE INDEX IF NOT EXISTS address_balance_idx ON address USING btree (balance);
CREATE INDEX IF NOT EXISTS address_tx_count_idx ON address USING btree (tx_count);
CREATE UNIQUE INDEX IF NOT EXISTS address_tx_balance_tx_id_idx ON address_tx_balance USING btree (tx_id, address_id);
CREATE INDEX IF NOT EXISTS multi_asset_supply_idx ON multi_asset (supply);
CREATE INDEX IF NOT EXISTS multi_asset_time_idx ON multi_asset ("time");

CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE INDEX IF NOT EXISTS name_view_gin_lower ON multi_asset USING gin (lower(name_view) gin_trgm_ops);
CREATE INDEX IF NOT EXISTS pool_name_gin_lower ON pool_offline_data USING gin (lower(pool_name) gin_trgm_ops);
CREATE INDEX IF NOT EXISTS asset_metadata_subject_idx ON asset_metadata (subject);
CREATE INDEX IF NOT EXISTS tx_count_idx ON block USING btree (tx_count)
