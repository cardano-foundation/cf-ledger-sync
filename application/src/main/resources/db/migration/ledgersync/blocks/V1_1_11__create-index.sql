--
-- Name: block; Type: INDEX;
--

CREATE INDEX IF NOT EXISTS idx_block_block_no ON block USING btree (block_no);
CREATE INDEX IF NOT EXISTS idx_block_epoch_no ON block USING btree (epoch_no);
CREATE INDEX IF NOT EXISTS idx_block_previous_id ON block USING btree (previous_id);
CREATE INDEX IF NOT EXISTS idx_block_slot_leader_id ON block USING btree (slot_leader_id);
CREATE INDEX IF NOT EXISTS idx_block_slot_no ON block USING btree (slot_no);
CREATE INDEX IF NOT EXISTS idx_block_time ON block USING btree ("time");
CREATE INDEX IF NOT EXISTS idx_block_id_time_tx_count ON block (id, "time", tx_count);
CREATE INDEX IF NOT EXISTS tx_count_idx ON block USING btree (tx_count);