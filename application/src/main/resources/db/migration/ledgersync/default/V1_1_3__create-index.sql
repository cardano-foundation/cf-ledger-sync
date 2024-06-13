CREATE INDEX IF NOT EXISTS idx_epoch_no ON epoch USING btree (no);
CREATE INDEX IF NOT EXISTS idx_reserved_pool_ticker_pool_hash ON reserved_pool_ticker USING btree (pool_hash);
CREATE INDEX IF NOT EXISTS idx_slot_leader_pool_hash_id ON slot_leader USING btree (pool_hash_id);
CREATE INDEX IF NOT EXISTS idx_tx_block_id ON tx USING btree (block_id);
CREATE INDEX IF NOT EXISTS idx_datum_tx_id ON datum USING btree (tx_id);
CREATE INDEX IF NOT EXISTS idx_extra_key_witness_tx_id ON extra_key_witness USING btree (tx_id);
CREATE INDEX IF NOT EXISTS idx_pool_metadata_ref_registered_tx_id ON pool_metadata_ref USING btree (registered_tx_id);
CREATE INDEX IF NOT EXISTS idx_pool_offline_data_pmr_id ON pool_offline_data USING btree (pmr_id);
CREATE INDEX IF NOT EXISTS idx_pool_offline_fetch_error_pmr_id ON pool_offline_fetch_error USING btree (pmr_id);
CREATE INDEX IF NOT EXISTS idx_pool_retire_announced_tx_id ON pool_retire USING btree (announced_tx_id);
CREATE INDEX IF NOT EXISTS idx_pool_retire_hash_id ON pool_retire USING btree (hash_id);
CREATE INDEX IF NOT EXISTS redeemer_data_tx_id_idx ON redeemer_data USING btree (tx_id);
CREATE INDEX IF NOT EXISTS reference_tx_in_tx_out_id_idx ON reference_tx_in USING btree (tx_out_id);
CREATE INDEX IF NOT EXISTS idx_script_tx_id ON script USING btree (tx_id);
CREATE INDEX IF NOT EXISTS idx_stake_address_hash_raw ON stake_address USING btree (hash_raw);
CREATE INDEX IF NOT EXISTS idx_stake_address_view ON stake_address USING hash (view);
CREATE INDEX IF NOT EXISTS idx_stake_registration_addr_id ON stake_registration USING btree (addr_id);
CREATE INDEX IF NOT EXISTS idx_stake_registration_tx_id ON stake_registration USING btree (tx_id);
CREATE INDEX IF NOT EXISTS idx_treasury_addr_id ON treasury USING btree (addr_id);
CREATE INDEX IF NOT EXISTS idx_treasury_tx_id ON treasury USING btree (tx_id);
CREATE INDEX IF NOT EXISTS idx_tx_out_address ON tx_out USING hash (address);
CREATE INDEX IF NOT EXISTS idx_tx_out_payment_cred ON tx_out USING btree (payment_cred);
CREATE INDEX IF NOT EXISTS idx_tx_out_stake_address_id ON tx_out USING btree (stake_address_id);
CREATE INDEX IF NOT EXISTS idx_tx_out_tx_id ON tx_out USING btree (tx_id);
CREATE INDEX IF NOT EXISTS tx_out_inline_datum_id_idx ON tx_out USING btree (inline_datum_id);
CREATE INDEX IF NOT EXISTS tx_out_reference_script_id_idx ON tx_out USING btree (reference_script_id);
CREATE INDEX IF NOT EXISTS idx_epoch_stake_addr_id ON epoch_stake USING btree (addr_id);
CREATE INDEX IF NOT EXISTS idx_epoch_stake_epoch_no ON epoch_stake USING btree (epoch_no);
CREATE INDEX IF NOT EXISTS idx_epoch_stake_pool_id ON epoch_stake USING btree (pool_id);
CREATE INDEX IF NOT EXISTS idx_ma_tx_out_tx_out_id ON ma_tx_out USING btree (tx_out_id);
CREATE INDEX IF NOT EXISTS idx_pool_update_active_epoch_no ON pool_update USING btree (active_epoch_no);
CREATE INDEX IF NOT EXISTS idx_pool_update_hash_id ON pool_update USING btree (hash_id);
CREATE INDEX IF NOT EXISTS idx_pool_update_meta_id ON pool_update USING btree (meta_id);
CREATE INDEX IF NOT EXISTS idx_pool_update_registered_tx_id ON pool_update USING btree (registered_tx_id);
CREATE INDEX IF NOT EXISTS idx_pool_update_reward_addr ON pool_update USING btree (reward_addr_id);
CREATE INDEX IF NOT EXISTS redeemer_redeemer_data_id_idx ON redeemer USING btree (redeemer_data_id);
CREATE INDEX IF NOT EXISTS idx_reserve_addr_id ON reserve USING btree (addr_id);
CREATE INDEX IF NOT EXISTS idx_reserve_tx_id ON reserve USING btree (tx_id);
CREATE INDEX IF NOT EXISTS idx_reward_addr_id ON reward USING btree (addr_id);
CREATE INDEX IF NOT EXISTS idx_reward_earned_epoch ON reward USING btree (earned_epoch);
CREATE INDEX IF NOT EXISTS idx_reward_pool_id ON reward USING btree (pool_id);
CREATE INDEX IF NOT EXISTS idx_stake_deregistration_addr_id ON stake_deregistration USING btree (addr_id);
CREATE INDEX IF NOT EXISTS idx_stake_deregistration_redeemer_id ON stake_deregistration USING btree (redeemer_id);
CREATE INDEX IF NOT EXISTS idx_stake_deregistration_tx_id ON stake_deregistration USING btree (tx_id);
CREATE INDEX IF NOT EXISTS idx_tx_in_redeemer_id ON tx_in USING btree (redeemer_id);
CREATE INDEX IF NOT EXISTS idx_tx_in_source_tx ON tx_in USING btree (tx_in_id);
CREATE INDEX IF NOT EXISTS idx_tx_in_tx_in_id ON tx_in USING btree (tx_in_id);
CREATE INDEX IF NOT EXISTS idx_tx_in_tx_out_id ON tx_in USING btree (tx_out_id);
CREATE INDEX IF NOT EXISTS idx_withdrawal_addr_id ON withdrawal USING btree (addr_id);
CREATE INDEX IF NOT EXISTS idx_withdrawal_redeemer_id ON withdrawal USING btree (redeemer_id);
CREATE INDEX IF NOT EXISTS idx_withdrawal_tx_id ON withdrawal USING btree (tx_id);
CREATE INDEX IF NOT EXISTS idx_delegation_active_epoch_no ON delegation USING btree (active_epoch_no);
CREATE INDEX IF NOT EXISTS idx_delegation_addr_id ON delegation USING btree (addr_id);
CREATE INDEX IF NOT EXISTS idx_delegation_pool_hash_id ON delegation USING btree (pool_hash_id);
CREATE INDEX IF NOT EXISTS idx_delegation_redeemer_id ON delegation USING btree (redeemer_id);
CREATE INDEX IF NOT EXISTS idx_delegation_tx_id ON delegation USING btree (tx_id);
CREATE INDEX IF NOT EXISTS pool_owner_pool_update_id_idx ON pool_owner USING btree (pool_update_id);
CREATE INDEX IF NOT EXISTS idx_pool_relay_update_id ON pool_relay USING btree (update_id);

CREATE INDEX IF NOT EXISTS extra_key_witness_hash_index ON extra_key_witness (hash);
CREATE INDEX IF NOT EXISTS ma_tx_out_ident_index ON ma_tx_out (ident);

CREATE INDEX IF NOT EXISTS pool_offline_data_pool_id_index ON pool_offline_data (pool_id);
CREATE INDEX IF NOT EXISTS redeemer_data_hash_index ON redeemer_data (hash);
CREATE INDEX IF NOT EXISTS slot_leader_hash_index ON slot_leader (hash);

CREATE UNIQUE INDEX IF NOT EXISTS datum_hash_uindex ON datum (hash);
CREATE UNIQUE INDEX IF NOT EXISTS pool_hash_hash_raw_index ON pool_hash (hash_raw);
CREATE UNIQUE INDEX IF NOT EXISTS script_hash_uindex ON script (hash);

-- Reward index
CREATE INDEX IF NOT EXISTS reward_earned_epoch_idx ON reward (earned_epoch);
CREATE INDEX IF NOT EXISTS reward_spendable_epoch_idx ON reward (spendable_epoch);
CREATE INDEX IF NOT EXISTS reward_type_idx ON reward ("type");
CREATE INDEX IF NOT EXISTS reward_addr_id_idx ON reward (addr_id);
CREATE INDEX IF NOT EXISTS reward_pool_id_idx ON reward (pool_id);


-- Optimize index for query

-- change index idx_tx_block_id of table tx:
CREATE INDEX IF NOT EXISTS tx_block_id_id_idx
    ON tx (block_id, id);

CREATE INDEX IF NOT EXISTS tx_metadata_hash_idx
    ON tx_metadata_hash (hash);

CREATE INDEX IF NOT EXISTS tx__tx_metadata_hash_idx
    ON tx (tx_metadata_hash_id);

CREATE EXTENSION IF NOT EXISTS pg_trgm WITH SCHEMA pg_catalog;
CREATE INDEX IF NOT EXISTS pool_name_gin_lower ON pool_offline_data USING gin (lower(pool_name) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS tx_fee_idx ON tx USING btree (fee);
CREATE INDEX IF NOT EXISTS tx_out_sum_idx ON tx USING btree (out_sum);

CREATE INDEX IF NOT EXISTS idx_unconsume_tx_in_redeemer_id ON unconsume_tx_in(redeemer_id);

CREATE INDEX IF NOT EXISTS idx_pool_name_length ON pool_offline_data (LENGTH(pool_name));

CREATE INDEX IF NOT EXISTS tx_witnesses_tx_id_idx ON tx_witnesses (tx_id);
CREATE INDEX IF NOT EXISTS tx_bootstrap_witnesses_tx_id_idx ON tx_bootstrap_witnesses (tx_id);

CREATE INDEX IF NOT EXISTS redeemer_script_hash_idx ON redeemer (script_hash);

CREATE INDEX IF NOT EXISTS redeemer_script_hash_tx_id_idx ON redeemer (script_hash, tx_id);
CREATE INDEX IF NOT EXISTS stake_address_script_hash_idx ON stake_address (script_hash);