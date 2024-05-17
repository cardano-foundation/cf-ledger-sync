--
-- Name: multi_asset; Type: INDEX;
--
CREATE INDEX IF NOT EXISTS idx_multi_asset_name_view ON multi_asset (name_view);
CREATE INDEX IF NOT EXISTS idx_multi_asset_unit ON multi_asset (unit);
CREATE UNIQUE INDEX IF NOT EXISTS multi_asset_fingerprint_uindex ON multi_asset (fingerprint);

CREATE INDEX IF NOT EXISTS multi_asset_supply_idx ON multi_asset (supply);
CREATE INDEX IF NOT EXISTS multi_asset_time_idx ON multi_asset ("time");

CREATE EXTENSION IF NOT EXISTS pg_trgm WITH SCHEMA pg_catalog;
CREATE INDEX IF NOT EXISTS name_view_gin_lower ON multi_asset USING gin (lower(name_view) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_name_view_length ON multi_asset (LENGTH(name_view));

--
-- Name: ma_tx_mint; Type: INDEX;
--
CREATE INDEX IF NOT EXISTS idx_ma_tx_mint_tx_id ON ma_tx_mint USING btree (tx_id);

CREATE INDEX IF NOT EXISTS ma_tx_mint_ident_index ON ma_tx_mint (ident);

