-- CREATE COMPOSITE INDEX FOR address_token_balance
CREATE INDEX IF NOT EXISTS address_token_balance_ident_stake_address_id_balance_idx
    ON address_token_balance (ident, stake_address_id, balance);

-- CREATE COMPOSITE INDEX FOR address_token
CREATE INDEX IF NOT EXISTS address_token_ident_tx_id_balance_idx
    ON address_token (ident, tx_id, balance);

-- CREATE COMPOSITE INDEX FOR address_tx_balance
CREATE INDEX IF NOT EXISTS address_tx_balance_stake_address_id_tx_id_balance_idx
    ON address_tx_balance (stake_address_id, tx_id, balance);
