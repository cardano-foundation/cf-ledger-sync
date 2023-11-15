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

CREATE SEQUENCE IF NOT EXISTS tx_witnesses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE tx_witnesses_id_seq OWNED BY tx_witnesses.id;

ALTER TABLE ONLY tx_witnesses
ALTER COLUMN id SET DEFAULT nextval('tx_witnesses_id_seq'::regclass);

CREATE INDEX IF NOT EXISTS tx_witnesses_tx_id_idx ON tx_witnesses (tx_id);


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

CREATE SEQUENCE IF NOT EXISTS tx_bootstrap_witnesses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE tx_bootstrap_witnesses_id_seq OWNED BY tx_bootstrap_witnesses.id;


ALTER TABLE ONLY tx_bootstrap_witnesses
ALTER COLUMN id SET DEFAULT nextval('tx_bootstrap_witnesses_id_seq'::regclass);

CREATE INDEX IF NOT EXISTS tx_bootstrap_witnesses_tx_id_idx ON tx_bootstrap_witnesses (tx_id);