CREATE INDEX IF NOT EXISTS idx_name_view_length ON multi_asset (LENGTH(name_view));
CREATE INDEX IF NOT EXISTS idx_pool_name_length ON pool_offline_data (LENGTH(pool_name));