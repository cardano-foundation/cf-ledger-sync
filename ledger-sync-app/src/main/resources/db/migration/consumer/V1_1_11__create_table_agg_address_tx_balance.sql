CREATE TABLE IF NOT EXISTS agg_address_tx_balance
(
    id               bigserial
        PRIMARY KEY,
    stake_address_id bigint,
    address_id       bigint      NOT NULL,
    balance          NUMERIC(39) NOT NULL,
    day              DATE        NOT NULL
);

INSERT INTO agg_address_tx_balance (stake_address_id, address_id, balance, day)
SELECT addr.stake_address_id       AS stake_address_id,
       addr.id                     AS address_id,
       SUM(atb.balance)            AS sum_balance,
       date_trunc('day', atb.time) AS time_agg
FROM address_tx_balance atb
         INNER JOIN address addr ON atb.address_id = addr.id
         LEFT JOIN (SELECT b_temp.id, b_temp.time AS blockTime
                    FROM block b_temp
                    WHERE b_temp.tx_count > 0
                    ORDER BY b_temp.id DESC
                    limit 1) max_block ON 1 = 1
WHERE atb.time < date_trunc('day', max_block.blockTime)
GROUP BY addr.id, time_agg
ORDER BY time_agg;

CREATE INDEX IF NOT EXISTS agg_address_tx_balance_day_idx
    ON agg_address_tx_balance (day DESC);

CREATE INDEX IF NOT EXISTS agg_address_tx_balance_stake_address_id_day_balance_idx
    ON agg_address_tx_balance (stake_address_id, day, balance);

CREATE INDEX IF NOT EXISTS agg_address_tx_balance_address_id_day_balance_idx
    ON agg_address_tx_balance (address_id, day, balance);

CREATE INDEX IF NOT EXISTS agg_address_tx_balance_day_index
    ON agg_address_tx_balance (day DESC);
