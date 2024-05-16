--
-- Name: tx_metadata_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE tx_metadata_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE tx_metadata_id_seq OWNED BY tx_metadata.id;


--
-- Name: tx_metadata id; Type: DEFAULT;
--

ALTER TABLE ONLY tx_metadata
ALTER COLUMN id SET DEFAULT nextval('tx_metadata_id_seq'::regclass);

--
-- Name: tx_metadata tx_metadata_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY tx_metadata
    ADD CONSTRAINT tx_metadata_pkey PRIMARY KEY (id);

