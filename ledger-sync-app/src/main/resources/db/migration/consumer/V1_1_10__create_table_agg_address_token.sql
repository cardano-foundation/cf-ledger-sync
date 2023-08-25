CREATE TABLE IF NOT EXISTS agg_address_token
(
    id      bigserial
        primary key,
    balance numeric(39) not null,
    ident   bigint      not null,
    day     date
);

INSERT INTO agg_address_token (ident, balance, day)
SELECT addt.ident                AS ident,
       sum(addt.balance)         AS sum_balance,
       date_trunc('day', b.time) AS time_agg
FROM address_token addt
         INNER JOIN multi_asset ma on addt.ident = ma.id
         INNER JOIN tx t on addt.tx_id = t.id
         INNER JOIN block b on t.block_id = b.id
         LEFT JOIN (SELECT b_temp.id, b_temp.time AS blockTime
                    FROM block b_temp
                    WHERE b_temp.tx_count > 0
                    ORDER BY b_temp.id DESC
                    limit 1) max_block ON 1 = 1
WHERE b.time < date_trunc('day', max_block.blockTime)
  AND b.tx_count > 0
  AND addt.balance > 0
GROUP BY addt.ident, time_agg
ORDER BY time_agg;

CREATE INDEX IF NOT EXISTS agg_address_token_day_idx
    ON agg_address_token (day DESC);

CREATE INDEX IF NOT EXISTS agg_address_token_ident_day_balance_idx
    ON agg_address_token (ident, day, balance);
