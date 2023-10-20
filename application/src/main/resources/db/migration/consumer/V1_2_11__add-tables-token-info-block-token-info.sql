CREATE TABLE IF NOT EXISTS token_info
(
    id                bigserial NOT NULL,
    ident             int8 NULL,
    number_of_holders int8 NULL,
    volume_24h        numeric(40) NULL,
    block_no          int8 NULL,
    update_time       timestamp NULL,
    CONSTRAINT token_info_pkey PRIMARY KEY (id),
    CONSTRAINT unique_token_info UNIQUE (ident)
);

CREATE SEQUENCE IF NOT EXISTS token_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE token_info_id_seq OWNED BY token_info.id;

CREATE TABLE IF NOT EXISTS token_info_checkpoint
(
    id          bigserial NOT NULL,
    block_no    int8 NULL,
    update_time timestamp NULL,
    CONSTRAINT block_token_info_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS token_info_checkpoint_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE token_info_checkpoint_id_seq OWNED BY token_info_checkpoint.id;

