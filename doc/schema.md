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

### `block`

A table for blocks on the chain.

* Primary Id: `id`

| Column name | Type   | Description |
|-|--------|-|
| `id` | integer (64) |  |
| `hash` | string | The hash identifier of the block. |
| `epoch_no` | integer (32) | The epoch number. |
| `slot_no` | integer (64) | The slot number. |
| `epoch_slot_no` | integer (32) | The slot number within an epoch (resets to zero at the start of each epoch). |
| `block_no` | integer (64) | The block number. |
| `previous_id` | integer (64) | The Block table index of the previous block. |
| `slot_leader_id` | integer (64) | The SlotLeader table index of the creator of this block. |
| `size` | integer (32) | The block size (in bytes). Note, this size value is not expected to be the same as the sum of the tx sizes due to the fact that txs being stored in segwit format and oddities in the CBOR encoding. |
| `time` | timestamp | The block time (UTCTime). |
| `tx_count` | integer (64) | The number of transactions in this block. |
| `proto_major` | integer (32) | The block's major protocol number. |
| `proto_minor` | integer (32) | The block's major protocol number. |
| `vrf_key` | string | The VRF key of the creator of this block. |
| `op_cert` | string | The hash of the operational certificate of the block producer. |
| `op_cert_counter` | integer (64) | The value of the counter used to produce the operational certificate. |