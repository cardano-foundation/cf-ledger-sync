--
-- Name: multi_asset_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE multi_asset_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE multi_asset_id_seq OWNED BY multi_asset.id;


--
-- Name: multi_asset id; Type: DEFAULT;
--

ALTER TABLE ONLY multi_asset
    ALTER COLUMN id SET DEFAULT nextval('multi_asset_id_seq'::regclass);


--
-- Name: multi_asset multi_asset_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY multi_asset
    ADD CONSTRAINT multi_asset_pkey PRIMARY KEY (id);


--
-- Name: ma_tx_mint ma_tx_mint_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY ma_tx_mint
    ADD CONSTRAINT ma_tx_mint_pkey PRIMARY KEY (id);

--
-- Name: ma_tx_mint_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE ma_tx_mint_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE ma_tx_mint_id_seq OWNED BY ma_tx_mint.id;


--
-- Name: ma_tx_mint id; Type: DEFAULT;
--

ALTER TABLE ONLY ma_tx_mint
ALTER COLUMN id SET DEFAULT nextval('ma_tx_mint_id_seq'::regclass);


