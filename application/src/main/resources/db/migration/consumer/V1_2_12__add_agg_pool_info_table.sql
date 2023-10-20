CREATE TABLE IF NOT EXISTS agg_pool_info
(
    id          bigint       NOT NULL
    PRIMARY KEY,
    pool_id bigint NOT NULL,
    block_in_epoch  bigint,
    block_life_time bigint,
    delegator_cnt bigint,
    block_check_point bigint,
    update_time timestamp
);


CREATE SEQUENCE IF NOT EXISTS agg_pool_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE agg_pool_info_id_seq OWNED BY agg_pool_info.id;


ALTER TABLE ONLY agg_pool_info
ALTER COLUMN id SET DEFAULT nextval('agg_pool_info_id_seq'::regclass);


CREATE INDEX IF NOT EXISTS EXagg_pool_info_pool_id_idx ON agg_pool_info (pool_id);
CREATE INDEX IF NOT EXISTS agg_pool_info_block_in_epoch_idx ON agg_pool_info (block_in_epoch);
CREATE INDEX IF NOT EXISTS agg_pool_info_block_life_time_idx ON agg_pool_info (block_life_time);
CREATE INDEX IF NOT EXISTS agg_pool_info_delegator_cnt_idx ON agg_pool_info (delegator_cnt);
