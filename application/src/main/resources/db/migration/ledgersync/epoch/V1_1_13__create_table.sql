-- Drop table of epoch store
DROP TABLE IF EXISTS local_protocol_params;
DROP TABLE IF EXISTS protocol_params_proposal;
DROP TABLE IF EXISTS cost_model;
DROP TABLE IF EXISTS epoch_param;


--
-- Name: param_proposal; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS param_proposal
(
    id                               bigint                NOT NULL,
    coins_per_utxo_size              numeric(19, 2),
    collateral_percent               integer,
    decentralisation                 double precision,
    entropy                          character varying(64),
    epoch_no                         integer               NOT NULL,
    influence                        double precision,
    key                              character varying(56) NOT NULL,
    key_deposit                      numeric(19, 2),
    max_bh_size                      numeric(20, 0),
    max_block_ex_mem                 numeric(20, 0),
    max_block_ex_steps               numeric(20, 0),
    max_block_size                   numeric(20, 0),
    max_collateral_inputs            integer,
    max_epoch                        numeric(20, 0),
    max_tx_ex_mem                    numeric(20, 0),
    max_tx_ex_steps                  numeric(20, 0),
    max_tx_size                      numeric(20, 0),
    max_val_size                     numeric(20, 0),
    min_fee_a                        numeric(20, 0),
    min_fee_b                        numeric(20, 0),
    min_pool_cost                    numeric(20, 0),
    min_utxo_value                   numeric(20, 0),
    monetary_expand_rate             double precision,
    optimal_pool_count               numeric(20, 0),
    pool_deposit                     numeric(20, 0),
    price_mem                        double precision,
    price_step                       double precision,
    protocol_major                   integer,
    protocol_minor                   integer,
    treasury_growth_rate             double precision,
    cost_model_id                    bigint,
    registered_tx_id                 bigint                NOT NULL,
    pvt_motion_no_confidence         double precision,
    pvt_commit_normal                double precision,
    pvt_committee_no_confidence      double precision,
    pvt_hard_fork_initiation         double precision,
    pvt_p_p_security_group           double precision,
    dvt_p_p_technical_group          double precision,
    dvt_p_p_gov_group                double precision,
    dvt_treasury_withdrawal          double precision,
    dvt_motion_no_confidence         double precision,
    dvt_commitee_normal              double precision,
    dvt_committee_no_confidence      double precision,
    dvt_update_to_constitution       double precision,
    dvt_hard_fork_initiation         double precision,
    dvt_p_p_network_group            double precision,
    dvt_p_p_economic_group           double precision,
    committee_min_size               numeric(20, 0),
    committee_max_term_length        numeric(20, 0),
    gov_action_lifetime              numeric(20, 0),
    gov_action_deposit               numeric(20, 0),
    drep_deposit                     numeric(20, 0),
    drep_activity                    numeric(20, 0),
    min_fee_ref_script_cost_per_byte double precision
);


--
-- Name: cost_model; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS cost_model
(
    id    bigint                NOT NULL,
    costs text                  NOT NULL,
    hash  character varying(64) NOT NULL
);


--
-- Name: epoch_param; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS epoch_param
(
    id                               bigint           NOT NULL,
    coins_per_utxo_size              numeric(20, 0),
    collateral_percent               integer,
    decentralisation                 double precision NOT NULL,
    epoch_no                         integer          NOT NULL,
    extra_entropy                    character varying(64),
    influence                        double precision NOT NULL,
    key_deposit                      numeric(20, 0)   NOT NULL,
    max_bh_size                      integer          NOT NULL,
    max_block_ex_mem                 numeric(20, 0),
    max_block_ex_steps               numeric(20, 0),
    max_block_size                   integer          NOT NULL,
    max_collateral_inputs            integer,
    max_epoch                        integer          NOT NULL,
    max_tx_ex_mem                    numeric(20, 0),
    max_tx_ex_steps                  numeric(20, 0),
    max_tx_size                      integer          NOT NULL,
    max_val_size                     numeric(20, 0),
    min_fee_a                        integer          NOT NULL,
    min_fee_b                        integer          NOT NULL,
    min_pool_cost                    numeric(20, 0)   NOT NULL,
    min_utxo_value                   numeric(20, 0),
    monetary_expand_rate             double precision NOT NULL,
    nonce                            character varying(64),
    optimal_pool_count               integer          NOT NULL,
    pool_deposit                     numeric(20, 0)   NOT NULL,
    price_mem                        double precision,
    price_step                       double precision,
    protocol_major                   integer          NOT NULL,
    protocol_minor                   integer          NOT NULL,
    treasury_growth_rate             double precision NOT NULL,
    block_id                         bigint           NOT NULL,
    cost_model_id                    bigint,
    pvt_motion_no_confidence         double precision,
    pvt_commit_normal                double precision,
    pvt_committee_no_confidence      double precision,
    pvt_hard_fork_initiation         double precision,
    pvt_p_p_security_group           double precision,
    dvt_p_p_technical_group          double precision,
    dvt_p_p_gov_group                double precision,
    dvt_treasury_withdrawal          double precision,
    dvt_motion_no_confidence         double precision,
    dvt_commitee_normal              double precision,
    dvt_committee_no_confidence      double precision,
    dvt_update_to_constitution       double precision,
    dvt_hard_fork_initiation         double precision,
    dvt_p_p_network_group            double precision,
    dvt_p_p_economic_group           double precision,
    committee_min_size               numeric(20, 0),
    committee_max_term_length        numeric(20, 0),
    gov_action_lifetime              numeric(20, 0),
    gov_action_deposit               numeric(20, 0),
    drep_deposit                     numeric(20, 0),
    drep_activity                    numeric(20, 0),
    min_fee_ref_script_cost_per_byte double precision
);

