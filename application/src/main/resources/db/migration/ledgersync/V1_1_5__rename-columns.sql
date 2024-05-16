ALTER TABLE epoch_param
    RENAME COLUMN pvt_p_p_technical_group TO dvt_p_p_technical_group;
ALTER TABLE epoch_param
    RENAME COLUMN pvt_p_p_gov_group TO dvt_p_p_gov_group;
ALTER TABLE epoch_param
    RENAME COLUMN pvt_treasury_withdrawal TO dvt_treasury_withdrawal;

ALTER TABLE param_proposal
    RENAME COLUMN pvt_p_p_technical_group TO dvt_p_p_technical_group;
ALTER TABLE param_proposal
    RENAME COLUMN pvt_p_p_gov_group TO dvt_p_p_gov_group;
ALTER TABLE param_proposal
    RENAME COLUMN pvt_treasury_withdrawal TO dvt_treasury_withdrawal;
