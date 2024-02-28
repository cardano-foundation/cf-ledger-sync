# Research LedgerSync and YaciStore

## I.Needed LedgerSync tables using by explorer

| Table                   | Note |
| ----------------------- | ---- |
| ada_pots                |      |
| address                 |      |
| address_token           |      |
| address_token_balance   |      |
| address_tx_balance      |      |
| assert_metadata         |      |
| block                   |      |
| cost_model              |      |
| datum                   |      |
| delegation              |      |
| epoch                   |      |
| epoch_param             |      |
| epoch_stake             |      |
| epoch_stake_checkpoint  |      |
| failed_tx_out           |      |
| ma_tx_mint              |      |
| ma_tx_out               |      |
| multi_asset             |      |
| param_proposal          |      |
| pool_hash               |      |
| pool_history            |      |
| pool_history_checkpoint |      |
| pool_info               |      |
| pool_info_checkpoint    |      |
| pool_metadata_ref       |      |
| pool_offline_data       |      |
| pool_owner              |      |
| pool_relay              |      |
| pool_retire             |      |
| pool_update             |      |
| redeemer                |      |
| redeemer_data           |      |
| reference_tx_in         |      |
| reserve                 |      |
| reward                  |      |
| reward_checkpoint       |      |
| script                  |      |
| slot_leader             |      |
| stake_address           |      |
| stake_deregistration    |      |
| stake_registration      |      |
| stake_tx_balance        |      |
| treasury                |      |
| tx                      |      |
| tx_bootstrap_witnesses  |      |
| tx_chart                |      |
| tx_metadata             |      |
| tx_metadata_hash        |      |
| tx_out                  |      |
| tx_witnesses            |      |
| unconsume_tx_in         |      |
| withdrawal              |      |

## II.List table use in ledger sync but not in yaci store

| Table                    | Note              |
| ------------------------ | ----------------- |
| ada_pots                 | related to reward |
| address                  |                   |
| address_token            |                   |
| address_token_balance    |                   |
| address_tx_balance       |                   |
| agg_address_token        |                   |
| agg_address_tx_balance   |                   |
| asset_metadata           |                   |
| delisted_pool            |                   |
| epoch_stake              | related to reward |
| epoch_sync_time          |                   |
| extra_key_witness        |                   |
| failed_tx_out            |                   |
| ma_tx_mint               |                   |
| ma_tx_out                |                   |
| meta                     |                   |
| multi_asset              |                   |
| param_proposal           |                   |
| pool_hash                |                   |
| pool_meta_ref            |                   |
| pool_offline_data        |                   |
| pool_offline_fetch_error |                   |
| pool_owner               |                   |
| pool_replay              |                   |
| pool_retire              |                   |
| pool_update              |                   |
| pot_transfer             |                   |
| redeemer                 |                   |
| redeemer_data            |                   |
| reference_tx_in          |                   |
| reserve                  |                   |
| reserved_pool_ticker     |                   |
| reward                   | related to reward |
| rollback_history         |                   |
| slot_leader              |                   |
| stake_address            |                   |
| stake_deregistration     |                   |
| treasury                 |                   |
| unconsume_tx_in          |                   |
| withdrawal               |                   |

## III.List table use in ledger sync maybe in yaci store

1. Analyze cost_model table (Can replaced with cost_model table in yaci_store DB)
    - Compare columns with yaci store: 
        | Ledger_Sync DB | Yaci_Store DB |
        | -------------- | ------------- |
        | costs          | costs         |
        | hash           | hash          |
    - References tables: `param_proposal`, `epoch_param`
    - Query used in explorer:
        |          | Table related | Note |
        | -------- | ------------- | ---- |
        | findAll  |               |      |
        | findById |               |      |

    💡Solution: Change `findById` to `findByHash`, and relation by hash rather `id`
        
2. Analyze datum table (Can replaced with datum table in yaci_store DB)
    - Compare columns with yaci store:  
        | Ledger_Sync DB | Yaci_Store DB |
        | -------------- | ------------- |
        | bytes          | datum         |
        | hash           | hash          |
        | values         | N/A           |
    - References tables: 
    - Query used in explorer:
        |                             | Table related                                                                                                                                                                                            | Note                             |
        | --------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------- |
        | getReferenceInputByTx       | - reference_tx_in<br> - tx_out<br> - tx<br> - script<br> - `datum`<br> - ma_tx_out<br> - multi_asset                                                                                                     | using datum.hash and datum.bytes |
        | findContractByTx            | - redeemer<br> - tx<br> - redeemer_data<br> - tx_in<br> - tx_out<br> - `datum`<br> - script<br> - withdrawal<br> - delegation<br> - stake_deregistration<br> - stake_address<br> - tx                    | using datum.hash and datum.bytes |
        | findContractByTxFail        | - redeemer<br> - tx<br> - redeemer_data<br> - tx<br> - unconsume_tx_in<br> - tx_out<br> - `datum`<br> - script<br> - withdrawal<br> - delegation<br> - stake_deregistration<br> - stake_address<br> - tx |                                  |
        | getContractDatumOutByTx     | - tx_out<br> - `datum`                                                                                                                                                                                   | using datum.hash and datum.bytes |
        | getContractDatumOutByTx     | - tx_out<br> - `datum`                                                                                                                                                                                   | using datum.hash and datum.bytes |
        | getContractDatumOutByTxFail | - failed_tx_out<br> - `datum`                                                                                                                                                                            | using datum.hash and datum.bytes |

    💡Note: The `values` column is not currently in use.

3. Analyze delegation table(Can replaced with delegation table in yaci_store DB)
    - Compare columns with yaci store: 
        | Ledger_Sync DB  | Yaci_Store DB |
        | --------------- | ------------- |
        | active_epoch_no | epoch         |
        | cert_index      | cert_index    |
        | slot_no         | slot          |
        | addr_id         | address       |
        | pool_hash_id    | pool_id       |
        | tx_id           | tx_hash       |
        | redeemer_id     | N/A           |
    - References tables: 
    - Query used in explorer:
        |                                | Table related                                                                                            | Note |
        | ------------------------------ | -------------------------------------------------------------------------------------------------------- | ---- |
        | findDelegationByAddressAndTxIn | - `delegation`<br> - tx                                                                                    |      |
        | findDelegationByAddress        | - `delegation`<br> - tx<br> - block<br> - stake_address<br> - pool_hash<br> - pool_offline_data            |      |
        | findDelegationByAddress        | - `delegation`<br> - tx<br> - block<br> - pool_offline_data                                                |      |
        | findDelegationByAddressAndTx   | - `delegation`<br> - tx<br> - block<br> - pool_hash<br> - pool_offline_data                                |      |
        | findPoolDataByAddress          | - `delegation`<br> - pool_hash<br> - pool_offline_data<br> - stake_registration<br> - stake_deregistration |      |
        | findPoolDataByAddressIn        | - `delegation`<br> - stake_address<br> - pool_hash<br> - pool_offline_data                                 |      |
        | liveDelegatorsList             | - `delegation`<br> - pool_hash<br> - tx<br> - stake_deregistration                                         |      |
        | findDelegationByTxIdIn         | - `delegation`<br> - stake_address<br> - pool_hash<br> - pool_offline_data                                 |      |

    💡Note: The `redeemer_id` column is not currently in use.

4. Analyze epoch table (Can replaced with epoch table in yaci_store DB)
    - Compare columns with yaci store: 
        | Ledger_Sync DB      | Yaci_Store DB     |
        | ------------------- | ----------------- |
        | blk_count           | block_count       |
        | end_time            | end_time          |
        | fees                | total_fees        |
        | max_slot            | max_slot          |
        | no                  | number            |
        | out_sum             | total_output      |
        | start_time          | start_time        |
        | tx_count            | transaction_count |
        | era                 | N/A               |
        | rewards_distributed | N/A               |
    - References tables: 
    - Query use in explorer:
        |                         | Table related | Note |
        | ----------------------- | ------------- | ---- |
        | findFirstByNo           | - epoch       |      |
        | findCurrentEpochNo      | - epoch       |      |
        | findCurrentEpochSummary | - epoch       |      |
        | findByCurrentEpochNo    | - epoch       |      |
        | findEpochTime           | - epoch       |      |

    💡Note: The `era` and `rewards_distributed` columns is not currently in use.

5.  Analyze epoch_param table (Can replaced with epoch_param table in yaci_store DB if find `optimal_pool_count` column)
    - Compare columns with yaci store: 
        | Ledger_Sync DB        | Yaci_Store DB                 |
        | --------------------- | ----------------------------- |
        | epoch_no              | epoch                         |
        | coins_per_utxo_size   | params.ada_per_utxo_byte      |
        | collateral_percent    | params.collateral_percent     |
        | decentralisation      | params.decentralisation_param |
        | extra_entropy         | params.extra_entropy          |
        | influence             | params.pool_pledge_influence  |
        | key_deposit           | params.key_deposit            |
        | max_bh_size           | params.max_block_header_size  |
        | max_block_ex_mem      | params.max_block_ex_mem       |
        | max_block_ex_steps    | params.max_block_ex_steps     |
        | max_block_size        | params.max_block_size         |
        | max_collateral_inputs | params.max_collateral_inputs  |
        | max_epoch             | params.max_epoch              |
        | max_tx_ex_mem         | params.max_tx_ex_mem          |
        | max_tx_ex_steps       | params.max_tx_ex_steps        |
        | max_tx_size           | params.max_tx_size            |
        | max_val_size          | params.max_val_size           |
        | min_fee_a             | params.min_fee_a              |
        | min_fee_b             | params.min_fee_b              |
        | min_pool_cost         | params.min_pool_cost          |
        | min_utxo_value        | params.min_utxo               |
        | monetary_expand_rate  | params.expansion_rate         |
        | pool_deposit          | params.pool_deposit           |
        | price_mem             | params.price_mem              |
        | price_step            | params.price_step             |
        | protocol_major        | params.protocol_major_ver     |
        | protocol_minor        | params.protocol_minor_ver     |
        | treasury_growth_rate  | params.treasury_growth_rate   |
        | block_id              | block                         |
        | cost_model_id         | cost_model_hash               |
        | nonce                 | N/A                           |
        | optimal_pool_count    | N/A                           |
    - References tables: 
    - Query used in explorer:
        |                         | Query                        | Table related                                                                                                | Note                            |
        | ----------------------- | ---------------------------- | ------------------------------------------------------------------------------------------------------------ | ------------------------------- |
        | PoolHashRepository      | getDataForPoolDetailNoReward | - `epoch_param`<br> - pool_hash<br> - pool_offline_data<br> - pool_update<br> - stake_address                | Using `optimalPoolCount` column |
        |                         | getDataForPoolDetail         | - `epoch_param`<br> - pool_hash<br> - pool_offline_data<br> - pool_update<br> - stake_address<br> - ada_pots | Using `optimalPoolCount` column |
        |                         | getPoolRegistrationByPool    | - `epoch_param`<br> - pool_update<br> - block<br> - stake_address                                            | Using `optimalPoolCount` column |
        | EpochParamRepository    | findEpochParamByEpochNo      |                                                                                                              |                                 |
        |                         | findEpochParamInTime         |                                                                                                              |                                 |
        |                         | getOptimalPoolCountByEpochNo |                                                                                                              | Using `optimalPoolCount` column |
        |                         | findKeyDepositByEpochNo      |                                                                                                              | Using `optimalPoolCount` column |
        |                         | findByEpochNoIn              |                                                                                                              |                                 |
        | ParamProposalRepository | findMaxEpochChange           | - `epoch_param`<br> - param_proposal                                                                         | Using `epochNo` column          |
        |                         | findProtocolsChange          | - `epoch_param`<br> - param_proposal<br> - tx<br> - block<br> - epoch                                        |                                 |
        | PoolUpdateRepository    | findPoolRegistrationByPool   | - `epoch_param`<br> - pool_update<br> - tx<br> - block                                                       |                                 |
    💡 Note:<br>
            - `nonce`is alway Null in Ledger Sync DB <br>
            - Not found `optimal_pool_count` in yaci store DB
6. Analyze script table (It can replace the script of the smart contract, but the script of the native script is missing)
    - Compare columns with yaci store: 
        | Ledger_Sync DB  | Yaci_Store DB   |
        | --------------- | --------------- |
        | bytes           | content.content |
        | hash            | script_hash     |
        | type            | script_type     |
        | json            | N/A             |
        | serialised_size | N/A             |
        | tx_id           | N/A             |
        | verified        | N/A             |
    - References tables: 
    - Query used in explorer:
        |                             |                            | Table related | Note |
        | --------------------------- | -------------------------- | ------------- | ---- |
        | SmartContractInfoRepository | findAllByFilterRequest     |               |      |
        |                             | getTxCountByScriptHash     |               |      |
        | NativeScriptInfoRepository  | findAll                    |               |      |
        |                             | findByScriptHash           |               |      |
        | ScriptRepository            | findByHash                 |               |      |
        |                             | findAllByHashIn            |               |      |
        | VerifiedScriptRepository    | existsVerifiedScriptByHash |               |      |
        |                             | findByHash                 |               |      |
        |                             | save                       |               |      |

    💡 Note:<br>
          - In the yaci store, data for the native script is currently missing.<br>
          - Data for the smart contract in the yaci store is less than what's in the ledger sync.
7. Analyze stake_registration table (Can replaced with stake_registration table in yaci_store DB)
    - Compare columns with yaci store: 
        | Ledger_Sync DB | Yaci_Store DB |
        | -------------- | ------------- |
        | cert_index     | cert_index    |
        | epoch_no       | epoch         |
        | address_id     | address       |
        | tx_id          | tx_hash       |
    - References tables: 
    - Query used in explorer:
        |                             | Query                                 | Table related                                                                                              | Note |
        | --------------------------- | ------------------------------------- | ---------------------------------------------------------------------------------------------------------- | ---- |
        | StakeRegistrationRepository | findMaxTxIdByStake                    | - `stake_registration`                                                                                     |      |
        |                             | getStakeRegistrationsByAddress        | - `stake_registration`<br> - tx<br> - block<br> - stake_address                                            |      |
        |                             | getStakeRegistrationsByAddressAndTxIn | - `stake_registration`<br> - stake_address                                                                 |      |
        |                             | getStakeRegistrationsByAddress        | - `stake_registration`<br> - tx<br> - block<br> - stake_address                                            |      |
        |                             | findByAddressAndTx                    | - `stake_registration`<br> - tx<br> - block<br> - stake_address                                            |      |
        |                             | findByTx                              | - `stake_registration`                                                                                     |      |
        |                             | existsByAddr                          | - `stake_registration`                                                                                     |      |
        | DelegationRepository        | getDelegatorsByAddress                | - `stake_registration`<br> - stake_address<br> - tx<br> - block                                            |      |
        |                             | findPoolDataByAddress                 | - `stake_registration`<br> - delegation<br> - pool_hash<br> - pool_offline_data<br> - stake_deregistration |      |
        | StakeAddressRepository      | findStakeAddressOrderByBalance        | - `stake_registration`<br> - delegation<br> - stake_address<br> - stake_deregistration                     |      |

8. Analyze stake_deregistration table (Can replaced with stake_registration table in yaci_store DB)
    - Compare columns with yaci store:
        | Ledger_Sync DB | Yaci_Store DB |
        | -------------- | ------------- |
        | cert_index     | cert_index    |
        | epoch_no       | epoch         |
        | address_id     | address       |
        | tx_id          | tx_hash       |
        | redeemer_id    | N/A           |
    - References tables: 
    - Query used in explorer:
        |                        | Query                                    | Table related                                                                                              | Note                                |
        | ---------------------- | ---------------------------------------- | ---------------------------------------------------------------------------------------------------------- | ----------------------------------- |
        | StakeDeRegistration    | findMaxTxIdByStake                       | - `stake_deregistration`<br> - stake_address                                                               |                                     |
        | Repository             | getStakeDeRegistrationsByAddress         | - `stake_deregistration`<br> - tx<br> - block<br> - stake_address                                          |                                     |
        |                        | getStakeDeRegistrationsByAddressAndTxIn  | - `stake_deregistration`<br> - stake_address                                                               |                                     |
        |                        | getStakeDeRegistrationsByAddress         | - `stake_deregistration`<br> - tx<br> - block<br> - stake_address                                          |                                     |
        |                        | findByAddressAndTx                       | - `stake_deregistration`<br> - tx<br> - block<br> - stake_address                                          |                                     |
        |                        | findByTx                                 | - `stake_deregistration`                                                                                   |                                     |
        |                        | existsByAddr                             | - `stake_deregistration`                                                                                   |                                     |
        | DelegationRepository   | findPoolDataByAddress                    | - `stake_deregistration`<br> - delegation<br> - pool_hash<br> - pool_offline_data<br> - stake_registration |                                     |
        |                        | liveDelegatorsList                       | - `stake_deregistration`<br> - delegation<br> - pool_hash<br> - stake_address<br> - tx                     |                                     |
        | StakeAddressRepository | findStakeAddressOrderByBalance           | - `stake_deregistration`<br> - delegation<br> - stake_registration                                         |                                     |
        | RedeemerRepository     | findContractByTx                         |                                                                                                            | Not using state deregistration info |
        |                        | findContractByTxFail                     |                                                                                                            | Not using state deregistration info |
                  
    💡Note: The `redeemer_id` column is not currently in use.

9. Analyze tx_metadata table (Can replaced with transaction_metadata in yaci_store DB)
    - Compare columns with yaci store:
        | Ledger_Sync DB | Yaci_Store DB |
        | -------------- | ------------- |
        | bytes          | cbor          |
        | json           | body          |
        | key            | lablel        |
        | tx_id          | tx_hash       |
    - References tables: 
    - Query used in explorer:
        |                      | Query                    | Table related                                    | Note |
        | -------------------- | ------------------------ | ------------------------------------------------ | ---- |
        | TxMetadataRepository | findAllByTxOrderByKeyAsc |                                                  |      |
        |                      | findAllByTxHash          |                                                  |      |
        | MaTxMintRepository   | getTxMetadataToken       | - tx_metadata<br> - ma_tx_mint<br> - multi_asset |      |

10. Analyze tx_bootstrap_witnesses table (Can replaced with transaction_witness in yaci_store DB)
    - Compare columns with yaci store:
        | Ledger_Sync DB | Yaci_Store DB   |
        | -------------- | --------------- |
        | tx_id          | tx_hash         |
        | public_key     | pub_key         |
        | signature      | signature       |
        | chain_code     | additional_data |
        | attributes     | additional_data |
    - References tables: 
    - Query use in explorer:
        |     | Table related | Note |
        | --- | ------------- | ---- |