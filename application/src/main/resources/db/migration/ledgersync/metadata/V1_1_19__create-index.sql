--
-- Name: multi_asset; Type: INDEX;
--

CREATE INDEX IF NOT EXISTS idx_tx_metadata_tx_id ON tx_metadata USING btree (tx_id);