--
-- Name: param_proposal; Type: INDEX;
--
CREATE INDEX IF NOT EXISTS idx_param_proposal_cost_model_id ON param_proposal USING btree (cost_model_id);
CREATE INDEX IF NOT EXISTS idx_param_proposal_registered_tx_id ON param_proposal USING btree (registered_tx_id);


--
-- Name: epoch_param; Type: INDEX;
--
CREATE INDEX IF NOT EXISTS idx_epoch_param_block_id ON epoch_param USING btree (block_id);
CREATE INDEX IF NOT EXISTS idx_epoch_param_cost_model_id ON epoch_param USING btree (cost_model_id);