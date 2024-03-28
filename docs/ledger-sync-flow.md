# I. Handle Genesis block
1. Listen event `GenesisBlockEvent` from Yaci store
2. SetupData
    1. `setup byron genesis data`
    2. `fetchTransactionAndTransactionOutput` 
        1. Set startTime, transaction, transactionOutput for genesisData
    3. `fetchBlockAndSlotLeader`
        1. Set slot leader and block for genesisData
    4. Save slotLeader
    5. `saveAggregatedBlock` to memory
    6. `blockSyncService.startBlockSyncing`
        1. [handleBlockSync](#v-handleblocksync)
# II. Handle ByronEbBlock
1. `aggregateBlock`
    1. Create `AggregatedBlock`
2. `handleAggregateBlock` (EventMetadata, AggregatedBlock)
    1. [handleBlockSync](#v-handleblocksync)
# III. Handle ByronMainBlock
1. `aggregateBlock`
    1. Create `AggregatedBlock`
2. `handleAggregateBlock` (EventMetadata, AggregatedBlock)
    1. [handleBlockSync](#v-handleblocksync)
# IV. Handle Block
1. `aggregateBlock`
    1. Create `AggregatedBlock`
2. `handleAggregateBlock` (EventMetadata, AggregatedBlock)
    1. [handleBlockSync](#v-handleblocksync)

# V. HandleBlockSync

1. handleBlockSync
    1. `collectCountBlockProcessingMetric`
    2. `collectCurrentBlockMetric`
    3. `collectCurrentEraMetric`
    4. `collectNetworkSyncPercentMetric`
    5. `collectEraAndEpochProcessingMetric`
    6. save list (`block` table)
    7. `prepareAndHandleTxs:`Prepare and handle transaction contents
        1. `Handle and save tx metadata hash`
        2. Save Transaction to `(tx table)` 
        3. `handleScripts:`handleScriptFromTxOut and saveNonExistsScripts (`script` table)
        4. `handleTxWitnesses`
            1. `handleTxWitness` save list txWitnesses (`tx_witnesses` table)
            2. `handleBootstrapWitnesses` save list txBootstrapWitnesses (`tx_bootstrap_witnesses` table)
        5. `datumService.handleDatum(aggregatedTxList, txMap)`
            1. `saveAll` (`datum` table)
        6. `handleTxs` (successTxs, failedTxs, txMap, datumMap)
            1. `handleExtraKeyWitnesses` save List extraKeyWitnesses (`extra_key_witness` table)
            2. `handleTxContents`
                1. `handleMultiAssetMint`(successTxs, txMap)
                    1. save list multiAssets (`multi_asset` table)
                    2. save list maTxMints (`ma_tx_mint` table)
                2. `handleStakeAddressesFromTxs`: Handle stake address and its first appeared tx
                    1. save list stakeAddress
                3. `handle tx out for success txs` : 
                    1. `prepareTxOuts`
                    2. save to `ma_tx_out` table
                4.  `handle collateral out as tx out for failed txs`
                    1. `prepareTxOuts`
                    2. save to `ma_tx_out` table
                5. `handleRedeemers`
                    1. save redeemerData (`redeemer_data` table)
                    2. save redeemer (`redeemer` table)
                6. `handleTxIns` save list txIn (`tx_in` table)
                7. `handleAuxiliaryDataMaps` save list txMetadata (`tx_metadata` table)
                8. `handleParamProposals`  save list paramProposals (`param_proposal` table)
                9. `handleReferenceInputs` save list (`reference_tx_in` table)
                10. `handleCertificates`
                    1. `batchCertificateDataService.saveAllAndClearBatchData()`
                        1. save list (`pool_hash` table)
                        2. save list (`pool_update` table)
                        3. save list (`pool_metadata_ref` table)
                        4. save list (`pool_owner` table)
                        5. save list (`pool_relay` table)
                        6. save list (`pool_retire` table)
                        7. save list (`delegation` table)
                        8. save list (`stake_deregistration` table)
                        9. save list (`stake_registration`table)
                        10. save list (`treasury` table)
                        11. save list (`reserve` table)
                        12. save list (`pot_transfer`table)
                11. `handleWithdrawal` save list (`withdrawal` table)
                12. `handleUnconsumeTxIn` save list (`unconsume_tx_in` table)
                13. `handleFailedTxOuts` save list (`failed_tx_out` table)
                14. `handleAddressBalance`
                    1. save list (`stake_address` table)
                    2. save list (`address`table)
                    3. `handleAddressToken` 
                        1. save list (`address_token` table)
                        2. save list (`multi_asset`table)
                        3. save list (`address_token_balance` table)
                    4. `handleAddressTxBalance`  save list (`address_tx_balance` table)
    8. `handleEpoch` save List (`epoch` table)
    9. `handleEpochParams` 
        1. Handle epoch param for `epochNo`. 
        2. save (`epoch_param` table)
    10. `handleTxChart` save list (`tx_chart` table)
    11. `Finally, commit and clear the aggregated data`
        1. `aggregatedBatchBlockData.clear();`
        2. `aggregatedDataCachingService.commit();`