--
-- Name: block unique_block; Type: CONSTRAINT;
--

ALTER TABLE ONLY block
    ADD CONSTRAINT unique_block UNIQUE (hash);