-- Drop table of block store
DROP TABLE IF EXISTS block;


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