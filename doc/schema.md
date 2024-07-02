# Schema Documentation for ledger-sync

### `pool_hash`

A table for every unique pool key hash.

* Primary Id: `id`

| Column name | Type         | Description                           |
|-------------|--------------|---------------------------------------|
| `id`        | integer (64) |                                       |
| `hash_raw`  | string       | The raw string of the pool hash.      |
| `pool_size` | numeric      | The size of the pool.                 |
| `epoch_no`  | integer (32) | The raw bytes of the pool hash.       |
| `view`      | string       | The Bech32 encoding of the pool hash. |

### `slot_leader`

Every unique slot leader (ie an entity that mines a block). It could be a pool or a leader defined in genesis.

* Primary Id: `id`

| Column name    | Type         | Description                                                       |
|----------------|--------------|-------------------------------------------------------------------|
| `id`           | integer (64) |                                                                   |
| `hash`         | string       | The hash of of the block producer identifier.                     |
| `pool_hash_id` | integer (64) | If the slot leader is a pool, an index into the `PoolHash` table. |
| `description`  | string       | An auto-generated description of the slot leader.                 |

### `block`

A table for blocks on the chain.

* Primary Id: `id`

| Column name       | Type         | Description                                                                                                                                                                                          |
|-------------------|--------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`              | integer (64) |                                                                                                                                                                                                      |
| `hash`            | string       | The hash identifier of the block.                                                                                                                                                                    |
| `epoch_no`        | integer (32) | The epoch number.                                                                                                                                                                                    |
| `slot_no`         | integer (64) | The slot number.                                                                                                                                                                                     |
| `epoch_slot_no`   | integer (32) | The slot number within an epoch (resets to zero at the start of each epoch).                                                                                                                         |
| `block_no`        | integer (64) | The block number.                                                                                                                                                                                    |
| `previous_id`     | integer (64) | The Block table index of the previous block.                                                                                                                                                         |
| `slot_leader_id`  | integer (64) | The SlotLeader table index of the creator of this block.                                                                                                                                             |
| `size`            | integer (32) | The block size (in bytes). Note, this size value is not expected to be the same as the sum of the tx sizes due to the fact that txs being stored in segwit format and oddities in the CBOR encoding. |
| `time`            | timestamp    | The block time (UTCTime).                                                                                                                                                                            |
| `tx_count`        | integer (64) | The number of transactions in this block.                                                                                                                                                            |
| `proto_major`     | integer (32) | The block's major protocol number.                                                                                                                                                                   |
| `proto_minor`     | integer (32) | The block's major protocol number.                                                                                                                                                                   |
| `vrf_key`         | string       | The VRF key of the creator of this block.                                                                                                                                                            |
| `op_cert`         | string       | The hash of the operational certificate of the block producer.                                                                                                                                       |
| `op_cert_counter` | integer (64) | The value of the counter used to produce the operational certificate.                                                                                                                                |

### `schema_version`

The version of the database schema. Schema versioning is split into three stages as detailed below. This table should
only ever have a single row.

* Primary Id: `id`

| Column name   | Type         | Description                                                   |
|---------------|--------------|---------------------------------------------------------------|
| `id`          | integer (64) |                                                               |
| `stage_one`   | integer (64) | Set up PostgreSQL data types (using SQL 'DOMAIN' statements). |
| `stage_two`   | integer (64) | Persistent generated migrations.                              |
| `stage_three` | integer (64) | Set up database views, indices etc.                           |

### `tx`

A table for transactions within a block on the chain.

* Primary Id: `id`

| Column name         | Type         | Description                                                                               |
|---------------------|--------------|-------------------------------------------------------------------------------------------|
| `id`                | integer (64) |                                                                                           |
| `hash`              | integer (64) | The hash identifier of the transaction.                                                   |
| `block_id`          | integer (64) | The Block table index of the block that contains this transaction.                        |
| `block_index`       | integer (64) | The index of this transaction with the block (zero based).                                |
| `out_sum`           | numeric      | The sum of the transaction outputs .                                                      |
| `fee`               | numeric      | The fees paid for this transaction.                                                       |
| `deposit`           | integer (64) | Deposit (or deposit refund) in this transaction. Deposits are positive, refunds negative. |
| `size`              | integer (32) | The size of the transaction in bytes.                                                     |
| `invalid_before`    | integer (64) | Transaction in invalid before this slot number.                                           |
| `invalid_hereafter` | integer (64) | Transaction in invalid at or after this slot number.                                      |
| `valid_contract`    | boolean      | False if the contract is invalid. True if the contract is valid or there is no contract.  |
| `script_size`       | integer (32) | The sum of the script sizes (in bytes) of scripts in the transaction.                     |

### `stake_address`

A table of unique stake addresses. Can be an actual address or a script hash. The existance of an entry doesn't mean the
address is registered or in fact that is was ever registered.

* Primary Id: `id`

| Column name        | Type         | Description                                                               |
|--------------------|--------------|---------------------------------------------------------------------------|
| `id`               | integer (64) |                                                                           |
| `hash_raw`         | string       | The raw bytes of the stake address hash.                                  |
| `view`             | string       | The Bech32 encoded version of the stake address.                          |
| `script_hash`      | string       | The script hash, in case this address is locked by a script.              |
| `available_reward` | numeric      | The total amount of ADA rewards that have been accrued by a stake address |

### `tx_out`

A table for transaction outputs.

* Primary Id: `id`

| Column name           | Type         | Description                                                                                                                           |
|-----------------------|--------------|---------------------------------------------------------------------------------------------------------------------------------------|
| `id`                  | integer (64) |                                                                                                                                       |
| `address`             | string       | The human readable encoding of the output address. Will be Base58 for Byron era addresses and Bech32 for Shelley era.                 |
| `address_has_script`  | boolean      | Flag which shows if this address is locked by a script.                                                                               |
| `address_raw`         | bytea        | Flag which shows if this address is locked by a script.                                                                               |
| `data_hash`           | string       | The hash of the transaction output datum. (NULL for Txs without scripts).                                                             |
| `index`               | smallint     | The index of this transaction output with the transaction.                                                                            |
| `payment_cred`        | string       | The payment credential part of the Shelley address. (NULL for Byron addresses). For a script-locked address, this is the script hash. |
| `token_type`          | integer      | Type of token (NATIVE_TOKEN(0),TOKEN(1),ALL_TOKEN_TYPE(2)).                                                                           |
| `value`               | numeric      | The output value  of the transaction output.                                                                                          |
| `inline_datum_id`     | integer (64) | The inline datum of the output, if it has one.                                                                                        |
| `reference_script_id` | integer (64) | The reference script of the output, if it has one.                                                                                    |
| `stake_address_id`    | integer (64) | The StakeAddress table index for the stake address part of the Shelley address. (NULL for Byron addresses).                           |
| `tx_id`               | integer (64) | The Tx table index of the transaction that contains this transaction output.                                                          |

### `tx_in`

A table for transaction inputs.

* Primary Id: `id`

| Column name    | Type         | Description                                                                            |
|----------------|--------------|----------------------------------------------------------------------------------------|
| `id`           | integer (64) |                                                                                        |
| `tx_in_id`     | integer (64) | The Tx table index of the transaction that contains this transaction input.            |
| `tx_out_index` | smallint     | The index within the transaction outputs.                                              |
| `tx_out_id`    | integer (64) | The Tx table index of the transaction that contains the referenced transaction output. |
| `redeemer_id`  | integer (64) | The Redeemer table index which is used to validate this input.                         |

### `reference_tx_in`

A table for reference transaction inputs.

* Primary Id: `id`

| Column name    | Type         | Description                                                                |
|----------------|--------------|----------------------------------------------------------------------------|
| `id`           | integer (64) |                                                                            |
| `tx_in_id`     | integer (64) | The Tx table index of the transaction that contains this transaction input |
| `tx_out_id`    | integer (64) | The Tx table index of the transaction that contains the referenced output. |
| `tx_out_index` | smallint     | The index within the transaction outputs.                                  |

### `meta`

A table containing metadata about the chain. There will probably only ever be one row in this table.

* Primary Id: `id`

| Column name    | Type         | Description                    |
|----------------|--------------|--------------------------------|
| `id`           | integer (64) |                                |
| `network_name` | string       | The network name.              |
| `start_time`   | timestamp    | The start time of the network. |
| `version`      | string       |                                |

### `epoch`

Aggregation of data within an epoch.

* Primary Id: `id`

| Column name           | Type         | Description                                                                                       |
|-----------------------|--------------|---------------------------------------------------------------------------------------------------|
| `id`                  | integer (64) |                                                                                                   |
| `blk_count`           | integer (32) | The number of blocks in this epoch.                                                               |
| `end_time`            | timestamp    | The epoch end time.                                                                               |
| `fees`                | numeric      | The sum of the fees  in this epoch.                                                               |
| `max_slot`            | integer      | The maximum slots of this epoch                                                                   |
| `no`                  | integer      | The epoch number.                                                                                 |
| `out_sum`             | numeric      | The sum of the transaction output values  in this epoch.                                          |
| `start_time`          | timestamp    | The epoch start time.                                                                             |
| `tx_count`            | integer (32) | The number of transactions in this epoch.                                                         |
| `era`                 | integer      | Type of era (BYRON_EBB(0),BYRON(1),SHELLEY(2),ALLEGRA(3),MARY(4),ALONZO(5),BABBAGE(6),CONWAY(7)). |
| `rewards_distributed` | numeric      | The total amount of ADA that was distributed as staking rewards during a specific epoch.          |

### `ada_pots`

A table with all the different types of total balances (Shelley only).
The treasury and rewards fields will be correct for the whole epoch, but all other fields change block by block.

* Primary Id: `id`

| Column name | Type         | Description                                                               |
|-------------|--------------|---------------------------------------------------------------------------|
| `id`        | integer (64) |                                                                           |
| `deposits`  | numeric      | The amount  in the obligation pot coming from stake key and pool deposits |
| `epoch_no`  | integer (32) | The epoch number where this AdaPots snapshot was taken.                   |
| `fees`      | numeric      | The amount  in the fee pot.                                               |
| `reserves`  | numeric      | The amount  in the reserves pot.                                          |
| `rewards`   | numeric      | The amount  in the rewards pot.                                           |
| `slot_no`   | integer (64) | The slot number where this AdaPots snapshot was taken.                    |
| `treasury`  | numeric      | The amount  in the treasury pot.                                          |
| `utxo`      | numeric      | The amount  in the UTxO set.                                              |
| `block_id`  | integer (64) | The Block table index of the block for which this snapshot was taken.     |

### `pool_metadata_ref`

An on-chain reference to off-chain pool metadata.

* Primary Id: `id`

| Column name        | Type         | Description                                                                      |
|--------------------|--------------|----------------------------------------------------------------------------------|
| `id`               | integer (64) |                                                                                  |
| `hash`             | varchar      | The expected hash for the off-chain data.                                        |
| `url`              | varchar      | The URL for the location of the off-chain data.                                  |
| `pool_id`          | integer (64) | The PoolHash table index of the pool for this reference.                         |
| `registered_tx_id` | integer (64) | The Tx table index of the transaction in which provided this metadata reference. |

### `pool_update`

An on-chain pool update.

* Primary Id: `id`

| Column name        | Type         | Description                                                                |
|--------------------|--------------|----------------------------------------------------------------------------|
| `id`               | integer (64) |                                                                            |
| `active_epoch_no`  | integer (64) | The epoch number where this update becomes active.                         |
| `cert_index`       | integer (32) | The index of this pool update within the certificates of this transaction. |
| `fixed_cost`       | numeric      | The fixed per epoch fee (in ADA) this pool charges.                        |
| `margin`           | double       | The margin (as a percentage) this pool charges.                            |
| `pledge`           | numeric      | The amount the pool owner pledges to the pool.                             |
| `vrf_key_hash`     | varchar      | The hash of the pool's VRF key.                                            |
| `meta_id`          | integer (64) | The PoolMetadataRef table index this pool update refers to.                |
| `hash_id`          | integer (64) | The PoolHash table index of the pool this update refers to.                |
| `registered_tx_id` | integer (64) | The Tx table index of the transaction in which provided this pool update.  |
| `reward_addr_id`   | integer (64) | The StakeAddress table index of this pool's rewards address.               |

### `pool_owner`

A table containing pool owners.

* Primary Id: `id`

| Column name      | Type         | Description                                                      |
|------------------|--------------|------------------------------------------------------------------|
| `id`             | integer (64) |                                                                  |
| `pool_update_id` | integer (64) | The PoolUpdate table index for the pool.                         |
| `addr_id`        | integer (64) | The StakeAddress table index for the pool owner's stake address. |

### `pool_retire`

A table containing information about pools retiring.

* Primary Id: `id`

| Column name       | Type         | Description                                                                     |
|-------------------|--------------|---------------------------------------------------------------------------------|
| `id`              | integer (64) |                                                                                 |
| `cert_index`      | integer (32) | The index of this pool retirement within the certificates of this transaction.  |
| `retiring_epoch`  | integer (32) | The epoch where this pool retires.                                              |
| `announced_tx_id` | integer (64) | The Tx table index of the transaction where this pool retirement was announced. |
| `hash_id`         | integer (64) | The PoolHash table index of the pool this retirement refers to.                 |

### `pool_relay`

A table containing information about pools relay.

* Primary Id: `id`

| Column name    | Type         | Description                                                |
|----------------|--------------|------------------------------------------------------------|
| `id`           | integer (64) |                                                            |
| `dns_name`     | string       | The DNS name of the relay (NULLable).                      |
| `dns_srv_name` | string       | The DNS service name of the relay (NULLable).              |
| `ipv4`         | string       | The IPv4 address of the relay (NULLable).                  |
| `ipv6`         | string       | The IPv6 address of the relay (NULLable).                  |
| `port`         | integer (32) | The port number of relay (NULLable).                       |
| `update_id`    | integer (64) | The PoolUpdate table index this PoolRelay entry refers to. |

### `stake_registration`

A table containing stake address registrations.

* Primary Id: `id`

| Column name  | Type         | Description                                                                       |
|--------------|--------------|-----------------------------------------------------------------------------------|
| `id`         | integer (64) |                                                                                   |
| `cert_index` | integer (32) | The index of this stake registration within the certificates of this transaction. |
| `epoch_no`   | integer (32) | The epoch in which the registration took place.                                   |
| `addr_id`    | integer (64) | The StakeAddress table index for the stake address.                               |
| `tx_id`      | integer (64) | The Tx table index of the transaction where this stake address was registered.    |

### `stake_deregistration`

A table containing stake address deregistrations.

* Primary Id: `id`

| Column name   | Type         | Description                                                                         |
|---------------|--------------|-------------------------------------------------------------------------------------|
| `id`          | integer (64) |                                                                                     |
| `cert_index`  | integer (32) | The index of this stake deregistration within the certificates of this transaction. |
| `epoch_no`    | integer (32) | The epoch in which the deregistration took place.                                   |
| `addr_id`     | integer (64) | The StakeAddress table index for the stake address.                                 |
| `redeemer_id` | integer (64) | The Redeemer table index that is related with this certificate.                     |
| `tx_id`       | integer (64) | The Tx table index of the transaction where this stake address was deregistered.    |

### `delegation`

A table containing delegations from a stake address to a stake pool.

* Primary Id: `id`

| Column name       | Type         | Description                                                               |
|-------------------|--------------|---------------------------------------------------------------------------|
| `id`              | integer (64) |                                                                           |
| `active_epoch_no` | integer (64) | The epoch number where this delegation becomes active.                    |
| `cert_index`      | integer (32) | The index of this delegation within the certificates of this transaction. |
| `slot_no`         | integer (64) | The slot number of the block that contained this delegation.              |
| `addr_id`         | integer (64) | The StakeAddress table index for the stake address.                       |
| `pool_hash_id`    | integer (64) | The PoolHash table index for the pool being delegated to.                 |
| `redeemer_id`     | integer (64) | The Redeemer table index that is related with this certificate.           |
| `tx_id`           | integer (64) | The Tx table index of the transaction that contained this delegation.     |
