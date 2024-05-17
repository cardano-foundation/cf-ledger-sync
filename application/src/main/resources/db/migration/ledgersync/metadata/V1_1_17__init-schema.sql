-- Drop table of metadata store
DROP TABLE IF EXISTS transaction_metadata;


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
