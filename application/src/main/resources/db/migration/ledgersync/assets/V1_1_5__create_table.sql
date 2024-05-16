-- Drop table of assets store
DROP TABLE IF EXISTS assets;


--
-- Name: multi_asset; Type: TABLE;
--

CREATE TABLE IF NOT EXISTS multi_asset
(
    id          bigint                 NOT NULL,
    fingerprint character varying(255) NOT NULL,
    name        bytea                  NOT NULL,
    policy      character varying(56)  NOT NULL,
    unit        character varying(255),
    supply      numeric(23, 0),
    "time"      timestamp without time zone,
    name_view   varchar(64)
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