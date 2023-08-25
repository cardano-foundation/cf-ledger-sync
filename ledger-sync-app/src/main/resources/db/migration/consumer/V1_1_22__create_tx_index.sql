CREATE INDEX IF NOT EXISTS tx_fee_idx ON tx USING btree (fee);
CREATE INDEX IF NOT EXISTS tx_out_sum_idx ON tx USING btree (out_sum);