# Schema Documentation for ledger-sync


### `pool_hash`

A table for every unique pool key hash.

* Primary Id: `id`

| Column name | Type   | Description                           |
|--------|--------|---------------------------------------|
| `id`   | integer (64) |                                       |
| `hash_raw` | string | The raw string of the pool hash.      |
| `pool_size` | numeric | The size of the pool.                 |
| `epoch_no` | integer (32) | The raw bytes of the pool hash.       |
| `view` | string | The Bech32 encoding of the pool hash. |

### `slot_leader`

Every unique slot leader (ie an entity that mines a block). It could be a pool or a leader defined in genesis.

* Primary Id: `id`

| Column name | Type   | Description |
|-|--------|-|
| `id` | integer (64) |  |
| `hash` | string | The hash of of the block producer identifier. |
| `pool_hash_id` | integer (64) | If the slot leader is a pool, an index into the `PoolHash` table. |
| `description` | string | An auto-generated description of the slot leader. |
