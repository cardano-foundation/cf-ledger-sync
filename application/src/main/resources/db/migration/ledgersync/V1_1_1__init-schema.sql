CREATE TABLE IF NOT EXISTS ada_pots
(
    id       bigint         NOT NULL,
    deposits numeric(20, 0) NOT NULL,
    epoch_no integer        NOT NULL,
    fees     numeric(20, 0) NOT NULL,
    reserves numeric(20, 0) NOT NULL,
    rewards  numeric(20, 0) NOT NULL,
    slot_no  bigint         NOT NULL,
    treasury numeric(20, 0) NOT NULL,
    utxo     numeric(20, 0) NOT NULL,
    block_id bigint         NOT NULL
    );

--
-- Name: block; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS block
(
    id              bigint                NOT NULL,
    block_no        bigint,
    epoch_no        integer,
    epoch_slot_no   integer,
    hash            character varying(64) NOT NULL,
    op_cert         character varying(64),
    op_cert_counter bigint,
    proto_major     integer,
    proto_minor     integer,
    size            integer,
    slot_leader_id  bigint,
    slot_no         bigint,
    "time"          timestamp without time zone,
    tx_count        bigint,
    vrf_key         character varying(65535),
    previous_id     bigint
    );

--
-- Name: cost_model; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS cost_model
(
    id    bigint                   NOT NULL,
    costs text                     NOT NULL,
    hash  character varying(64)    NOT NULL
    );

--
-- Name: datum; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS datum
(
    id    bigint                NOT NULL,
    bytes bytea,
    hash  character varying(64) NOT NULL,
    value text,
    tx_id bigint                NOT NULL
    );

--
-- Name: delegation; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS delegation
(
    id              bigint  NOT NULL,
    active_epoch_no bigint  NOT NULL,
    cert_index      integer NOT NULL,
    slot_no         bigint  NOT NULL,
    addr_id         bigint  NOT NULL,
    pool_hash_id    bigint  NOT NULL,
    redeemer_id     bigint,
    tx_id           bigint  NOT NULL
);
--
-- Name: delisted_pool; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS delisted_pool
(
    id       bigint                NOT NULL,
    hash_raw character varying(56) NOT NULL
    );

--
-- Name: epoch; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS epoch
(
    id                  bigint         NOT NULL,
    blk_count           integer        NOT NULL,
    end_time            timestamp without time zone,
    fees                numeric(20, 0) NOT NULL,
    max_slot            integer        NOT NULL,
    no                  integer        NOT NULL,
    out_sum             numeric(39, 0) NOT NULL,
    start_time          timestamp without time zone,
    tx_count            integer        NOT NULL,
    era                 integer,
    rewards_distributed numeric(20, 0)
    );


--
-- Name: epoch_param; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS epoch_param
(
    id                    bigint           NOT NULL,
    coins_per_utxo_size   numeric(20, 0),
    collateral_percent    integer,
    decentralisation      double precision NOT NULL,
    epoch_no              integer          NOT NULL,
    extra_entropy         character varying(64),
    influence             double precision NOT NULL,
    key_deposit           numeric(20, 0)   NOT NULL,
    max_bh_size           integer          NOT NULL,
    max_block_ex_mem      numeric(20, 0),
    max_block_ex_steps    numeric(20, 0),
    max_block_size        integer          NOT NULL,
    max_collateral_inputs integer,
    max_epoch             integer          NOT NULL,
    max_tx_ex_mem         numeric(20, 0),
    max_tx_ex_steps       numeric(20, 0),
    max_tx_size           integer          NOT NULL,
    max_val_size          numeric(20, 0),
    min_fee_a             integer          NOT NULL,
    min_fee_b             integer          NOT NULL,
    min_pool_cost         numeric(20, 0)   NOT NULL,
    min_utxo_value        numeric(20, 0),
    monetary_expand_rate  double precision NOT NULL,
    nonce                 character varying(64),
    optimal_pool_count    integer          NOT NULL,
    pool_deposit          numeric(20, 0)   NOT NULL,
    price_mem             double precision,
    price_step            double precision,
    protocol_major        integer          NOT NULL,
    protocol_minor        integer          NOT NULL,
    treasury_growth_rate  double precision NOT NULL,
    block_id              bigint           NOT NULL,
    cost_model_id         bigint
    );

--
-- Name: epoch_stake; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS epoch_stake
(
    id       bigint         NOT NULL,
    amount   numeric(20, 0) NOT NULL,
    epoch_no integer        NOT NULL,
    addr_id  bigint         NOT NULL,
    pool_id  bigint         NOT NULL
    );

--
-- Name: epoch_sync_time; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS epoch_sync_time
(
    id      bigint                 NOT NULL,
    no      bigint                 NOT NULL,
    seconds bigint                 NOT NULL,
    state   character varying(255) NOT NULL
    );


--
-- Name: extra_key_witness; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS extra_key_witness
(
    id    bigint                NOT NULL,
    hash  character varying(56) NOT NULL,
    tx_id bigint                NOT NULL
    );

--
-- Name: failed_tx_out; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS failed_tx_out
(
    id                  bigint                 NOT NULL,
    address             character varying(255) NOT NULL,
    address_has_script  boolean                NOT NULL,
    address_raw         bytea                  NOT NULL,
    data_hash           character varying(64),
    index               smallint               NOT NULL,
    multi_assets_descr  text                   NOT NULL,
    payment_cred        character varying(56),
    value               numeric(20, 0)         NOT NULL,
    inline_datum_id     bigint,
    reference_script_id bigint,
    stake_address_id    bigint,
    tx_id               bigint                 NOT NULL
    );

--
-- Name: ma_tx_mint; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS ma_tx_mint
(
    id       bigint         NOT NULL,
    quantity numeric(20, 0) NOT NULL,
    ident    bigint         NOT NULL,
    tx_id    bigint         NOT NULL
    );

--
-- Name: ma_tx_out; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS ma_tx_out
(
    id        bigint         NOT NULL,
    quantity  numeric(20, 0) NOT NULL,
    ident     bigint         NOT NULL,
    tx_out_id bigint         NOT NULL
    );

--
-- Name: meta; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS meta
(
    id           bigint                      NOT NULL,
    network_name character varying(255)      NOT NULL,
    start_time   timestamp without time zone NOT NULL,
    version      character varying(255)      NOT NULL
    );

--
-- Name: multi_asset; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS multi_asset
(
    id           bigint                 NOT NULL,
    fingerprint  character varying(255) NOT NULL,
    name         bytea                  NOT NULL,
    policy       character varying(56)  NOT NULL,
    supply       numeric(23, 0),
    tx_count     bigint,
    "time"       timestamp without time zone,
    total_volume numeric(40),
    name_view    varchar(64)
    );

--
-- Name: param_proposal; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS param_proposal
(
    id                    bigint                NOT NULL,
    coins_per_utxo_size   numeric(19, 2),
    collateral_percent    integer,
    decentralisation      double precision,
    entropy               character varying(64),
    epoch_no              integer               NOT NULL,
    influence             double precision,
    key                   character varying(56) NOT NULL,
    key_deposit           numeric(19, 2),
    max_bh_size           numeric(20, 0),
    max_block_ex_mem      numeric(20, 0),
    max_block_ex_steps    numeric(20, 0),
    max_block_size        numeric(20, 0),
    max_collateral_inputs integer,
    max_epoch             numeric(20, 0),
    max_tx_ex_mem         numeric(20, 0),
    max_tx_ex_steps       numeric(20, 0),
    max_tx_size           numeric(20, 0),
    max_val_size          numeric(20, 0),
    min_fee_a             numeric(20, 0),
    min_fee_b             numeric(20, 0),
    min_pool_cost         numeric(20, 0),
    min_utxo_value        numeric(20, 0),
    monetary_expand_rate  double precision,
    optimal_pool_count    numeric(20, 0),
    pool_deposit          numeric(20, 0),
    price_mem             double precision,
    price_step            double precision,
    protocol_major        integer,
    protocol_minor        integer,
    treasury_growth_rate  double precision,
    cost_model_id         bigint,
    registered_tx_id      bigint                NOT NULL
    );

--
-- Name: pool_hash; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS pool_hash
(
    id        bigint                 NOT NULL,
    hash_raw  character varying(56)  NOT NULL,
    pool_size numeric(20, 0)         NOT NULL,
    epoch_no  integer                NOT NULL,
    view      character varying(255) NOT NULL
    );

--
-- Name: pool_metadata_ref; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS pool_metadata_ref
(
    id               bigint                 NOT NULL,
    hash             character varying(64)  NOT NULL,
    url              character varying(255) NOT NULL,
    pool_id          bigint                 NOT NULL,
    registered_tx_id bigint                 NOT NULL
    );

--
-- Name: pool_offline_data; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS pool_offline_data
(
    id          bigint                   NOT NULL,
    bytes       bytea,
    hash        character varying(64)    NOT NULL,
    json        character varying(65535) NOT NULL,
    ticker_name character varying(255)   NOT NULL,
    pool_id     bigint                   NOT NULL,
    pmr_id      bigint                   NOT NULL,
    pool_name   varchar(255),
    logo_url    varchar(2000),
    icon_url    varchar(2000)
    );

--
-- Name: pool_offline_fetch_error; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS pool_offline_fetch_error
(
    id          bigint                      NOT NULL,
    fetch_error character varying(65535)    NOT NULL,
    fetch_time  timestamp without time zone NOT NULL,
    retry_count integer                     NOT NULL,
    pool_id     bigint                      NOT NULL,
    pmr_id      bigint                      NOT NULL
    );

--
-- Name: pool_owner; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS pool_owner
(
    id             bigint NOT NULL,
    pool_update_id bigint NOT NULL,
    addr_id        bigint NOT NULL
);

--
-- Name: pool_relay; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS pool_relay
(
    id           bigint NOT NULL,
    dns_name     character varying(255),
    dns_srv_name character varying(255),
    ipv4         character varying(255),
    ipv6         character varying(255),
    port         integer,
    update_id    bigint NOT NULL
    );


--
-- Name: pool_retire; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS pool_retire
(
    id              bigint  NOT NULL,
    cert_index      integer NOT NULL,
    retiring_epoch  integer NOT NULL,
    announced_tx_id bigint  NOT NULL,
    hash_id         bigint  NOT NULL
);

--
-- Name: pool_update; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS pool_update
(
    id               bigint                NOT NULL,
    active_epoch_no  bigint                NOT NULL,
    cert_index       integer               NOT NULL,
    fixed_cost       numeric(20, 0)        NOT NULL,
    margin           double precision      NOT NULL,
    pledge           numeric(20, 0)        NOT NULL,
    vrf_key_hash     character varying(64) NOT NULL,
    meta_id          bigint,
    hash_id          bigint                NOT NULL,
    registered_tx_id bigint                NOT NULL,
    reward_addr_id   bigint                NOT NULL
    );

--
-- Name: pot_transfer; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS pot_transfer
(
    id         bigint         NOT NULL,
    cert_index integer        NOT NULL,
    reserves   numeric(20, 0) NOT NULL,
    treasury   numeric(20, 0) NOT NULL,
    tx_id      bigint         NOT NULL
    );


--
-- Name: redeemer; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS redeemer
(
    id               bigint                 NOT NULL,
    fee              numeric(20, 0),
    index            integer                NOT NULL,
    purpose          character varying(255) NOT NULL,
    script_hash      character varying(56),
    unit_mem         bigint                 NOT NULL,
    unit_steps       bigint                 NOT NULL,
    redeemer_data_id bigint                 NOT NULL,
    tx_id            bigint                 NOT NULL
    );

--
-- Name: redeemer_data; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS redeemer_data
(
    id    bigint                NOT NULL,
    bytes bytea,
    hash  character varying(64) NOT NULL,
    value text,
    tx_id bigint                NOT NULL
    );

--
-- Name: reference_tx_in; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS reference_tx_in
(
    id           bigint   NOT NULL,
    tx_out_index smallint NOT NULL,
    tx_in_id     bigint   NOT NULL,
    tx_out_id    bigint   NOT NULL
);

--
-- Name: reserve; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS reserve
(
    id         bigint         NOT NULL,
    amount     numeric(20, 0) NOT NULL,
    cert_index integer        NOT NULL,
    addr_id    bigint         NOT NULL,
    tx_id      bigint         NOT NULL
    );

--
-- Name: reserved_pool_ticker; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS reserved_pool_ticker
(
    id        bigint                 NOT NULL,
    name      character varying(255) NOT NULL,
    pool_hash character varying(56)  NOT NULL
    );


--
-- Name: reward; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS reward
(
    id              bigint                 NOT NULL,
    amount          numeric(20, 0)         NOT NULL,
    earned_epoch    bigint                 NOT NULL,
    spendable_epoch bigint                 NOT NULL,
    type            character varying(255) NOT NULL,
    addr_id         bigint                 NOT NULL,
    pool_id         bigint
    );

--
-- Name: schema_version; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS schema_version
(
    id          bigint NOT NULL,
    stage_one   bigint NOT NULL,
    stage_three bigint NOT NULL,
    stage_two   bigint NOT NULL
);

--
-- Name: script; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS script
(
    id              bigint                 NOT NULL,
    bytes           bytea,
    hash            character varying(64)  NOT NULL,
    json            text,
    serialised_size integer,
    type            character varying(255) NOT NULL,
    tx_id           bigint                 NOT NULL,
    verified        bool                   NULL DEFAULT false
    );

--
-- Name: slot_leader; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS slot_leader
(
    id           bigint                   NOT NULL,
    description  character varying(65535) NOT NULL,
    hash         character varying(56)    NOT NULL,
    pool_hash_id bigint
    );

--
-- Name: stake_address; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS stake_address
(
    id               bigint                   NOT NULL,
    hash_raw         character varying(255)   NOT NULL,
    script_hash      character varying(56),
    view             character varying(65535) NOT NULL,
    balance          numeric(39),
    available_reward numeric(39)
    );

--
-- Name: stake_deregistration; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS stake_deregistration
(
    id          bigint  NOT NULL,
    cert_index  integer NOT NULL,
    epoch_no    integer NOT NULL,
    addr_id     bigint  NOT NULL,
    redeemer_id bigint,
    tx_id       bigint  NOT NULL
);

--
-- Name: stake_registration; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS stake_registration
(
    id         bigint  NOT NULL,
    cert_index integer NOT NULL,
    epoch_no   integer NOT NULL,
    addr_id    bigint  NOT NULL,
    tx_id      bigint  NOT NULL
);

--
-- Name: treasury; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS treasury
(
    id         bigint         NOT NULL,
    amount     numeric(20, 0) NOT NULL,
    cert_index integer        NOT NULL,
    addr_id    bigint         NOT NULL,
    tx_id      bigint         NOT NULL
    );

--
-- Name: tx; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS tx
(
    id                  bigint                NOT NULL,
    block_id            bigint,
    block_index         bigint,
    deposit             bigint,
    fee                 numeric(20, 0),
    hash                character varying(64) NOT NULL,
    invalid_before      numeric(20, 0),
    invalid_hereafter   numeric(20, 0),
    out_sum             numeric(20, 0),
    script_size         integer,
    size                integer,
    valid_contract      boolean,
    tx_metadata_hash_id bigint
    );

--
-- Name: tx_in; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS tx_in
(
    id           bigint   NOT NULL,
    tx_in_id     bigint,
    tx_out_index smallint NOT NULL,
    tx_out_id    bigint,
    redeemer_id  bigint
);


--
-- Name: tx_metadata; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS tx_metadata
(
    id    bigint         NOT NULL,
    bytes bytea,
    json  text,
    key   numeric(20, 0) NOT NULL,
    tx_id bigint         NOT NULL
    );


--
-- Name: tx_out; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS tx_out
(
    id                  bigint                   NOT NULL,
    address             character varying(65535) NOT NULL,
    address_has_script  boolean                  NOT NULL,
    address_raw         bytea                    NOT NULL,
    data_hash           character varying(64),
    index               smallint                 NOT NULL,
    payment_cred        character varying(56),
    token_type          integer                  NOT NULL,
    value               numeric(20, 0)           NOT NULL,
    inline_datum_id     bigint,
    reference_script_id bigint,
    stake_address_id    bigint,
    tx_id               bigint                   NOT NULL
    );

--
-- Name: withdrawal; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS withdrawal
(
    id          bigint         NOT NULL,
    amount      numeric(20, 0) NOT NULL,
    addr_id     bigint         NOT NULL,
    redeemer_id bigint,
    tx_id       bigint         NOT NULL
    );

--
-- Tag: extended edition; Type: TABLE
--

--
-- Name: address; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS address
(
    id                 bigint         NOT NULL,
    address            varchar(65535) NOT NULL,
    address_has_script boolean        NOT NULL,
    balance            numeric(39)    NOT NULL,
    tx_count           bigint,
    stake_address_id   bigint,
    verified_contract  boolean,
    payment_cred       varchar(56)
    );

--
-- Name: address_token; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS address_token
(
    id         bigint      NOT NULL,
    balance    numeric(39) NOT NULL,
    ident      bigint      NOT NULL,
    tx_id      bigint      NOT NULL,
    address_id bigint      NULL
    );

--
-- Name: address_tx_balance; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS address_tx_balance
(
    id               bigint      NOT NULL,
    balance          numeric(39) NOT NULL,
    time             timestamp,
    tx_id            bigint      NOT NULL,
    address_id       bigint      NULL,
    stake_address_id bigint
    );

CREATE TABLE IF NOT EXISTS address_token_balance
(
    id               bigint      NOT NULL,
    address_id       bigint      NULL,
    balance          numeric(39) NOT NULL,
    ident            bigint,
    stake_address_id bigint
    );

--
-- Name: rollback_history; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS rollback_history
(
    id            bigint       NOT NULL,
    block_hash    varchar(64)  NOT NULL,
    block_no      bigint       NOT NULL,
    rollback_time timestamp(6) NOT NULL,
    slot_no       bigint       NOT NULL
    );

CREATE TABLE IF NOT EXISTS unconsume_tx_in
(
    id           bigint   NOT NULL,
    tx_out_index smallint NOT NULL,
    tx_in_id     bigint   NOT NULL,
    tx_out_id    bigint   NOT NULL,
    redeemer_id  bigint
);

CREATE TABLE IF NOT EXISTS tx_chart
(
    id                          bigserial   NOT NULL
    PRIMARY KEY,
    day                         NUMERIC(13) NOT NULL,
    hour                        NUMERIC(13) NOT NULL,
    minute                      NUMERIC(13) NOT NULL,
    month                       NUMERIC(13) NOT NULL,
    tx_count                    bigint      NOT NULL,
    tx_simple                   bigint      NOT NULL,
    tx_with_metadata_without_sc bigint      NOT NULL,
    tx_with_sc                  bigint      NOT NULL,
    year                        NUMERIC(13) NOT NULL
);

CREATE TABLE IF NOT EXISTS tx_metadata_hash
(
    id    bigserial   NOT NULL
    PRIMARY KEY,
    hash  VARCHAR(64) NOT NULL
    );

-- tx_witnesses
CREATE TABLE IF NOT EXISTS tx_witnesses
(
    id          bigint       NOT NULL
    PRIMARY KEY,
    tx_id  bigint NOT NULL,
    key varchar,
    signature varchar,
    index_arr int[],
    index_arr_size int,
    type varchar(50)
    );

-- tx_bootstrap_witnesses
CREATE TABLE IF NOT EXISTS tx_bootstrap_witnesses
(
    id          bigint       NOT NULL
    PRIMARY KEY,
    tx_id  bigint NOT NULL,
    public_key varchar,
    signature varchar,
    chain_code varchar,
    attributes varchar
);

