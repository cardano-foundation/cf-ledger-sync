--
-- Name: block block_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY block
    ADD CONSTRAINT block_pkey PRIMARY KEY (id);

--
-- Name: block_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE block_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE block_id_seq OWNED BY block.id;

--
-- Name: block id; Type: DEFAULT;
--

ALTER TABLE ONLY block
ALTER COLUMN id SET DEFAULT nextval('block_id_seq'::regclass);