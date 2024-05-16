--
-- Name: ada_pots_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE ada_pots_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE ada_pots_id_seq OWNED BY ada_pots.id;

--
-- Name: block_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE block_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE block_id_seq OWNED BY block.id;
--
-- Name: cost_model_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE cost_model_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE cost_model_id_seq OWNED BY cost_model.id;
--
-- Name: datum_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE datum_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE datum_id_seq OWNED BY datum.id;
--
-- Name: delegation_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE delegation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE delegation_id_seq OWNED BY delegation.id;

CREATE SEQUENCE delisted_pool_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE delisted_pool_id_seq OWNED BY delisted_pool.id;
--
-- Name: epoch_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE epoch_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE epoch_id_seq OWNED BY epoch.id;

--
-- Name: epoch_param_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE epoch_param_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE epoch_param_id_seq OWNED BY epoch_param.id;
--
-- Name: epoch_stake_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE epoch_stake_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE epoch_stake_id_seq OWNED BY epoch_stake.id;
--
-- Name: epoch_sync_time_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE epoch_sync_time_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE epoch_sync_time_id_seq OWNED BY epoch_sync_time.id;
--
-- Name: extra_key_witness_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE extra_key_witness_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE extra_key_witness_id_seq OWNED BY extra_key_witness.id;

--
-- Name: failed_tx_out_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE failed_tx_out_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE failed_tx_out_id_seq OWNED BY failed_tx_out.id;

--
-- Name: ma_tx_out_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE ma_tx_out_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE ma_tx_out_id_seq OWNED BY ma_tx_out.id;
--
-- Name: meta_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE meta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE meta_id_seq OWNED BY meta.id;

--
-- Name: param_proposal_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE param_proposal_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE param_proposal_id_seq OWNED BY param_proposal.id;
--
-- Name: pool_hash_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE pool_hash_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE pool_hash_id_seq OWNED BY pool_hash.id;
--
-- Name: pool_metadata_ref_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE pool_metadata_ref_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE pool_metadata_ref_id_seq OWNED BY pool_metadata_ref.id;

--
-- Name: pool_offline_data_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE pool_offline_data_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE pool_offline_data_id_seq OWNED BY pool_offline_data.id;
--
-- Name: pool_offline_fetch_error_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE pool_offline_fetch_error_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE pool_offline_fetch_error_id_seq OWNED BY pool_offline_fetch_error.id;
--
-- Name: pool_owner_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE pool_owner_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE pool_owner_id_seq OWNED BY pool_owner.id;
--
-- Name: pool_relay_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE pool_relay_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE pool_relay_id_seq OWNED BY pool_relay.id;
--
-- Name: pool_retire_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE pool_retire_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE pool_retire_id_seq OWNED BY pool_retire.id;

--
-- Name: pool_update_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE pool_update_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE pool_update_id_seq OWNED BY pool_update.id;
--
-- Name: pot_transfer_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE pot_transfer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE pot_transfer_id_seq OWNED BY pot_transfer.id;
--
-- Name: redeemer_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE redeemer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE redeemer_id_seq OWNED BY redeemer.id;
--
-- Name: redeemer_data_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE redeemer_data_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE redeemer_data_id_seq OWNED BY redeemer_data.id;
--
-- Name: reference_tx_in_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE reference_tx_in_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE reference_tx_in_id_seq OWNED BY reference_tx_in.id;
--
-- Name: reserve_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE reserve_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE reserve_id_seq OWNED BY reserve.id;
--
-- Name: reserved_pool_ticker_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE reserved_pool_ticker_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE reserved_pool_ticker_id_seq OWNED BY reserved_pool_ticker.id;
--
-- Name: reward_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE reward_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE reward_id_seq OWNED BY reward.id;

--
-- Name: schema_version_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE schema_version_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE schema_version_id_seq OWNED BY schema_version.id;

--
-- Name: script_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE script_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE script_id_seq OWNED BY script.id;

--
-- Name: slot_leader_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE slot_leader_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE slot_leader_id_seq OWNED BY slot_leader.id;
--
-- Name: stake_address_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE stake_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE stake_address_id_seq OWNED BY stake_address.id;
--
-- Name: stake_deregistration_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE stake_deregistration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE stake_deregistration_id_seq OWNED BY stake_deregistration.id;
--
-- Name: stake_registration_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE stake_registration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE stake_registration_id_seq OWNED BY stake_registration.id;
--
-- Name: treasury_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE treasury_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE treasury_id_seq OWNED BY treasury.id;
--
-- Name: tx_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE tx_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE tx_id_seq OWNED BY tx.id;
--
-- Name: tx_in_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE tx_in_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE tx_in_id_seq OWNED BY tx_in.id;

--
-- Name: tx_out_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE tx_out_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE tx_out_id_seq OWNED BY tx_out.id;
--
-- Name: unconsume_tx_in; Type: SEQUENCE;
--

CREATE SEQUENCE unconsume_tx_in_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE unconsume_tx_in_id_seq OWNED BY unconsume_tx_in.id;
--
-- Name: withdrawal_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE withdrawal_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE withdrawal_id_seq OWNED BY withdrawal.id;


--
-- Name: rollback_history_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE rollback_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE rollback_history_id_seq OWNED BY rollback_history.id;

--
-- Name: ada_pots id; Type: DEFAULT;
--

ALTER TABLE ONLY ada_pots
ALTER COLUMN id SET DEFAULT nextval('ada_pots_id_seq'::regclass);


--
-- Name: block id; Type: DEFAULT;
--

ALTER TABLE ONLY block
ALTER COLUMN id SET DEFAULT nextval('block_id_seq'::regclass);


--
-- Name: cost_model id; Type: DEFAULT;
--

ALTER TABLE ONLY cost_model
ALTER COLUMN id SET DEFAULT nextval('cost_model_id_seq'::regclass);


--
-- Name: datum id; Type: DEFAULT;
--

ALTER TABLE ONLY datum
ALTER COLUMN id SET DEFAULT nextval('datum_id_seq'::regclass);


--
-- Name: delegation id; Type: DEFAULT;
--

ALTER TABLE ONLY delegation
ALTER COLUMN id SET DEFAULT nextval('delegation_id_seq'::regclass);


--
-- Name: delisted_pool id; Type: DEFAULT;
--

ALTER TABLE ONLY delisted_pool
ALTER COLUMN id SET DEFAULT nextval('delisted_pool_id_seq'::regclass);


--
-- Name: epoch id; Type: DEFAULT;
--

ALTER TABLE ONLY epoch
ALTER COLUMN id SET DEFAULT nextval('epoch_id_seq'::regclass);


--
-- Name: epoch_param id; Type: DEFAULT;
--

ALTER TABLE ONLY epoch_param
ALTER COLUMN id SET DEFAULT nextval('epoch_param_id_seq'::regclass);


--
-- Name: epoch_stake id; Type: DEFAULT;
--

ALTER TABLE ONLY epoch_stake
ALTER COLUMN id SET DEFAULT nextval('epoch_stake_id_seq'::regclass);


--
-- Name: epoch_sync_time id; Type: DEFAULT;
--

ALTER TABLE ONLY epoch_sync_time
ALTER COLUMN id SET DEFAULT nextval('epoch_sync_time_id_seq'::regclass);


--
-- Name: extra_key_witness id; Type: DEFAULT;
--

ALTER TABLE ONLY extra_key_witness
ALTER COLUMN id SET DEFAULT nextval('extra_key_witness_id_seq'::regclass);


--
-- Name: failed_tx_out id; Type: DEFAULT;
--

ALTER TABLE ONLY failed_tx_out
ALTER COLUMN id SET DEFAULT nextval('failed_tx_out_id_seq'::regclass);



--
-- Name: ma_tx_out id; Type: DEFAULT;
--

ALTER TABLE ONLY ma_tx_out
ALTER COLUMN id SET DEFAULT nextval('ma_tx_out_id_seq'::regclass);


--
-- Name: meta id; Type: DEFAULT;
--

ALTER TABLE ONLY meta
ALTER COLUMN id SET DEFAULT nextval('meta_id_seq'::regclass);



--
-- Name: param_proposal id; Type: DEFAULT;
--

ALTER TABLE ONLY param_proposal
ALTER COLUMN id SET DEFAULT nextval('param_proposal_id_seq'::regclass);


--
-- Name: pool_hash id; Type: DEFAULT;
--

ALTER TABLE ONLY pool_hash
ALTER COLUMN id SET DEFAULT nextval('pool_hash_id_seq'::regclass);


--
-- Name: pool_metadata_ref id; Type: DEFAULT;
--

ALTER TABLE ONLY pool_metadata_ref
ALTER COLUMN id SET DEFAULT nextval('pool_metadata_ref_id_seq'::regclass);


--
-- Name: pool_offline_data id; Type: DEFAULT;
--

ALTER TABLE ONLY pool_offline_data
ALTER COLUMN id SET DEFAULT nextval('pool_offline_data_id_seq'::regclass);


--
-- Name: pool_offline_fetch_error id; Type: DEFAULT;
--

ALTER TABLE ONLY pool_offline_fetch_error
ALTER COLUMN id SET DEFAULT nextval('pool_offline_fetch_error_id_seq'::regclass);


--
-- Name: pool_owner id; Type: DEFAULT;
--

ALTER TABLE ONLY pool_owner
ALTER COLUMN id SET DEFAULT nextval('pool_owner_id_seq'::regclass);


--
-- Name: pool_relay id; Type: DEFAULT;
--

ALTER TABLE ONLY pool_relay
ALTER COLUMN id SET DEFAULT nextval('pool_relay_id_seq'::regclass);


--
-- Name: pool_retire id; Type: DEFAULT;
--

ALTER TABLE ONLY pool_retire
ALTER COLUMN id SET DEFAULT nextval('pool_retire_id_seq'::regclass);

--
-- Name: pool_update id; Type: DEFAULT;
--

ALTER TABLE ONLY pool_update
ALTER COLUMN id SET DEFAULT nextval('pool_update_id_seq'::regclass);


--
-- Name: pot_transfer id; Type: DEFAULT;
--

ALTER TABLE ONLY pot_transfer
ALTER COLUMN id SET DEFAULT nextval('pot_transfer_id_seq'::regclass);


--
-- Name: redeemer id; Type: DEFAULT;
--

ALTER TABLE ONLY redeemer
ALTER COLUMN id SET DEFAULT nextval('redeemer_id_seq'::regclass);


--
-- Name: redeemer_data id; Type: DEFAULT;
--

ALTER TABLE ONLY redeemer_data
ALTER COLUMN id SET DEFAULT nextval('redeemer_data_id_seq'::regclass);


--
-- Name: reference_tx_in id; Type: DEFAULT;
--

ALTER TABLE ONLY reference_tx_in
ALTER COLUMN id SET DEFAULT nextval('reference_tx_in_id_seq'::regclass);


--
-- Name: reserve id; Type: DEFAULT;
--

ALTER TABLE ONLY reserve
ALTER COLUMN id SET DEFAULT nextval('reserve_id_seq'::regclass);


--
-- Name: reserved_pool_ticker id; Type: DEFAULT;
--

ALTER TABLE ONLY reserved_pool_ticker
ALTER COLUMN id SET DEFAULT nextval('reserved_pool_ticker_id_seq'::regclass);


--
-- Name: reward id; Type: DEFAULT;
--

ALTER TABLE ONLY reward
ALTER COLUMN id SET DEFAULT nextval('reward_id_seq'::regclass);

--
-- Name: script id; Type: DEFAULT;
--

ALTER TABLE ONLY script
ALTER COLUMN id SET DEFAULT nextval('script_id_seq'::regclass);


--
-- Name: slot_leader id; Type: DEFAULT;
--

ALTER TABLE ONLY slot_leader
ALTER COLUMN id SET DEFAULT nextval('slot_leader_id_seq'::regclass);


--
-- Name: stake_address id; Type: DEFAULT;
--

ALTER TABLE ONLY stake_address
ALTER COLUMN id SET DEFAULT nextval('stake_address_id_seq'::regclass);


--
-- Name: stake_deregistration id; Type: DEFAULT;
--

ALTER TABLE ONLY stake_deregistration
ALTER COLUMN id SET DEFAULT nextval('stake_deregistration_id_seq'::regclass);


--
-- Name: stake_registration id; Type: DEFAULT;
--

ALTER TABLE ONLY stake_registration
ALTER COLUMN id SET DEFAULT nextval('stake_registration_id_seq'::regclass);


--
-- Name: treasury id; Type: DEFAULT;
--

ALTER TABLE ONLY treasury
ALTER COLUMN id SET DEFAULT nextval('treasury_id_seq'::regclass);


--
-- Name: tx id; Type: DEFAULT;
--

ALTER TABLE ONLY tx
ALTER COLUMN id SET DEFAULT nextval('tx_id_seq'::regclass);


--
-- Name: tx_in id; Type: DEFAULT;
--

ALTER TABLE ONLY tx_in
ALTER COLUMN id SET DEFAULT nextval('tx_in_id_seq'::regclass);


--
-- Name: tx_out id; Type: DEFAULT;
--

ALTER TABLE ONLY tx_out
ALTER COLUMN id SET DEFAULT nextval('tx_out_id_seq'::regclass);


--
-- Name: unconsume_tx_in id; Type: DEFAULT;
--

ALTER TABLE ONLY unconsume_tx_in
ALTER COLUMN id SET DEFAULT nextval('unconsume_tx_in_id_seq'::regclass);


--
-- Name: withdrawal id; Type: DEFAULT;
--

ALTER TABLE ONLY withdrawal
ALTER COLUMN id SET DEFAULT nextval('withdrawal_id_seq'::regclass);

--
-- Name: rollback_history id; Type: DEFAULT;
--

ALTER TABLE ONLY rollback_history
ALTER COLUMN id SET DEFAULT nextval('rollback_history_id_seq'::regclass);

--
-- Name: ada_pots ada_pots_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY ada_pots
    ADD CONSTRAINT ada_pots_pkey PRIMARY KEY (id);


--
-- Name: block block_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY block
    ADD CONSTRAINT block_pkey PRIMARY KEY (id);


--
-- Name: cost_model cost_model_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY cost_model
    ADD CONSTRAINT cost_model_pkey PRIMARY KEY (id);


--
-- Name: datum datum_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY datum
    ADD CONSTRAINT datum_pkey PRIMARY KEY (id);


--
-- Name: delegation delegation_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY delegation
    ADD CONSTRAINT delegation_pkey PRIMARY KEY (id);


--
-- Name: delisted_pool delisted_pool_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY delisted_pool
    ADD CONSTRAINT delisted_pool_pkey PRIMARY KEY (id);


--
-- Name: epoch_param epoch_param_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY epoch_param
    ADD CONSTRAINT epoch_param_pkey PRIMARY KEY (id);


--
-- Name: epoch epoch_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY epoch
    ADD CONSTRAINT epoch_pkey PRIMARY KEY (id);


--
-- Name: epoch_stake epoch_stake_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY epoch_stake
    ADD CONSTRAINT epoch_stake_pkey PRIMARY KEY (id);


--
-- Name: epoch_sync_time epoch_sync_time_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY epoch_sync_time
    ADD CONSTRAINT epoch_sync_time_pkey PRIMARY KEY (id);


--
-- Name: extra_key_witness extra_key_witness_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY extra_key_witness
    ADD CONSTRAINT extra_key_witness_pkey PRIMARY KEY (id);


--
-- Name: failed_tx_out failed_tx_out_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY failed_tx_out
    ADD CONSTRAINT failed_tx_out_pkey PRIMARY KEY (id);



--
-- Name: ma_tx_out ma_tx_out_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY ma_tx_out
    ADD CONSTRAINT ma_tx_out_pkey PRIMARY KEY (id);


--
-- Name: meta meta_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY meta
    ADD CONSTRAINT meta_pkey PRIMARY KEY (id);



--
-- Name: param_proposal param_proposal_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY param_proposal
    ADD CONSTRAINT param_proposal_pkey PRIMARY KEY (id);


--
-- Name: pool_hash pool_hash_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY pool_hash
    ADD CONSTRAINT pool_hash_pkey PRIMARY KEY (id);


--
-- Name: pool_metadata_ref pool_metadata_ref_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY pool_metadata_ref
    ADD CONSTRAINT pool_metadata_ref_pkey PRIMARY KEY (id);


--
-- Name: pool_offline_data pool_offline_data_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY pool_offline_data
    ADD CONSTRAINT pool_offline_data_pkey PRIMARY KEY (id);


--
-- Name: pool_offline_fetch_error pool_offline_fetch_error_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY pool_offline_fetch_error
    ADD CONSTRAINT pool_offline_fetch_error_pkey PRIMARY KEY (id);


--
-- Name: pool_owner pool_owner_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY pool_owner
    ADD CONSTRAINT pool_owner_pkey PRIMARY KEY (id);


--
-- Name: pool_relay pool_relay_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY pool_relay
    ADD CONSTRAINT pool_relay_pkey PRIMARY KEY (id);


--
-- Name: pool_retire pool_retire_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY pool_retire
    ADD CONSTRAINT pool_retire_pkey PRIMARY KEY (id);

--
-- Name: pool_update pool_update_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY pool_update
    ADD CONSTRAINT pool_update_pkey PRIMARY KEY (id);


--
-- Name: pot_transfer pot_transfer_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY pot_transfer
    ADD CONSTRAINT pot_transfer_pkey PRIMARY KEY (id);


--
-- Name: redeemer_data redeemer_data_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY redeemer_data
    ADD CONSTRAINT redeemer_data_pkey PRIMARY KEY (id);


--
-- Name: redeemer redeemer_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY redeemer
    ADD CONSTRAINT redeemer_pkey PRIMARY KEY (id);


--
-- Name: reference_tx_in reference_tx_in_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY reference_tx_in
    ADD CONSTRAINT reference_tx_in_pkey PRIMARY KEY (id);


--
-- Name: reserve reserve_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY reserve
    ADD CONSTRAINT reserve_pkey PRIMARY KEY (id);


--
-- Name: reserved_pool_ticker reserved_pool_ticker_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY reserved_pool_ticker
    ADD CONSTRAINT reserved_pool_ticker_pkey PRIMARY KEY (id);


--
-- Name: reward reward_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY reward
    ADD CONSTRAINT reward_pkey PRIMARY KEY (id);


--
-- Name: schema_version schema_version_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY schema_version
    ADD CONSTRAINT schema_version_pkey PRIMARY KEY (id);


--
-- Name: script script_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY script
    ADD CONSTRAINT script_pkey PRIMARY KEY (id);


--
-- Name: slot_leader slot_leader_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY slot_leader
    ADD CONSTRAINT slot_leader_pkey PRIMARY KEY (id);


--
-- Name: stake_address stake_address_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY stake_address
    ADD CONSTRAINT stake_address_pkey PRIMARY KEY (id);


--
-- Name: stake_deregistration stake_deregistration_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY stake_deregistration
    ADD CONSTRAINT stake_deregistration_pkey PRIMARY KEY (id);


--
-- Name: stake_registration stake_registration_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY stake_registration
    ADD CONSTRAINT stake_registration_pkey PRIMARY KEY (id);


--
-- Name: treasury treasury_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY treasury
    ADD CONSTRAINT treasury_pkey PRIMARY KEY (id);


--
-- Name: tx_in tx_in_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY tx_in
    ADD CONSTRAINT tx_in_pkey PRIMARY KEY (id);

--
-- Name: tx_out tx_out_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY tx_out
    ADD CONSTRAINT tx_out_pkey PRIMARY KEY (id);

--
-- Name: tx tx_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY tx
    ADD CONSTRAINT tx_pkey PRIMARY KEY (id);

--
-- Name: withdrawal withdrawal_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY withdrawal
    ADD CONSTRAINT withdrawal_pkey PRIMARY KEY (id);

--
-- Name: unconsume_tx_in unconsume_tx_in_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY unconsume_tx_in
    ADD CONSTRAINT unconsume_tx_in_pkey PRIMARY KEY (id);

--
-- Name: rollback_history rollback_history_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY rollback_history
    ADD CONSTRAINT rollback_history_pkey PRIMARY KEY (id);


CREATE SEQUENCE IF NOT EXISTS tx_witnesses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE tx_witnesses_id_seq OWNED BY tx_witnesses.id;

ALTER TABLE ONLY tx_witnesses
ALTER COLUMN id SET DEFAULT nextval('tx_witnesses_id_seq'::regclass);


CREATE SEQUENCE IF NOT EXISTS tx_bootstrap_witnesses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE tx_bootstrap_witnesses_id_seq OWNED BY tx_bootstrap_witnesses.id;


ALTER TABLE ONLY tx_bootstrap_witnesses
ALTER COLUMN id SET DEFAULT nextval('tx_bootstrap_witnesses_id_seq'::regclass);
