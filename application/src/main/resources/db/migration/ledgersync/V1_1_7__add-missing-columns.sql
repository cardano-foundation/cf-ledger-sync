ALTER TABLE epoch_param
    ADD COLUMN dvt_p_p_technical_group double precision,
    ADD COLUMN dvt_p_p_gov_group double precision,
    ADD COLUMN dvt_treasury_withdrawal double precision;

ALTER TABLE param_proposal
    ADD COLUMN dvt_p_p_technical_group double precision,
    ADD COLUMN dvt_p_p_gov_group double precision,
    ADD COLUMN dvt_treasury_withdrawal double precision;