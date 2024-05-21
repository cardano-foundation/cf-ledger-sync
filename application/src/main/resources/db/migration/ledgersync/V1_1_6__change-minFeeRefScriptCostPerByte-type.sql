ALTER TABLE epoch_param
ALTER COLUMN min_fee_ref_script_cost_per_byte TYPE double precision USING min_fee_ref_script_cost_per_byte::double precision;

ALTER TABLE param_proposal
ALTER COLUMN min_fee_ref_script_cost_per_byte TYPE double precision USING min_fee_ref_script_cost_per_byte::double precision;
