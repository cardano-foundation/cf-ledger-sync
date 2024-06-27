# I. Assets store

## A. Analyze **assets** table

### 1. multi_asset - assets

| **Ledger_Sync** | **Yaci_Store** | **Note**                                                     |
|:----------------|:---------------|:-------------------------------------------------------------|
| finger_print    | finger_print   |                                                              |
| name_view       | asset_name     |                                                              |
| policy          | policy         |                                                              |
| unit            | unit           |                                                              |
| time            | block_time     |                                                              |
| name            |                | LedgerSync: Hex string of name_view (`Currently not in use`) |
| supply          |                | totalSupply                                                  |

### 2. ma_tx_mint - assets

| Ledger_Sync | Yaci_Store | Note |
|:------------|:-----------|:-----|
| tx_id       | tx_hash    |      |
| quantity    | quantity   |      |

# II. Block store

## A. Analyze **block** table

### 1. block - block

| **Ledger_Sync**          | **Yaci_Store**     | **Note**                                                                                                       |
|:-------------------------|:-------------------|:---------------------------------------------------------------------------------------------------------------|
| block_no                 | number             |                                                                                                                |
| epoch_no                 | epoch              |                                                                                                                |
| epoch_slot_no            | epoch_slot         |                                                                                                                |
| hash                     | hash               |                                                                                                                |
| op_cert                  | op_cert_hot_vkey   |                                                                                                                |
| op_cert_counter          | op_cert_seq_number |                                                                                                                |
| proto_major, proto_minor | protocol_version   |                                                                                                                |
| size                     | body_size          |                                                                                                                |
| slot_leader_id           | slot_leader        | In ledger_sync, slot_leader_id is id of slot_leader table                                                      |
| slot_no                  | slot               |                                                                                                                |
| time                     | block_time         |                                                                                                                |
| tx_count                 | no_of_txs          |                                                                                                                |
| vrf_key                  | vrf_vkey           | <p>Bech32.encode(HexUtil.decodeHexString(headerBody.getVrfVkey()), ConsumerConstant.VRF_KEY_PREFIX)</p><p></p> |
| previous_id              | prev_hash          |                                                                                                                |

### 2. ada_pots - block

| **Ledger_Sync** | **Yaci_Store** | **Note** |
|:----------------|:---------------|:---------|
| fees            | total_fees     |          |

### 3. slot_leader - block

| **Ledger_Sync** | **Yaci_Store** | **Note** |
|:----------------|:---------------|:---------|
| hash            | slot_leader    |          |

## B. Analyze **roll_back** table

- Not found in Ledger_sync

# III. Epoch store

## A. Analyze **local_protocol_params** table

- Not found in Ledger_sync

## B. Analyze **protocol_params_proposal** table

- param_proposal + cost model = protocol_params_proposal

### 1. param_proposal - protocol_params_proposal

| **Ledger_Sync**  | **Yaci_Store** | **Note** |
|:-----------------|:---------------|:---------|
| epoch_no         | epoch          |          |
| key              | key_hash       |          |
| registered_tx_id | tx_hash        |          |
| others           | params         |          |

### 2. cost_model - protocol_params_proposal

| **Ledger_Sync** | **Yaci_Store** | **Note** |
|:----------------|:---------------|:---------|
|                 |                |          |

## C. Analyze **cost_model** table

### 1. cost_model - cost_model

| **Ledger_Sync** | **Yaci_Store** | **Note** |
|:----------------|:---------------|:---------|
| costs           | costs          |          |
| hash            | hash           |          |

## D. Analyze **epoch_param** table

### 1. epoch_param - epoch_param

| **Ledger_Sync** | **Yaci_Store**  | **Note** |
|:----------------|:----------------|:---------|
| epoch_no        | epoch           |          |
| cost_model_id   | cost_model_hash |          |
| others          | params          |          |

# IV. Epoch aggregate

## A. Analyze  **transaction_metadata** table

### 1. epoch - epoch

| **Ledger_Sync**     | **Yaci_Store**    | **Note**                                                                                         |
|:--------------------|:------------------|:-------------------------------------------------------------------------------------------------|
| no                  | number            |                                                                                                  |
| blk_count           | block_count       |                                                                                                  |
| start_time          | start_time        |                                                                                                  |
| end_time            | end_time          |                                                                                                  |
| fees                | total_fees        |                                                                                                  |
| out_sum             | total_output      |                                                                                                  |
| tx_count            | transaction_count |                                                                                                  |
| max_slot            | max_slot          | LedgerSync: The maximum slot of each epoch<br>YaciStore: The slot of the last block in the epoch |
| era                 |                   | Can be additionally stored in YaciStore.                                                         |
| rewards_distributed |                   | All values are null.                                                                             |

# V. Governance store

- Ledger_sync is using the entire table from yaci_store.

# VI. Live store

# VII. Metadata store

## A. Analyze  **transaction_metadata** table

- tx_metadata + tx_metadata_hash = transaction_metadata

### 1. tx_metadata - transaction_metadata

| **Ledger_Sync** | **Yaci_Store** | **Note** |
|:----------------|:---------------|:---------|
| tx_id           | tx_hash        |          |
| key             | label          |          |
| json            | body           |          |
| bytes           | cbor           |          |

### 2. tx_metadata_hash - transaction_metadata

| **Ledger_Sync** | **Yaci_Store** | **Note** |
|:----------------|:---------------|:---------|
| hash            | tx_hash        |          |

# VIII. Mir store

- Regarding reward.

# IX. Script store

- script + redeemer +redeemer_data + datum = script + transaction_script + datum

## A. Analyze **script** table

### 1. script - script

- In yaci_store, the main records are mostly PLUTUS, with a small portion being about Native Script.
- The data is less compared to ledger_sync.

| **Ledger_Sync** | **Yaci_Store** | **Note**                                                          |
|:----------------|:---------------|:------------------------------------------------------------------|
| bytes           | content        | has bytes is plutus data                                          |
| hash            | script_hash    |                                                                   |
| json            | content        | hash json is timelock script                                      |
| type            | script_type    |                                                                   |
| serialised_size |                | The size of the CBOR serialised script, if it is a Plutus script. |
| tx_id           | tx_hash        |                                                                   |
| verified        |                | All values are false.                                             |

## B. Analyze **transaction_scripts** table

### 2. redeemer - transaction_scripts

| **Ledger_Sync**  | **Yaci_Store**                                   | **Note**             |
|:-----------------|:-------------------------------------------------|:---------------------|
| index            | redeemer_index                                   |                      |
| purpose          | purpose                                          |                      |
| script_hash      | script_hash                                      |                      |
| unit_mem         | unit_mem                                         |                      |
| unit_steps       | unit_steps                                       |                      |
| tx_id            | tx_hash                                          |                      |
| fee              |                                                  | All values are null. |
| redeemer_data_id | redeemer_cbor, redeemer_index, redeemer_datahash |                      |

### 2. redeemer_data - transaction_scripts

| **Ledger_Sync** | **Yaci_Store**    | **Note**                                  |
|:----------------|:------------------|:------------------------------------------|
| hash            | redeemer_datahash |                                           |
| bytes           |                   | Join with the datatum table in yaci_store |
| value           |                   |                                           |

## C. Analyze **datum** table

### 2. datum - datum

| **Ledger_Sync** | **Yaci_Store** | **Note** |
|:----------------|:---------------|:---------|
| hash            | hash           |          |
| bytes           | datum          |          |
| tx_id           | created_at_tx  |          |
| value           |                |          |

# X. Staking store

- stake_registration + stake_deregistration = stake_registration
- pool_hash + pool_metadata_ref + pool_owner + pool_relays + pool_update = delegation

## A. Analyze **stake_registration** table

### 1. stake_registration - stake_registration

| **Ledger_Sync** | **Yaci_Store** | **Note** |
|:----------------|:---------------|:---------|
| cert_index      | cert_index     |          |
| epoch_no        | epoch          |          |
| addr_id         | address        |          |
| tx_id           | tx_hash        |          |

### 2. stake_deregistration - stake_registration

| **Ledger_Sync** | **Yaci_Store** | **Note**                                             |
|:----------------|:---------------|:-----------------------------------------------------|
| cert_index      | cert_index     |                                                      |
| epoch_no        | epoch          |                                                      |
| addr_id         | address        |                                                      |
| tx_id           | tx_hash        |                                                      |
| redeemer_id     |                | Join with the transaction_script table in yaci_store |

##

## B. Analyze **delegation** table

### 1. delegation - delegation

| **Ledger_Sync** | **Yaci_Store** | **Note**                                             |
|:----------------|:---------------|:-----------------------------------------------------|
| active_epoch_no | epoch          |                                                      |
| cert_index      | cert_index     |                                                      |
| slot_no         | slot           |                                                      |
| addr_id         | address        |                                                      |
| pool_hash_id    | pool_id        |                                                      |
| tx_id           | tx_hash        |                                                      |
| redeemer_id     |                | Join with the transaction_script table in yaci_store |

## C. Analyze **pool_registration** table

### 1. pool_hash - pool_registration

| **Ledger_Sync** | **Yaci_Store** | **Note**                                                                                                |
|:----------------|:---------------|:--------------------------------------------------------------------------------------------------------|
| hash_raw        | pool_id        |                                                                                                         |
| epoch_no        | epoch          |                                                                                                         |
| pool_size       |                | Hard-fix at 0                                                                                           |
| view            |                | Ledger_sync: ```Bech32.encode(HexUtil.decodeHexString(hash_raw), ConsumerConstant.POOL_HASH_PREFIX))``` |

### 2. pool_metadata_ref - pool_registration

| **Ledger_Sync**  | **Yaci_Store** | **Note** |
|:-----------------|:---------------|:---------|
| url              | metadata_url   |          |
| hash             | metadata_hash  |          |
| pool_id          | pool_id        |          |
| registered_tx_id | tx_hash        |          |

### 3. pool_owner - pool_registration

| **Ledger_Sync** | **Yaci_Store** | **Note** |
|:----------------|:---------------|:---------|
| addr_id         | pool_owners    |          |
| pool_update_id  |                |          |

### 4. pool_relay - pool_registration

| **Ledger_Sync**                          | **Yaci_Store** | **Note** |
|:-----------------------------------------|:---------------|:---------|
| dns_name, dns_srv_name, ipv4, ipv6, port | relays         |          |
| update_id                                |                |          |

### 5. pool_update - pool_registration

| **Ledger_Sync**  | **Yaci_Store**              | **Note**                                                                                                                                             |
|:-----------------|:----------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------|
| cert_index       | cert_index                  |                                                                                                                                                      |
| fixed_cost       | cost                        |                                                                                                                                                      |
| margin           | margin                      |                                                                                                                                                      |
| pledge           | pledge                      |                                                                                                                                                      |
| vrf_key_hash     | vrf_key                     |                                                                                                                                                      |
| meta_id          | metadata_url, metadata_hash |                                                                                                                                                      |
| registered_tx_id | tx_hash                     |                                                                                                                                                      |
| reward_addr_id   | reward_account              |                                                                                                                                                      |
| active_epoch_no  |                             | Ledger_sync logic:<br>epochActivationDelay + aggregatedBlock.getEpochNo()<br>epochActivationDelay: If the pool is first registered, return 2, else 3 |

## D. Analyze **pool_retirement** table

### 1. pool_retire - pool_retirement

| **Ledger_Sync** | **Yaci_Store**   | **Note** |
|:----------------|:-----------------|:---------|
| cert_index      | cert_index       |          |
| retiring_epoch  | retirement_epoch |          |
| announced_tx_id | tx_hash          |          |
| hash_id         | pool_id          |          |

# XI. Transaction store

## A. Analyze **transaction** table

### 1. tx - transaction

| **Ledger_Sync**     | **Yaci_Store**     | **Note**                                                                                                                                                                                                                                                                            |
|:--------------------|:-------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| block_id            | block_hash         |                                                                                                                                                                                                                                                                                     |
| fee                 | fee                |                                                                                                                                                                                                                                                                                     |
| hash                | tx_hash            |                                                                                                                                                                                                                                                                                     |
| tx_metadata_hash_id | auxiliary_datahash |                                                                                                                                                                                                                                                                                     |
| block_index         |                    | Index of transaction in block                                                                                                                                                                                                                                                       |
| deposit             |                    |                                                                                                                                                                                                                                                                                     |
| invalid_before      |                    | All values are null.                                                                                                                                                                                                                                                                |
| invalid_hereafter   |                    | All values are null.                                                                                                                                                                                                                                                                |
| out_sum             |                    | The sum of the transaction outputs (in Lovelace).                                                                                                                                                                                                                                   |
| script_size         |                    | All values are null.                                                                                                                                                                                                                                                                |
| size                |                    | <p>Definition: The size of the transaction in bytes.<br>Logic in ledger_sync :<br>size là size của script<br>public void addScriptSize(int size) {</p><p>if (this.size == null) {</p><p> &nbsp;&nbsp;&nbsp;&nbsp;this.size = 0;</p><p>}</p><p>this.size += size;</p><p>}</p><p></p> |
| valid_contract      |                    | Logic in ledger_sync:<br>validContract = !invalidTransactions.contains(txIdx);<br>                                                                                                                                                                                                  |

## B. Analyze **transaction_witness** table

### 1. tx_witness - transaction_witness

| **Ledger_Sync**           | **Yaci_Store** | **Note**                                |
|:--------------------------|:---------------|:----------------------------------------|
| tx_id                     | tx_hash        |                                         |
| key                       | pub_key        |                                         |
| signature                 | signature      |                                         |
| index_arr, index_arr_size | idx            |                                         |
| type                      | type           | In Ledger_sync, the type is always null |

### 2. extra_key_witness - transaction_witness

| **Ledger_Sync** | **Yaci_Store** | **Note** |
|:----------------|:---------------|:---------|
| hash            | pub_keyhash    |          |
| tx_id           | tx_hash        |          |

## C. Analyze **withdrawal** table

### 1. withdrawal - withdrawal

| **Ledger_Sync** | **Yaci_Store** | **Note**                                             |
|:----------------|:---------------|:-----------------------------------------------------|
| amount          | amount         |                                                      |
| addr_id         | address        |                                                      |
| tx_id           | tx_hash        |                                                      |
| redeemer_id     |                | Join with the transaction_script table in yaci_store |

## D. Analyze **invalid_transaction** table

- Not found in Ledger_sync

# XII. Utxo store

## A. Analyze **address_utxo** table

### 1. tx_out - address_utxo

| **Ledger_Sync**     | **Yaci_Store**           | **Note**                                                       |
|:--------------------|:-------------------------|:---------------------------------------------------------------|
| address             | owner_addr               |                                                                |
| data_hash           | data_hash                |                                                                |
| index               | output_index             |                                                                |
| payment_cred        | owner_payment_credential |                                                                |
| value               | lovelace_amount          |                                                                |
| reference_script_id | reference_script_hash    |                                                                |
| stake_address_id    | owner_stake_addr         |                                                                |
| tx_id               | tx_hash                  |                                                                |
| address_has_script  |                          |                                                                |
| address_raw         |                          |                                                                |
| token_type          |                          |                                                                |
| inline_datum_id     | inline_datum             | Join with the transaction_script and datum table in yaci_store |

### 1. ma_tx_out - address_utxo

| **Ledger_Sync** | **Yaci_Store**       | **Note**                                                                            |
|:----------------|:---------------------|:------------------------------------------------------------------------------------|
| quantity        | amounts->>'quantity' |                                                                                     |
| ident           |                      | LS: Reference to `multi_assets` table. <br>Yaci store: Can join with `assets` table |
| tx_out_id       | tx_hash              |                                                                                     |

## B. Analyze **tx_input** table

### 1. tx_in - tx_input

| **Ledger_Sync** | **Yaci_Store** | **Note**                                             |
|:----------------|:---------------|:-----------------------------------------------------|
| tx_in_id        | spent_tx_hash  |                                                      |
| tx_out_index    | output_index   |                                                      |
| tx_out_id       | tx_hash        |                                                      |
| redeemer_id     |                | Join with the transaction_script table in yaci_store |

## C. Analyze **address** table

- Currently, the address table of yaci_store is being used in ledger_sync.

# XIII.Gap between ledger_sync and yaci_store

## Asset store

- [ ] Need to add a `supply` column to the asset table.

## Script store

- [ ] Need to add a `value` column for redeemer_data.
- [ ] Need to add a `value` column for datum.

## Staking store

- [ ] Need to add a `pool_view` column to the pool_registration table or new table
    - pool_view = Bech32.encode(HexUtil.decodeHexString(hash_raw), ConsumerConstant.POOL_HASH_PREFIX))

## Transaction store

- [ ] Need to add a `block_index` column for transaction.
- [ ] Need to add a `deposit` column for transaction.
- [ ] Need to add a `out_sum` column for transaction.
- [ ] Need to add a `size` column for transaction.

## Utxo store

- [ ] Need to add a `address_has_script` column for address_utxo.
- [ ] Need to add a `address_raw` column for address_utxo.
- [ ] Need to add a `token_type` column for address_utxo.