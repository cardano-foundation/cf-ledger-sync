TRUNCATE TABLE pool_offline_data;
ALTER SEQUENCE pool_offline_data_id_seq RESTART;
TRUNCATE TABLE pool_offline_fetch_error;
ALTER SEQUENCE pool_offline_fetch_error_id_seq RESTART;

CREATE INDEX pool_offline_fetch_error_pool_id_idx ON pool_offline_fetch_error (pool_id);
CREATE INDEX pool_offline_fetch_error_pool_id_pmr_id_idx ON pool_offline_fetch_error (pool_id,pmr_id);

ALTER TABLE pool_offline_data DROP CONSTRAINT unique_pool_offline_data;
ALTER TABLE pool_offline_data ADD CONSTRAINT pool_offline_data_pool_id_unique UNIQUE (pool_id);