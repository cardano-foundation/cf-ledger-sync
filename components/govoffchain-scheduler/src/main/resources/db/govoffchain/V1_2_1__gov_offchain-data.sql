--
-- Name: off_chain_voting_data; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS off_chain_voting_data
(
    id                    bigint        PRIMARY KEY,
    voting_procedure_id   uuid          NOT NULL UNIQUE,
    content               jsonb,
    check_valid           integer       NOT NULL,
    valid_at_slot         bigint
);
CREATE SEQUENCE off_chain_voting_data_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE off_chain_voting_data_id_seq OWNED BY off_chain_voting_data.id;

ALTER TABLE ONLY off_chain_voting_data
ALTER COLUMN id SET DEFAULT nextval('off_chain_voting_data_id_seq'::regclass);

--
-- Name: off_chain_gov_action; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS off_chain_gov_action
(
    id                    bigint        PRIMARY KEY,
    gov_action_tx_hash    varchar(64)   NOT NULL,
    gov_action_idx        integer       NOT NULL,
    content               jsonb,
    check_valid           integer       NOT NULL,
    valid_at_slot         bigint
);
ALTER TABLE ONLY off_chain_gov_action
    ADD CONSTRAINT unique_off_chain_gov_action UNIQUE (gov_action_tx_hash, gov_action_idx);

CREATE SEQUENCE off_chain_gov_action_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE off_chain_gov_action_id_seq OWNED BY off_chain_gov_action.id;

ALTER TABLE ONLY off_chain_gov_action
ALTER COLUMN id SET DEFAULT nextval('off_chain_gov_action_id_seq'::regclass);

--
-- Name: off_chain_drep_registration; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS off_chain_drep_registration
(
    id                    bigint        PRIMARY KEY,
    drep_reg_tx_hash      varchar(64)   NOT NULL,
    drep_reg_cert_index   integer       NOT NULL,
    content               jsonb,
    check_valid           integer       NOT NULL,
    valid_at_slot         bigint
);
ALTER TABLE ONLY off_chain_drep_registration
    ADD CONSTRAINT unique_off_chain_drep_registration UNIQUE (drep_reg_tx_hash, drep_reg_cert_index);
CREATE SEQUENCE off_chain_drep_registration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE off_chain_drep_registration_id_seq OWNED BY off_chain_drep_registration.id;

ALTER TABLE ONLY off_chain_drep_registration
ALTER COLUMN id SET DEFAULT nextval('off_chain_drep_registration_id_seq'::regclass);

--
-- Name: off_chain_constitution; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS off_chain_constitution
(
    id                        bigint        PRIMARY KEY,
    constitution_active_epoch integer       NOT NULL UNIQUE,
    content                   jsonb,
    check_valid               integer       NOT NULL,
    valid_at_slot             bigint
);

CREATE SEQUENCE off_chain_constitution_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE off_chain_constitution_id_seq OWNED BY off_chain_constitution.id;

ALTER TABLE ONLY off_chain_constitution
ALTER COLUMN id SET DEFAULT nextval('off_chain_constitution_id_seq'::regclass);

--
-- Name: off_chain_committee_deregistration; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS off_chain_committee_deregistration
(
    id                            bigint        PRIMARY KEY,
    committee_dereg_tx_hash       varchar(64)   NOT NULL,
    committee_dereg_cert_index    integer       NOT NULL,
    content                       jsonb,
    check_valid                   integer       NOT NULL,
    valid_at_slot                 bigint
);
ALTER TABLE ONLY off_chain_committee_deregistration
    ADD CONSTRAINT unique_off_chain_committee_dereg UNIQUE (committee_dereg_tx_hash, committee_dereg_cert_index);
CREATE SEQUENCE off_chain_committee_deregistration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE off_chain_committee_deregistration_id_seq OWNED BY off_chain_committee_deregistration.id;

ALTER TABLE ONLY off_chain_committee_deregistration
ALTER COLUMN id SET DEFAULT nextval('off_chain_committee_deregistration_id_seq'::regclass);

--
-- Name: off_chain_vote_fetch_error; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS off_chain_fetch_error
(
    id                    bigint                        PRIMARY KEY,
    anchor_url            varchar(2000),
    anchor_hash           varchar(64),
    type                  integer                       NOT NULL,
    fetch_error           varchar(2000)                 NOT NULL,
    fetch_time            timestamp without time zone   NOT NULL,
    retry_count           integer default 0             NOT NULL
);
ALTER TABLE ONLY off_chain_fetch_error
    ADD CONSTRAINT unique_off_chain_fetch_error UNIQUE (anchor_url, anchor_hash, type);

CREATE SEQUENCE off_chain_fetch_error_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE off_chain_fetch_error_id_seq OWNED BY off_chain_fetch_error.id;

ALTER TABLE ONLY off_chain_fetch_error
ALTER COLUMN id SET DEFAULT nextval('off_chain_fetch_error_id_seq'::regclass);

--
-- Name: off_chain_data_checkpoint; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS off_chain_data_checkpoint
(
    id          bigint PRIMARY KEY,
    block_no    bigint,
    slot_no     bigint,
    type        varchar(64)   UNIQUE,
    update_time timestamp without time zone NOT NULL
);

CREATE SEQUENCE off_chain_data_checkpoint_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE off_chain_data_checkpoint_id_seq OWNED BY off_chain_data_checkpoint.id;

ALTER TABLE ONLY off_chain_data_checkpoint
ALTER COLUMN id SET DEFAULT nextval('off_chain_data_checkpoint_id_seq'::regclass);