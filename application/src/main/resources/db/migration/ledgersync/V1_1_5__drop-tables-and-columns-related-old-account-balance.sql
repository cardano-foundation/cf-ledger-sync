DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS address_token;
DROP TABLE IF EXISTS address_token_balance;
DROP TABLE IF EXISTS address_tx_balance;

DROP INDEX IF EXISTS stake_address_balance_idx;
ALTER TABLE stake_address DROP COLUMN IF EXISTS balance;

ALTER TABLE multi_asset DROP COLUMN IF EXISTS tx_count;
ALTER TABLE multi_asset DROP COLUMN IF EXISTS total_volume;