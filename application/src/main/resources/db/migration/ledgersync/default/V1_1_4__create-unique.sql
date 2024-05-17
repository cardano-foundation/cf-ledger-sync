--
-- Name: ada_pots uk_ada_pots_block_id; Type: CONSTRAINT;
--

ALTER TABLE ONLY ada_pots
    ADD CONSTRAINT uk_ada_pots_block_id UNIQUE (block_id);

--
-- Name: ada_pots unique_ada_pots; Type: CONSTRAINT;
--

ALTER TABLE ONLY ada_pots
    ADD CONSTRAINT unique_ada_pots UNIQUE (block_id);



--
-- Name: failed_tx_out unique_col_failed_txout; Type: CONSTRAINT;
--

ALTER TABLE ONLY failed_tx_out
    ADD CONSTRAINT unique_col_failed_txout UNIQUE (tx_id, index);


--
-- Name: unconsume_tx_in unique_col_txin; Type: CONSTRAINT;
--

ALTER TABLE ONLY unconsume_tx_in
    ADD CONSTRAINT unique_col_txin UNIQUE (tx_in_id, tx_out_id, tx_out_index);



--
-- Name: datum unique_datum; Type: CONSTRAINT;
--

ALTER TABLE ONLY datum
    ADD CONSTRAINT unique_datum UNIQUE (hash);


--
-- Name: delegation unique_delegation; Type: CONSTRAINT;
--

ALTER TABLE ONLY delegation
    ADD CONSTRAINT unique_delegation UNIQUE (tx_id, cert_index);


--
-- Name: delisted_pool unique_delisted_pool; Type: CONSTRAINT;
--

ALTER TABLE ONLY delisted_pool
    ADD CONSTRAINT unique_delisted_pool UNIQUE (hash_raw);


--
-- Name: epoch unique_epoch; Type: CONSTRAINT;
--

ALTER TABLE ONLY epoch
    ADD CONSTRAINT unique_epoch UNIQUE (no);



--
-- Name: epoch_sync_time unique_epoch_sync_time; Type: CONSTRAINT;
--

ALTER TABLE ONLY epoch_sync_time
    ADD CONSTRAINT unique_epoch_sync_time UNIQUE (no);


--
-- Name: ma_tx_out unique_ma_tx_out; Type: CONSTRAINT;
--

ALTER TABLE ONLY ma_tx_out
    ADD CONSTRAINT unique_ma_tx_out UNIQUE (ident, tx_out_id);


--
-- Name: meta unique_meta; Type: CONSTRAINT;
--

ALTER TABLE ONLY meta
    ADD CONSTRAINT unique_meta UNIQUE (start_time);



--
-- Name: pool_metadata_ref unique_pool_metadata_ref; Type: CONSTRAINT;
--

ALTER TABLE ONLY pool_metadata_ref
    ADD CONSTRAINT unique_pool_metadata_ref UNIQUE (pool_id, url, hash);


--
-- Name: pool_offline_data pool_offline_data_pool_id_unique; Type: CONSTRAINT;
--
ALTER TABLE pool_offline_data ADD CONSTRAINT pool_offline_data_pool_id_unique UNIQUE (pool_id);

--
-- Name: pool_owner unique_pool_owner; Type: CONSTRAINT;
--

ALTER TABLE ONLY pool_owner
    ADD CONSTRAINT unique_pool_owner UNIQUE (addr_id, pool_update_id);


--
-- Name: pool_relay unique_pool_relay; Type: CONSTRAINT;
--

ALTER TABLE ONLY pool_relay
    ADD CONSTRAINT unique_pool_relay UNIQUE (update_id, ipv4, ipv6, dns_name);


--
-- Name: pool_retire unique_pool_retiring; Type: CONSTRAINT;
--

ALTER TABLE ONLY pool_retire
    ADD CONSTRAINT unique_pool_retiring UNIQUE (announced_tx_id, cert_index);


--
-- Name: pool_update unique_pool_update; Type: CONSTRAINT;
--

ALTER TABLE ONLY pool_update
    ADD CONSTRAINT unique_pool_update UNIQUE (registered_tx_id, cert_index);


--
-- Name: pot_transfer unique_pot_transfer; Type: CONSTRAINT;
--

ALTER TABLE ONLY pot_transfer
    ADD CONSTRAINT unique_pot_transfer UNIQUE (tx_id, cert_index);


--
-- Name: redeemer unique_redeemer; Type: CONSTRAINT;
--

ALTER TABLE ONLY redeemer
    ADD CONSTRAINT unique_redeemer UNIQUE (tx_id, purpose, index);


--
-- Name: redeemer_data unique_redeemer_data; Type: CONSTRAINT;
--

ALTER TABLE ONLY redeemer_data
    ADD CONSTRAINT unique_redeemer_data UNIQUE (hash);


--
-- Name: reference_tx_in unique_ref_txin; Type: CONSTRAINT;
--

ALTER TABLE ONLY reference_tx_in
    ADD CONSTRAINT unique_ref_txin UNIQUE (tx_in_id, tx_out_id, tx_out_index);


--
-- Name: reserved_pool_ticker unique_reserved_pool_ticker; Type: CONSTRAINT;
--

ALTER TABLE ONLY reserved_pool_ticker
    ADD CONSTRAINT unique_reserved_pool_ticker UNIQUE (name);


--
-- Name: reserve unique_reserves; Type: CONSTRAINT;
--

ALTER TABLE ONLY reserve
    ADD CONSTRAINT unique_reserves UNIQUE (addr_id, tx_id, cert_index);


--
-- Name: reward unique_reward; Type: CONSTRAINT;
--

ALTER TABLE ONLY reward
    ADD CONSTRAINT unique_reward UNIQUE (addr_id, type, earned_epoch, pool_id);


--
-- Name: slot_leader unique_slot_leader; Type: CONSTRAINT;
--

ALTER TABLE ONLY slot_leader
    ADD CONSTRAINT unique_slot_leader UNIQUE (hash);


--
-- Name: epoch_stake unique_stake; Type: CONSTRAINT;
--

ALTER TABLE ONLY epoch_stake
    ADD CONSTRAINT unique_stake UNIQUE (epoch_no, addr_id, pool_id);


--
-- Name: stake_address unique_stake_address; Type: CONSTRAINT;
--

ALTER TABLE ONLY stake_address
    ADD CONSTRAINT unique_stake_address UNIQUE (hash_raw);


--
-- Name: stake_deregistration unique_stake_deregistration; Type: CONSTRAINT;
--

ALTER TABLE ONLY stake_deregistration
    ADD CONSTRAINT unique_stake_deregistration UNIQUE (tx_id, cert_index);


--
-- Name: stake_registration unique_stake_registration; Type: CONSTRAINT;
--

ALTER TABLE ONLY stake_registration
    ADD CONSTRAINT unique_stake_registration UNIQUE (tx_id, cert_index);


--
-- Name: treasury unique_treasury; Type: CONSTRAINT;
--

ALTER TABLE ONLY treasury
    ADD CONSTRAINT unique_treasury UNIQUE (addr_id, tx_id, cert_index);


--
-- Name: tx unique_tx; Type: CONSTRAINT;
--

ALTER TABLE ONLY tx
    ADD CONSTRAINT unique_tx UNIQUE (hash);



--
-- Name: tx_in unique_txin; Type: CONSTRAINT;
--

ALTER TABLE ONLY tx_in
    ADD CONSTRAINT unique_txin UNIQUE (tx_out_id, tx_out_index);


--
-- Name: tx_out unique_txout; Type: CONSTRAINT;
--

ALTER TABLE ONLY tx_out
    ADD CONSTRAINT unique_txout UNIQUE (tx_id, index);


--
-- Name: extra_key_witness unique_witness; Type: CONSTRAINT;
--

ALTER TABLE ONLY extra_key_witness
    ADD CONSTRAINT unique_witness UNIQUE (hash);