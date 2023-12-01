ALTER TABLE address
    ADD COLUMN IF NOT EXISTS payment_cred varchar (56) NULL;

UPDATE address
SET payment_cred = (SELECT txo.payment_cred
                    FROM tx_out txo
                    WHERE txo.address = address.address LIMIT 1)
WHERE address.address_has_script IS TRUE;

CREATE INDEX IF NOT EXISTS address_payment_cred_idx ON address (payment_cred);
CREATE INDEX IF NOT EXISTS redeemer_script_hash_tx_id_idx ON redeemer (script_hash, tx_id);
CREATE INDEX IF NOT EXISTS stake_address_script_hash_idx ON stake_address (script_hash);