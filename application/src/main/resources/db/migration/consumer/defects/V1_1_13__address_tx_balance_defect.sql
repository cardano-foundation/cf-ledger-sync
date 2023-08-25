UPDATE address_tx_balance atb
SET stake_address_id = (SELECT stake_address_id FROM address a WHERE a.id = atb.address_id);
