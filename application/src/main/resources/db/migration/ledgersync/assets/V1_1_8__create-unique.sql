--
-- Name: multi_asset unique_multi_asset; Type: CONSTRAINT;
--

ALTER TABLE ONLY multi_asset
    ADD CONSTRAINT unique_multi_asset UNIQUE (policy, name);


--
-- Name: ma_tx_mint unique_ma_tx_mint; Type: CONSTRAINT;
--

ALTER TABLE ONLY ma_tx_mint
    ADD CONSTRAINT unique_ma_tx_mint UNIQUE (ident, tx_id);


