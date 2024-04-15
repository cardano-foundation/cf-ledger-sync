docker compose -f $1 exec ledger-sync-postgres psql -U cardano-master -d ledger_sync
