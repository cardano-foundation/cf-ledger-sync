CREATE TABLE IF NOT EXISTS stake_tx_balance
(
    id          bigint       NOT NULL
    PRIMARY KEY,
    stake_address_id bigint NOT NULL,
    tx_id  bigint NOT NULL,
    balance_change bigint,
    time timestamp
);


--
-- Name: report_history_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE IF NOT EXISTS stake_tx_balance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE stake_tx_balance_id_seq OWNED BY stake_tx_balance.id;


ALTER TABLE ONLY stake_tx_balance
ALTER COLUMN id SET DEFAULT nextval('stake_tx_balance_id_seq'::regclass);

CREATE INDEX IF NOT EXISTS stake_tx_balance_tx_id_idx ON stake_tx_balance (tx_id);
CREATE INDEX IF NOT EXISTS stake_tx_balance_time_idx ON stake_tx_balance (time);
CREATE INDEX IF NOT EXISTS stake_tx_balance_stake_address_id_idx ON stake_tx_balance (stake_address_id);
