--
-- Name: param_proposal_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE param_proposal_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE param_proposal_id_seq OWNED BY param_proposal.id;


--
-- Name: param_proposal id; Type: DEFAULT;
--

ALTER TABLE ONLY param_proposal
ALTER COLUMN id SET DEFAULT nextval('param_proposal_id_seq'::regclass);


--
-- Name: param_proposal param_proposal_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY param_proposal
    ADD CONSTRAINT param_proposal_pkey PRIMARY KEY (id);



--
-- Name: cost_model_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE cost_model_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE cost_model_id_seq OWNED BY cost_model.id;


--
-- Name: cost_model id; Type: DEFAULT;
--

ALTER TABLE ONLY cost_model
ALTER COLUMN id SET DEFAULT nextval('cost_model_id_seq'::regclass);


--
-- Name: cost_model cost_model_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY cost_model
    ADD CONSTRAINT cost_model_pkey PRIMARY KEY (id);


--
-- Name: epoch_param_id_seq; Type: SEQUENCE;
--

CREATE SEQUENCE epoch_param_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER SEQUENCE epoch_param_id_seq OWNED BY epoch_param.id;


--
-- Name: epoch_param id; Type: DEFAULT;
--

ALTER TABLE ONLY epoch_param
ALTER COLUMN id SET DEFAULT nextval('epoch_param_id_seq'::regclass);


--
-- Name: epoch_param epoch_param_pkey; Type: CONSTRAINT;
--

ALTER TABLE ONLY epoch_param
    ADD CONSTRAINT epoch_param_pkey PRIMARY KEY (id);
