--
-- Name: tx_metadata unique_tx_metadata; Type: CONSTRAINT;
--

ALTER TABLE ONLY tx_metadata
    ADD CONSTRAINT unique_tx_metadata UNIQUE (key, tx_id);