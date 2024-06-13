--
-- Name: param_proposal unique_param_proposal; Type: CONSTRAINT;
--

ALTER TABLE ONLY param_proposal
    ADD CONSTRAINT unique_param_proposal UNIQUE (key, registered_tx_id);

--
-- Name: cost_model unique_cost_model; Type: CONSTRAINT;
--

ALTER TABLE ONLY cost_model
    ADD CONSTRAINT unique_cost_model UNIQUE (hash);


--
-- Name: epoch_param unique_epoch_param; Type: CONSTRAINT;
--

ALTER TABLE ONLY epoch_param
    ADD CONSTRAINT unique_epoch_param UNIQUE (epoch_no, block_id);