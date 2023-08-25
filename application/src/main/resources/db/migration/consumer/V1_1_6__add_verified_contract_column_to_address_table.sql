ALTER TABLE address
    ADD COLUMN IF NOT EXISTS verified_contract bool NULL;
