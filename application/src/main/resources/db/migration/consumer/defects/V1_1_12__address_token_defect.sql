--- address_token ---
TRUNCATE address_token;
ALTER SEQUENCE address_token_id_seq RESTART;

DROP INDEX IF EXISTS idx_address_token_tx_id;
DROP INDEX IF EXISTS idx_address_token_ident;
DROP INDEX IF EXISTS idx_address_token_address_id;
DROP INDEX IF EXISTS address_token_ident_stake_tx_id_balance_idx;
DROP INDEX IF EXISTS address_token_ident_tx_id_balance_idx;

INSERT INTO address_token (balance, tx_id, ident, address_id)
SELECT (COALESCE(a.balance, 0) - COALESCE(b.balance, 0)) as balance,
       CASE
           WHEN a.tx_id IS NULL THEN b.tx_id
           ELSE a.tx_id
           END,
       CASE
           WHEN a.ident IS NULL THEN b.ident
           ELSE a.ident
           END,
       CASE
           WHEN a.address_id IS NULL THEN b.address_id
           ELSE a.address_id
           END
FROM (SELECT DISTINCT tx_id                   AS tx_id,
                      address.id              AS address_id,
                      ma_tx_out.ident         as ident,
                      sum(ma_tx_out.quantity) AS balance
      FROM tx_out
               INNER JOIN ma_tx_out ON ma_tx_out.tx_out_id = tx_out.id
               INNER JOIN address ON tx_out.address = address.address
      GROUP BY address.id, tx_out.tx_id, ma_tx_out.ident) AS a
         FULL JOIN
     (SELECT DISTINCT tx_in_id                AS tx_id,
                      address.id              AS address_id,
                      ma_tx_out.ident         as ident,
                      sum(ma_tx_out.quantity) AS balance
      FROM tx_in
               INNER JOIN tx_out
                          ON tx_in.tx_out_id = tx_out.tx_id AND tx_in.tx_out_index = tx_out.index
               INNER JOIN ma_tx_out on ma_tx_out.tx_out_id = tx_out.id
               INNER JOIN address on tx_out.address = address.address
      GROUP BY address.id, tx_in_id, ma_tx_out.ident) AS b
     ON a.tx_id = b.tx_id AND a.address_id = b.address_id and a.ident = b.ident;

CREATE INDEX idx_address_token_tx_id ON address_token (tx_id);
CREATE INDEX idx_address_token_ident ON address_token (ident);
CREATE INDEX idx_address_token_address_id ON address_token (address_id);
CREATE INDEX address_token_ident_stake_tx_id_balance_idx ON address_token (ident, tx_id, balance);
CREATE INDEX address_token_ident_tx_id_balance_idx ON address_token (ident, tx_id, balance);

--- address_token_balance ---
TRUNCATE address_token_balance;
ALTER SEQUENCE address_token_balance_id_seq RESTART;

DROP INDEX IF EXISTS idx_address_token_balance_address_id;
DROP INDEX IF EXISTS idx_address_token_balance_ident;
DROP INDEX IF EXISTS idx_address_token_balance_address_id_ident;
DROP INDEX IF EXISTS idx_address_token_balance_ident_address_id;
DROP INDEX IF EXISTS address_token_balance_ident_stake_address_id_balance_idx;
DROP INDEX IF EXISTS address_token_balance_stake_address_id_idx;

INSERT INTO address_token_balance(address_id, ident, balance, stake_address_id)
SELECT address_id, ident, sum(at2.balance), a.stake_address_id
FROM address_token at2
         JOIN address a on a.id = at2.address_id
GROUP BY address_id, ident, a.stake_address_id;

CREATE INDEX IF NOT EXISTS idx_address_token_balance_address_id ON address_token_balance (address_id);
CREATE INDEX IF NOT EXISTS idx_address_token_balance_ident ON address_token_balance (ident);
CREATE INDEX IF NOT EXISTS idx_address_token_balance_address_id_ident ON address_token_balance (address_id, ident);
CREATE INDEX IF NOT EXISTS idx_address_token_balance_ident_address_id ON address_token_balance (ident, address_id);
CREATE INDEX IF NOT EXISTS address_token_balance_ident_stake_address_id_balance_idx ON address_token_balance (ident, stake_address_id, balance);
CREATE INDEX IF NOT EXISTS address_token_balance_stake_address_id_idx ON address_token_balance (stake_address_id);
