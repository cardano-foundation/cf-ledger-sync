CREATE INDEX IF NOT EXISTS redeemer_script_hash_idx ON redeemer (script_hash);
ALTER TABLE script ADD COLUMN IF NOT EXISTS verified bool DEFAULT FALSE;