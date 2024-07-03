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

### `tx_metadata`

A table for metadata attached to a transaction.

* Primary Id: `id`

| Column name | Type         | Description                                                             |
|-------------|--------------|-------------------------------------------------------------------------|
| `id`        | integer (64) |                                                                         |
| `bytes`     | bytea        | The raw bytes of the payload.                                           |
| `json`      | text         | The JSON payload if it can be decoded as JSON.                          |
| `key`       | numeric      | The metadata key.                                                       |
| `tx_id`     | integer (64) | The Tx table index of the transaction where this metadata was included. |

### `tx_metadata_hash`

A table for hash metadata attached to a transaction.

* Primary Id: `id`

| Column name | Type         | Description                          |
|-------------|--------------|--------------------------------------|
| `id`        | integer (64) |                                      |
| `hash`      | string       | The hash of metadata of transaction. |

### `reward`

A table for earned staking rewards. After 13.2 release it includes only 3 types of rewards: member, leader and refund,
since the other 2 types have moved to a separate table instant_reward. The rewards are inserted incrementally and this
procedure is finalised when the spendable epoch comes. Before the epoch comes, some entries may be missing.
The `reward.id` field has been removed and it only appears on docs due to a bug.

* Primary Id: `id`

| Column name       | Type         | Description                                                                                                                                        |
|-------------------|--------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`              | integer (64) |                                                                                                                                                    |
| `amount`          | numeric      | The reward amount.                                                                                                                                 |
| `earned_epoch`    | integer (64) | The epoch in which the reward was earned. For `pool` and `leader` rewards spendable in epoch `N`, this will be `N - 2`, `refund` N.                |
| `spendable_epoch` | integer (64) | The epoch in which the reward is actually distributed and can be spent.                                                                            |
| `type`            | string       | The type of the rewards ("leader", "member","reserves","treasury","refund")                                                                        |
| `addr_id`         | integer (64) | The StakeAddress table index for the stake address that earned the reward.                                                                         |
| `pool_id`         | integer (64) | The PoolHash table index for the pool the stake address was delegated to when the reward is earned or for the pool that there is a deposit refund. |

### `withdrawal`

A table for withdrawals from a reward account.

* Primary Id: `id`

| Column name   | Type         | Description                                                                         |
|---------------|--------------|-------------------------------------------------------------------------------------|
| `id`          | integer (64) |                                                                                     |
| `amount`      | numeric      | The withdrawal amount.                                                              |
| `addr_id`     | integer (64) | The StakeAddress table index for the stake address for which the withdrawal is for. |
| `redeemer_id` | integer (64) | The Redeemer table index that is related with this withdrawal.                      |
| `tx_id`       | integer (64) | The Tx table index for the transaction that contains this withdrawal.               |

### `epoch_stake`

A table containing the epoch stake distribution for each epoch. This is inserted incrementally in the first blocks of
the epoch. The stake distribution is extracted from the `set` snapshot of the ledger.

* Primary Id: `id`

| Column name | Type         | Description                                                                   |
|-------------|--------------|-------------------------------------------------------------------------------|
| `id`        | integer (64) |                                                                               |
| `amount`    | numeric      | The amount being staked.                                                      |
| `epoch_no`  | integer (32) | The epoch number.                                                             |
| `addr_id`   | integer (64) | The StakeAddress table index for the stake address for this EpochStake entry. |
| `pool_id`   | integer (64) | The PoolHash table index for the pool this entry is delegated to.             |

### `treasury`

A table for payments from the treasury to a StakeAddress. Note: Before protocol version 5.0 (Alonzo) if more than one
payment was made to a stake address in a single epoch, only the last payment was kept and earlier ones removed. For
protocol version 5.0 and later, they are summed and produce a single reward with type `treasury`.

* Primary Id: `id`

| Column name  | Type         | Description                                                                        |
|--------------|--------------|------------------------------------------------------------------------------------|
| `id`         | integer (64) |                                                                                    |
| `amount`     | numeric      | The payment amount.                                                                |
| `cert_index` | integer (32) | The index of this payment certificate within the certificates of this transaction. |
| `addr_id`    | integer (64) | The StakeAddress table index for the stake address for this Treasury entry.        |
| `tx_id`      | integer (64) | The Tx table index for the transaction that contains this payment.                 |

### `reserve`

A table for payments from the reserves to a StakeAddress. Note: Before protocol version 5.0 (Alonzo) if more than one
payment was made to a stake address in a single epoch, only the last payment was kept and earlier ones removed. For
protocol version 5.0 and later, they are summed and produce a single reward with type `reserves`

* Primary Id: `id`

| Column name  | Type         | Description                                                                        |
|--------------|--------------|------------------------------------------------------------------------------------|
| `id`         | integer (64) |                                                                                    |
| `amount`     | numeric      | The payment amount.                                                                |
| `cert_index` | integer (32) | The index of this payment certificate within the certificates of this transaction. |
| `addr_id`    | integer (64) | The StakeAddress table index for the stake address for this Treasury entry.        |
| `tx_id`      | integer (64) | The Tx table index for the transaction that contains this payment.                 |

### `pot_transfer`

A table containing transfers between the reserves pot and the treasury pot.

* Primary Id: `id`

| Column name  | Type         | Description                                                                         |
|--------------|--------------|-------------------------------------------------------------------------------------|
| `id`         | integer (64) |                                                                                     |
| `cert_index` | integer (32) | The index of this transfer certificate within the certificates of this transaction. |
| `reserves`   | numeric      | The amount the reserves balance changes by.                                         |
| `treasury`   | numeric      | The amount the treasury balance changes by.                                         |
| `tx_id`      | integer (64) | The Tx table index for the transaction that contains this transfer.                 |

### `epoch_sync_time`

A table containing the time required to fully sync an epoch.

* Primary Id: `id`

| Column name | Type         | Description                                                                                                                                |
|-------------|--------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| `id`        | integer (64) |                                                                                                                                            |
| `no`        | integer (64) | The epoch number for this sync time.                                                                                                       |
| `seconds`   | integer (64) | The time (in seconds) required to sync this epoch (may be NULL for an epoch that was already partially synced when `db-sync` was started). |
| `state`     | string       | The sync state when the sync time is recorded (either 'lagging' or 'following').                                                           |

### `multi_asset`

A table containing all information the unique policy/name pairs along with a CIP14 asset fingerprint

* Primary Id: `id`

| Column name   | Type         | Description                               |
|---------------|--------------|-------------------------------------------|
| `id`          | integer (64) |                                           |
| `fingerprint` | string       | The CIP14 fingerprint for the MultiAsset. |
| `name`        | bytea        | The MultiAsset name.                      |
| `policy`      | string       | The MultiAsset policy hash.               |
| `unit`        | string       | The MultiAsset unit.                      |
| `supply`      | numeric      | The MultiAsset supply.                    |
| `time`        | timestamp    | The MultiAsset time.                      |
| `name_view`   | string       | The MultiAsset name view.                 |

### `ma_tx_mint`

A table containing Multi-Asset mint events.

* Primary Id: `id`

| Column name | Type         | Description                                                               |
|-------------|--------------|---------------------------------------------------------------------------|
| `id`        | integer (64) |                                                                           |
| `quantity`  | numeric      | The amount of the Multi Asset to mint (can be negative to "burn" assets). |
| `ident`     | integer (64) | The MultiAsset table index specifying the asset.                          |
| `tx_id`     | integer (64) | The Tx table index for the transaction that contains this minting event.  |

### `ma_tx_out`

A table containing Multi-Asset transaction outputs.

* Primary Id: `id`

| Column name | Type         | Description                                                                         |
|-------------|--------------|-------------------------------------------------------------------------------------|
| `id`        | integer (64) |                                                                                     |
| `quantity`  | numeric      | The Multi Asset transaction output amount (denominated in the Multi Asset).         |
| `ident`     | integer (64) | The MultiAsset table index specifying the asset.                                    |
| `tx_out_id` | integer (64) | The TxOut table index for the transaction that this Multi Asset transaction output. |

### `redeemer`

A table containing redeemers. A redeemer is provided for all items that are validated by a script.

* Primary Id: `id`

| Column name        | Type         | Description                                                                                                                          |
|--------------------|--------------|--------------------------------------------------------------------------------------------------------------------------------------|
| `id`               | integer (64) |                                                                                                                                      |
| `fee`              | numeric      | The budget in fees to run a script. The fees depend on the ExUnits and the current prices. Is null when --disable-ledger is enabled. |
| `index`            | word31type   | The index of the redeemer pointer in the transaction.                                                                                |
| `purpose`          | string       | What kind pf validation this redeemer is used for. It can be one of 'spend', 'mint', 'cert', 'reward'.                               |
| `script_hash`      | string       | The script hash this redeemer is used for.                                                                                           |
| `unit_mem`         | integer (64) | The budget in Memory to run a script.                                                                                                |
| `unit_steps`       | integer (64) | The budget in Cpu steps to run a script.                                                                                             |
| `redeemer_data_id` | integer (64) | The data related to this redeemer.                                                                                                   |
| `tx_id`            | integer (64) | The Tx table index that contains this redeemer.                                                                                      |

### `script`

A table containing scripts available, found in witnesses, inlined in outputs (reference outputs) or auxdata of
transactions.

* Primary Id: `id`

| Column name       | Type         | Description                                                                      |
|-------------------|--------------|----------------------------------------------------------------------------------|
| `id`              | integer (64) |                                                                                  |
| `bytes`           | bytea        | CBOR encoded plutus script data, null for other script types                     |
| `hash`            | string       | The Hash of the Script.                                                          |
| `json`            | text         | JSON representation of the timelock script, null for other script types          |
| `serialised_size` | integer (32) | The size of the CBOR serialised script, if it is a Plutus script.                |
| `type`            | string       | The type of the script. This is currenttly either 'timelock' or 'plutus'.        |
| `tx_id`           | integer (64) | The Tx table index for the transaction where this script first became available. |
| `verified`        | bool         | Check the script has been verified.                                              |

### `datum`

A table containing Plutus Datum, found in witnesses or inlined in outputs

* Primary Id: `id`

| Column name | Type         | Description                                                                      |
|-------------|--------------|----------------------------------------------------------------------------------|
| `id`        | integer (64) |                                                                                  |
| `bytes`     | bytea        | The actual data in CBOR format                                                   |
| `hash`      | string       | The Hash of the Datum                                                            |
| `value`     | text         | The actual data in JSON format (detailed schema)                                 |
| `tx_id`     | integer (64) | The Tx table index for the transaction where this script first became available. |

### `redeemer_data`

A table containing Plutus Redeemer Data. These are always referenced by at least one redeemer. New in v13: split from
datum table.

* Primary Id: `id`

| Column name | Type         | Description                                                                      |
|-------------|--------------|----------------------------------------------------------------------------------|
| `id`        | integer (64) |                                                                                  |
| `bytes`     | bytea        | The actual data in CBOR format                                                   |
| `hash`      | string       | The Hash of the Plutus Data                                                      |
| `value`     | text         | The actual data in JSON format (detailed schema)                                 |
| `tx_id`     | integer (64) | The Tx table index for the transaction where this script first became available. |

### `extra_key_witness`

A table containing transaction extra key witness hashes.

* Primary Id: `id`

| Column name | Type         | Description                               |
|-------------|--------------|-------------------------------------------|
| `id`        | integer (64) |                                           |
| `hash`      | string       | The hash of the witness.                  |
| `tx_id`     | integer (64) | The id of the tx this witness belongs to. |

### `param_proposal`

A table containing block chain parameter change proposals.

* Primary Id: `id`

| Column name                        | Type         | Description                                                                                          |
|------------------------------------|--------------|------------------------------------------------------------------------------------------------------|
| `id`                               | integer (64) |                                                                                                      |
| `coins_per_utxo_size`              | numeric      | For Alonzo this is the cost per UTxO word. For Babbage and later per UTxO byte.                      |
| `collateral_percent`               | integer (32) | The percentage of the txfee which must be provided as collateral when including non-native scripts.  |
| `decentralisation`                 | double       | The decentralisation parameter (1 fully centralised, 0 fully decentralised).                         |
| `entropy`                          | string       | The 32 byte string of extra random-ness to be added into the protocol's entropy pool.                |
| `epoch_no`                         | integer (64) | The epoch for which this parameter proposal in intended to become active. Always null in Conway era. |
| `influence`                        | double       | The influence of the pledge on a stake pool's probability on minting a block.                        |
| `key`                              | string       | The hash of the crypto key used to sign this proposal. Always null in Conway era.                    |
| `key_deposit`                      | numeric      | The amount (in Lovelace) require for a deposit to register a StakeAddress.                           |
| `max_bh_size`                      | numeric      | The maximum block header size (in bytes).                                                            |
| `max_block_ex_mem`                 | numeric      | The maximum number of execution memory allowed to be used in a single block.                         |
| `max_block_ex_steps`               | numeric      | The maximum number of execution steps allowed to be used in a single block.                          |
| `max_block_size`                   | numeric      | The maximum block size (in bytes).                                                                   |
| `max_collateral_inputs`            | integer (32) | The maximum number of collateral inputs allowed in a transaction.                                    |
| `max_epoch`                        | numeric      | The maximum number of epochs in the future that a pool retirement is allowed to be scheduled for.    |
| `max_tx_ex_mem`                    | numeric      | The maximum number of execution memory allowed to be used in a single transaction.                   |
| `max_tx_ex_steps`                  | numeric      | The maximum number of execution steps allowed to be used in a single transaction.                    |
| `max_tx_size`                      | numeric      | The maximum transaction size (in bytes).                                                             |
| `max_val_size`                     | numeric      | The maximum Val size.                                                                                |
| `min_fee_a`                        | numeric      | The 'a' parameter to calculate the minimum transaction fee.                                          |
| `min_fee_b`                        | numeric      | The 'b' parameter to calculate the minimum transaction fee.                                          |
| `min_pool_cost`                    | numeric      | The minimum pool cost.                                                                               |
| `min_utxo_value`                   | numeric      | The minimum value of a UTxO entry.                                                                   |
| `monetary_expand_rate`             | double       | The monetary expansion rate.                                                                         |
| `optimal_pool_count`               | numeric      | The optimal number of stake pools.                                                                   |
| `pool_deposit`                     | numeric      | The amount (in Lovelace) require for a deposit to register a stake pool.                             |
| `price_mem`                        | double       | The per word cost of script memory usage.                                                            |
| `price_step`                       | double       | The cost of script execution step usage.                                                             |
| `protocol_major`                   | integer (32) | The protocol major number.                                                                           |
| `protocol_minor`                   | integer (32) | The protocol minor number.                                                                           |
| `treasury_growth_rate`             | double       | The treasury growth rate.                                                                            |
| `cost_model_id`                    | integer (64) | The CostModel table index for the proposal.                                                          |
| `registered_tx_id`                 | integer (64) | The Tx table index for the transaction that contains this parameter proposal.                        |
| `pvt_motion_no_confidence`         | double       | Pool Voting threshold for motion of no-confidence.                                                   |
| `pvt_committee_normal`             | double       | Pool Voting threshold for new committee/threshold (normal state).                                    |
| `pvt_committee_no_confidence`      | double       | Pool Voting threshold for new committee/threshold (state of no-confidence).                          |
| `pvt_hard_fork_initiation`         | double       | Pool Voting threshold for hard-fork initiation.                                                      |
| `pvt_p_p_security_group`           | double       | Pool Vote threshold for protocol parameter changes, security group.                                  |
| `pvt_p_p_technical_group`          | double       | Pool Vote threshold for protocol parameter changes, technical group.                                 |
| `pvt_p_p_gov_group`                | double       | Pool Vote threshold for protocol parameter changes, governance group.                                |
| `pvt_treasury_withdrawal`          | double       | Pool Vote threshold for treasury withdrawal.                                                         |
| `dvt_motion_no_confidence`         | double       | DRep Vote threshold for motion of no-confidence.                                                     |
| `dvt_committee_normal`             | double       | DRep Vote threshold for new committee/threshold (normal state).                                      |
| `dvt_committee_no_confidence`      | double       | DRep Vote threshold for new committee/threshold (state of no-confidence).                            |
| `dvt_update_to_constitution`       | double       | DRep Vote threshold for update to the Constitution.                                                  |
| `dvt_hard_fork_initiation`         | double       | DRep Vote threshold for hard-fork initiation.                                                        |
| `dvt_p_p_network_group`            | double       | DRep Vote threshold for protocol parameter changes, network group.                                   |
| `dvt_p_p_economic_group`           | double       | DRep Vote threshold for protocol parameter changes, economic group.                                  |
| `committee_min_size`               | numeric      | Minimal constitutional committee size.                                                               |
| `committee_max_term_length`        | numeric      | Constitutional committee term limits.                                                                |
| `gov_action_lifetime`              | numeric      | Governance action expiration.                                                                        |
| `gov_action_deposit`               | numeric      | Governance action deposit.                                                                           |
| `drep_deposit`                     | numeric      | DRep deposit amount.                                                                                 |
| `drep_activity`                    | numeric      | DRep activity period.                                                                                |
| `min_fee_ref_script_cost_per_byte` | numeric      | Min fee ref script cost per byte                                                                     |

### `epoch_param`

The accepted protocol parameters for an epoch.

* Primary Id: `id`

| Column name                        | Type         | Description                                                                                         |
|------------------------------------|--------------|-----------------------------------------------------------------------------------------------------|
| `id`                               | integer (64) |                                                                                                     |
| `coins_per_utxo_size`              | numeric      | For Alonzo this is the cost per UTxO word. For Babbage and later per UTxO byte.                     |
| `collateral_percent`               | integer (32) | The percentage of the txfee which must be provided as collateral when including non-native scripts. |
| `decentralisation`                 | double       | The decentralisation parameter (1 fully centralised, 0 fully decentralised).                        |
| `epoch_no`                         | integer (32) | The first epoch for which these parameters are valid.                                               |
| `extra_entropy`                    | string       | The 32 byte string of extra random-ness to be added into the protocol's entropy pool.               |
| `influence`                        | double       | The influence of the pledge on a stake pool's probability on minting a block.                       |
| `key_deposit`                      | numeric      | The amount require for a deposit to register a StakeAddress.                                        |
| `max_bh_size`                      | integer (32) | The maximum block header size (in bytes).                                                           |
| `max_block_ex_mem`                 | numeric      | The maximum number of execution memory allowed to be used in a single block.                        |
| `max_block_ex_steps`               | numeric      | The maximum number of execution steps allowed to be used in a single block.                         |
| `max_block_size`                   | integer (32) | The maximum block size (in bytes).                                                                  |
| `max_collateral_inputs`            | integer (32) | The maximum number of collateral inputs allowed in a transaction.                                   |
| `max_epoch`                        | integer (32) | The maximum number of epochs in the future that a pool retirement is allowed to be scheduled for.   |
| `max_tx_ex_mem`                    | numeric      | The maximum number of execution memory allowed to be used in a single transaction.                  |
| `max_tx_ex_steps`                  | numeric      | The maximum number of execution steps allowed to be used in a single transaction.                   |
| `max_tx_size`                      | integer (32) | The maximum transaction size (in bytes).                                                            |
| `max_val_size`                     | numeric      | The maximum Val size.                                                                               |
| `min_fee_a`                        | integer (32) | The 'a' parameter to calculate the minimum transaction fee.                                         |
| `min_fee_b`                        | integer (32) | The 'b' parameter to calculate the minimum transaction fee.                                         |
| `min_pool_cost`                    | numeric      | The minimum pool cost.                                                                              |
| `min_utxo_value`                   | numeric      | The minimum value of a UTxO entry.                                                                  |
| `monetary_expand_rate`             | double       | The monetary expansion rate.                                                                        |
| `nonce`                            | string       | The nonce value for this epoch.                                                                     |
| `optimal_pool_count`               | integer (32) | The optimal number of stake pools.                                                                  |
| `pool_deposit`                     | numeric      | The amount (in Lovelace) require for a deposit to register a stake pool.                            |
| `price_mem`                        | double       | The per word cost of script memory usage.                                                           |
| `price_step`                       | double       | The cost of script execution step usage.                                                            |
| `protocol_major`                   | integer (32) | The protocol major number.                                                                          |
| `protocol_minor`                   | integer (32) | The protocol minor number.                                                                          |
| `treasury_growth_rate`             | double       | The treasury growth rate.                                                                           |
| `block_id`                         | integer (64) | The Block table index for the first block where these parameters are valid.                         |
| `cost_model_id`                    | integer (64) | The CostModel table index for the params.                                                           |
| `pvt_motion_no_confidence`         | double       | Pool Voting threshold for motion of no-confidence.                                                  |
| `pvt_committee_normal`             | double       | Pool Voting threshold for new committee/threshold (normal state).                                   |
| `pvt_committee_no_confidence`      | double       | Pool Voting threshold for new committee/threshold (state of no-confidence).                         |
| `pvt_hard_fork_initiation`         | double       | Pool Voting threshold for hard-fork initiation.                                                     |
| `pvt_p_p_security_group`           | double       | Pool Vote threshold for protocol parameter changes, security group.                                 |
| `pvt_p_p_technical_group`          | double       | Pool Vote threshold for protocol parameter changes, technical group.                                |
| `pvt_p_p_gov_group`                | double       | Pool Vote threshold for protocol parameter changes, governance group.                               |
| `pvt_treasury_withdrawal`          | double       | Pool Vote threshold for treasury withdrawal.                                                        |
| `dvt_motion_no_confidence`         | double       | DRep Vote threshold for motion of no-confidence.                                                    |
| `dvt_committee_normal`             | double       | DRep Vote threshold for new committee/threshold (normal state).                                     |
| `dvt_committee_no_confidence`      | double       | DRep Vote threshold for new committee/threshold (state of no-confidence).                           |
| `dvt_update_to_constitution`       | double       | DRep Vote threshold for update to the Constitution.                                                 |
| `dvt_hard_fork_initiation`         | double       | DRep Vote threshold for hard-fork initiation.                                                       |
| `dvt_p_p_network_group`            | double       | DRep Vote threshold for protocol parameter changes, network group.                                  |
| `dvt_p_p_economic_group`           | double       | DRep Vote threshold for protocol parameter changes, economic group.                                 |
| `committee_min_size`               | numeric      | Minimal constitutional committee size.                                                              |
| `committee_max_term_length`        | numeric      | Constitutional committee term limits.                                                               |
| `gov_action_lifetime`              | numeric      | Governance action expiration.                                                                       |
| `gov_action_deposit`               | numeric      | Governance action deposit.                                                                          |
| `drep_deposit`                     | numeric      | DRep deposit amount.                                                                                |
| `drep_activity`                    | numeric      | DRep activity period.                                                                               |
| `min_fee_ref_script_cost_per_byte` | numeric      | Min fee ref script cost per byte                                                                    |

### `cost_model`

CostModel for EpochParam and ParamProposal.

* Primary Id: `id`

| Column name | Type         | Description                                               |
|-------------|--------------|-----------------------------------------------------------|
| `id`        | integer (64) |                                                           |
| `costs`     | text         | The actual costs formatted as json.                       |
| `hash`      | string       | The hash of cost model. It ensures uniqueness of entries. |

### `reserved_pool_ticker`

A table containing a managed list of reserved ticker names.

* Primary Id: `id`

| Column name | Type         | Description                                 |
|-------------|--------------|---------------------------------------------|
| `id`        | integer (64) |                                             |
| `name`      | string       | The ticker name.                            |
| `pool_hash` | string       | The hash of the pool that owns this ticker. |

### `delisted_pool`

A table containing pools that have been delisted.

* Primary Id: `id`

| Column name | Type         | Description   |
|-------------|--------------|---------------|
| `id`        | integer (64) |               |
| `hash_raw`  | string       | The pool hash |

### `unconsume_tx_in`

A table for unconsume transaction inputs.

* Primary Id: `id`

| Column name    | Type         | Description                                                                            |
|----------------|--------------|----------------------------------------------------------------------------------------|
| `id`           | integer (64) |                                                                                        |
| `tx_in_id`     | integer (64) | The Tx table index of the transaction that contains this transaction input.            |
| `tx_out_index` | smallint     | The index within the transaction outputs.                                              |
| `tx_out_id`    | integer (64) | The Tx table index of the transaction that contains the referenced transaction output. |
| `redeemer_id`  | integer (64) | The Redeemer table index which is used to validate this input.                         |

### `tx_witnesses`

A table for transaction witnesses.

* Primary Id: `id`

| Column name      | Type         | Description                            |
|------------------|--------------|----------------------------------------|
| `id`             | integer (64) |                                        |
| `tx_id`          | integer (64) | The Tx table index of the transaction. |
| `key`            | string       | Key used for signing the transaction.                |
| `signature`      | string       | The signature of the transaction.      |
| `index_arr`      | integer[]    | Array containing the transaction index. |
| `index_arr_size` | integer (32) | Size of index array.                   |
| `type`           | string       | Type of transaction witnesses.         |

### `tx_bootstrap_witnesses`

A table for transaction bootstrap witnesses.

* Primary Id: `id`

| Column name  | Type         | Description                            |
|--------------|--------------|----------------------------------------|
| `id`         | integer (64) |                                        |
| `tx_id`      | integer (64) | The Tx table index of the transaction. |
| `public_key` | string       | Public key of the transaction.         |
| `signature`  | string       | The signature of the transaction.      |
| `chain_code` | string       | The chain code of the transaction.     |
| `attributes` | string       | The attributes of the transaction.     |

### `rollback_history`

A table for rollback history.

* Primary Id: `id`

| Column name     | Type         | Description         |
|-----------------|--------------|---------------------|
| `id`            | integer (64) |                     |
| `block_hash`    | string       | The hash of block.  |
| `block_no`      | integer (64) | The number of hash. |
| `rollback_time` | timestamp    | Rollback time.      |
| `slot_no`       | integer (64) | The slot number.    |

### `pool_offline_data`

The pool offchain (ie not on chain) for a stake pool.

* Primary Id: `id`

| Column name   | Type         | Description                                             |
|---------------|--------------|---------------------------------------------------------|
| `id`          | integer (64) |                                                         |
| `bytes`       | bytea        | The raw bytes of the payload.                           |
| `hash`        | string       | The hash identifier of the pool.                        |
| `json`        | string       | The payload as JSON.                                 .  |
| `ticker_name` | string       | The pool's ticker name.                                 |
| `pool_id`     | integer (64) | The pool index.                                         |
| `pmr_id`      | integer (64) | The PoolMetadataRef table index for this offchain data. |
| `pool_name`   | string       | Name of pool.                                           |
| `logo_url`    | string       | Logo url of pool.                                       |
| `icon_url`    | string       | Icon url of pool.                                       |

### `pool_offline_fetch_error`

A table containing pool offchain data fetch errors.

* Primary Id: `id`

| Column name   | Type         | Description                                                             |
|---------------|--------------|-------------------------------------------------------------------------|
| `id`          | integer (64) |                                                                         |
| `fetch_error` | string       | The text of the error.                                                  |
| `fetch_time`  | timestamp    | The UTC time stamp of the error.                                        |
| `retry_count` | integer (32) | The number of retries.                                                  |
| `pool_id`     | integer (64) | The PoolHash table index for the pool this offchain fetch error refers. |
| `pmr_id`      | integer (64) | The PoolMetadataRef table index for this offchain data.                 |

### `failed_tx_out`

A table for transaction collateral outputs.

* Primary Id: `id`

| Column name           | Type         | Description                                                                                                                                          |
|-----------------------|--------------|------------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`                  | integer (64) |                                                                                                                                                      |
| `address`             | string       | The human readable encoding of the output address. Will be Base58 for Byron era addresses and Bech32 for Shelley era.                                |
| `address_has_script`  | boolean      | Flag which shows if this address is locked by a script.                                                                                              |
| `address_raw`         | bytea        | Flag which shows if this address is locked by a script.                                                                                              |
| `data_hash`           | string       | The hash of the transaction output datum. (NULL for Txs without scripts).                                                                            |
| `index`               | smallint     | The index of this transaction output with the transaction.                                                                                           |
| `multi_assets_descr`  | text         | This is a description of the multiassets in collateral output. Since the output is not really created, we don't need to add them in separate tables. |
| `payment_cred`        | string       | The payment credential part of the Shelley address. (NULL for Byron addresses). For a script-locked address, this is the script hash.                |
| `value`               | numeric      | The output value (in Lovelace) of the transaction output.                                                                                            |
| `inline_datum_id`     | integer (64) | The inline datum of the output, if it has one.                                                                                                       |
| `reference_script_id` | integer (64) | The reference script of the output, if it has one.                                                                                                   |
| `stake_address_id`    | integer (64) | The StakeAddress table index for the stake address part of the Shelley address. (NULL for Byron addresses).                                          |
| `tx_id`               | integer (64) | The Tx table index of the transaction that contains this transaction output.                                                                         |
