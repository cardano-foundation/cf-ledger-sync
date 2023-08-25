ALTER TABLE address_token_balance
    ADD COLUMN stake_address_id bigint;

UPDATE address_token_balance atb
SET stake_address_id = (SELECT stake_address_id FROM address a WHERE a.id = atb.address_id);

CREATE INDEX IF NOT EXISTS address_token_balance_stake_address_id_idx ON address_token_balance (stake_address_id);

ALTER TABLE address_tx_balance
    ADD COLUMN stake_address_id bigint;

UPDATE address_tx_balance atb
SET stake_address_id = (SELECT stake_address_id FROM address a WHERE a.id = atb.address_id);

CREATE INDEX IF NOT EXISTS address_tx_balance_stake_address_id_idx ON address_tx_balance (stake_address_id);
