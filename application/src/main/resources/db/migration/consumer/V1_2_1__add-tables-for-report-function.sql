CREATE TABLE IF NOT EXISTS report_history
(
    id          BIGINT       NOT NULL
    PRIMARY KEY,
    report_name varchar(255) NOT NULL,
    created_at  timestamp,
    username    varchar(255) NOT NULL,
    status      varchar(255),
    type        varchar(255),
    storage_key varchar(255)
    constraint storage_key_unique
    unique
    );

CREATE TABLE IF NOT EXISTS stake_key_report_history
(
    id                   bigint       NOT NULL
    primary key,
    stake_key            varchar(255) NOT NULL,
    from_date            timestamp    NOT NULL,
    to_date              timestamp    NOT NULL,
    is_ada_transfer      boolean DEFAULT FALSE,
    is_fees_paid         boolean DEFAULT FALSE,
    report_id            bigint       NOT NULL,
    event_registration   boolean DEFAULT FALSE,
    event_delegation     boolean DEFAULT FALSE,
    event_rewards        boolean DEFAULT FALSE,
    event_withdrawal     boolean DEFAULT FALSE,
    event_deregistration boolean DEFAULT FALSE
    );

CREATE TABLE IF NOT EXISTS pool_report_history
(
    id                   bigint       NOT NULL
    PRIMARY KEY,
    pool_id              varchar(255) NOT NULL,
    is_pool_size         boolean      NOT NULL,
    is_fees_paid         boolean      NOT NULL,
    begin_epoch          integer      NOT NULL,
    end_epoch            integer      NOT NULL,
    report_id            bigint       NOT NULL,
    event_registration   boolean      NOT NULL,
    event_deregistration boolean      NOT NULL,
    event_reward         boolean      NOT NULL,
    event_pool_update    boolean      NOT NULL
    );

--
-- Name: report_history_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE IF NOT EXISTS report_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE report_history_id_seq OWNED BY report_history.id;

--
-- Name: stake_key_report_history_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE IF NOT EXISTS stake_key_report_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE stake_key_report_history_id_seq OWNED BY stake_key_report_history.id;

--
-- Name: pool_report_history_id_seq; Type: SEQUENCE;
--
CREATE SEQUENCE IF NOT EXISTS pool_report_history_id_seq
    start with 1
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

alter sequence pool_report_history_id_seq owned by pool_report_history.id;

--
-- Name: report_history id; Type: DEFAULT;
--

ALTER TABLE ONLY report_history
ALTER COLUMN id SET DEFAULT nextval('report_history_id_seq'::regclass);

--
-- Name: rollback_history id; Type: DEFAULT;
--

ALTER TABLE ONLY stake_key_report_history
ALTER COLUMN id SET DEFAULT nextval('stake_key_report_history_id_seq'::regclass);

--
-- Name: rollback_history id; Type: DEFAULT;
--

ALTER TABLE ONLY pool_report_history
ALTER COLUMN id SET DEFAULT nextval('pool_report_history_id_seq'::regclass);

CREATE INDEX IF NOT EXISTS report_history_username_idx ON report_history (username);

CREATE INDEX IF NOT EXISTS report_history_status_idx ON report_history (status);

CREATE INDEX IF NOT EXISTS stake_key_report_history_stake_key_idx ON stake_key_report_history (stake_key);

CREATE INDEX IF NOT EXISTS stake_key_report_history_report_id_idx ON stake_key_report_history (report_id);

CREATE INDEX IF NOT EXISTS stake_key_report_history_stake_key_report_id_idx ON stake_key_report_history (stake_key, report_id);

CREATE INDEX IF NOT EXISTS pool_report_history_pool_id_idx ON pool_report_history (pool_id);

CREATE INDEX IF NOT EXISTS pool_report_history_report_id_idx on pool_report_history (report_id);

CREATE INDEX IF NOT EXISTS pool_report_history_pool_id_report_id_idx on pool_report_history (pool_id, report_id);

ALTER TABLE report_history ADD COLUMN IF NOT EXISTS uploaded_at timestamp;