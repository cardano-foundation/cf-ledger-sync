CREATE TABLE IF NOT EXISTS address_tx_balance
(
    id               bigint                 NOT NULL PRIMARY KEY,
    slot             bigint,
    balance          numeric(39)            NOT NULL,
    time             timestamp,
    tx_hash          character varying(64)  NOT NULL,
    address_id       bigint      NULL,
    stake_address    character varying(65535),
    block_hash       varchar(64),
    block            bigint,
    block_time       bigint,
    epoch            integer,
    update_datetime  timestamp
    );

CREATE TABLE IF NOT EXISTS address_token_balance
(
    id               bigint      NOT NULL PRIMARY KEY,
    slot             bigint,
    address_id       bigint      NULL,
    balance          numeric(39) NOT NULL,
    fingerprint      character varying(255),
    asset_name       varchar(64),
    policy           character varying(56),
    stake_address    character varying(65535),
    block            bigint,
    update_datetime  timestamp
    );

CREATE TABLE IF NOT EXISTS address_token
(
    id         bigint      NOT NULL PRIMARY KEY,
    slot       bigint,
    balance    numeric(39) NOT NULL,
    fingerprint  character varying(255),
    asset_name varchar(64),
    policy     character varying(56),
    tx_hash    character varying(64)      NOT NULL,
    address_id bigint      NULL,
    block            bigint,
    update_datetime  timestamp
    );

CREATE TABLE IF NOT EXISTS address
(
    id                 bigint         NOT NULL PRIMARY KEY,
    slot               bigint,
    address            varchar(65535) NOT NULL,
    address_has_script boolean        NOT NULL,
    balance            numeric(39)    NOT NULL,
    tx_count           bigint,
    stake_address      character varying(65535),
    verified_contract  boolean,
    payment_cred       varchar(56),
    block            bigint,
    update_datetime  timestamp
    );

CREATE SEQUENCE address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE address_id_seq OWNED BY address.id;

CREATE SEQUENCE address_token_balance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE address_token_balance_id_seq OWNED BY address_token_balance.id;

CREATE SEQUENCE address_token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE address_token_id_seq OWNED BY address_token.id;

CREATE SEQUENCE address_tx_balance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE address_tx_balance_id_seq OWNED BY address_tx_balance.id;

CREATE INDEX IF NOT EXISTS idx_address_address ON address USING hash (address);

CREATE INDEX IF NOT EXISTS idx_address_token_tx_hash ON address_token USING btree (tx_hash);
CREATE INDEX IF NOT EXISTS idx_address_token_asset_policy ON address_token (asset_name, policy);
CREATE INDEX IF NOT EXISTS idx_address_token_address_id ON address_token (address_id);

CREATE INDEX IF NOT EXISTS idx_address_tx_balance_address_id ON address_tx_balance (address_id);
CREATE INDEX IF NOT EXISTS idx_address_token_balance_address_id ON address_token_balance (address_id);
CREATE INDEX IF NOT EXISTS idx_address_token_balance_asset_policy ON address_token_balance (asset_name, policy);
CREATE INDEX IF NOT EXISTS idx_address_token_balance_address_id_asset_policy ON address_token_balance (address_id, asset_name, policy);
CREATE INDEX IF NOT EXISTS idx_address_token_balance_stake_address ON address_token_balance (stake_address);

CREATE INDEX IF NOT EXISTS idx_address_tx_balance_time ON address_tx_balance USING btree ("time");
CREATE INDEX IF NOT EXISTS idx_address_tx_balance_tx_hash ON address_tx_balance USING btree (tx_hash);
CREATE INDEX IF NOT EXISTS idx_address_tx_balance_stake_address ON address_tx_balance (stake_address);

