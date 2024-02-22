# Yaci-store source code (single thread)

1. `StartService.start`
    1. `publishEvent`(genesisBlockEvent)
        - Listener process
            1. `[ByronBlockProcessor]->handleGenesisBlockEvent` save (`block` table)
            2. `[BlockFetchService]->handleGenesisBlockEvent`  save (`cursor_` table)
            3. `[GenesisTransactionProcessor]->processGenesisUtxos`saveAll (`transaction`table)
            4. `[GenesisUtxoProcessor]->processGenesisUtxos` saveAll(`address_utxo` table)
    2. `Reset cursor`
    3. `Send a rollback event`
    4. `blockFetchService.startFetch(from, to)`
        - Listener process
            1. `onByronEbBlock`
                1. `publisher.publishEvent`(ByronEbBlockEvent)
                    - Listener process
                        1. `[ByronBlockProcessor]->handleByronEbBlockEvent` save (`block` table)
                2. `Set the cursor` save (`cursor_` table)
            2. `onByronBlock`
                1. If `newEpoch`
                    1. `publisher.publishEvent(epochChangeEvent)`
                        - Listener process
                            1. `handleEpochChangeEvent`
                                1. `ppEraChangeRules.apply(newEra, prevEra, protocolParams):`Additional Era change specific rules
                                2. `Save cost model if required` save (`cost_model` table)
                                3. Save (`epoch_param` table)
                2. `byronBlockEventPublisher.publishBlockEvents`
                    1. `publisher.publishEvent`(byronMainBlockEvent)
                        - Listener process
                            1. `[ByronBlockProcessor]->handleByronMainBlockEvent` save (`block` table)
                            2. `[DBIndexService]->handleFirstBlockEvent` runDeleteIndexes if first
                            3. `[ByronTransactionProcessor]->handleByronTransactionEvent`  saveAll (`transaction` table)
                            4. `[ByronTransactionProcessor]->handleByronTransactionWitnesses` saveAll (`transaction_witness` table)
                            5. `[ByronUtxoProcessor]->handleByronTransactionEvent`  
                                1. saveAll (`address_utxo`table)
                                2. saveAll (`tx_input` table)
                                3. `publisher.publishEvent`(AddressUtxoEvent)
                    2. `publisher.publishEvent`(ReadyForBalanceAggregationEvent)
                    3. `publisher.publishEvent`(CommitEvent)
                        - Listener process
                            1. `[DatumStorageImpl]->handleCommit` 
                                1. `saveDatumsInPartition` save list (`datum`table)
                            2. `[ScriptStorageImpl]->handleCommit` 
                                1. `saveScriptsInPartition`save list (`script`table)
                            3. `[UtxoStorageImpl]->handleCommit` 
                                1. `utxoCache.clear()` clear cache
                    4. `Set the cursor` save (`cursor_` table)
            3. `onBlock`
                1. `byronBlockEventPublisher.processBlocksInParallel()`
                2. If `newEra` || `newEpoch`
                    1. `publisher.publishEvent(epochChangeEvent)`
                        - Listener process
                            1. `handleEpochChangeEvent`
                                1. `ppEraChangeRules.apply(newEra, prevEra, protocolParams):`Additional Era change specific rules
                                2. `Save cost model if required` save (`cost_model` table)
                                3. Save (`epoch_param` table)
                3. `postShelleyBlockEventPublisher.publishBlockEvents`
                    1. `processBlockSingleThread`
                        1. `publisher.publishEvent`(Era) No listener
                        2. `publisher.publishEvent`(BlockEvent) 
                            - Listener process
                                1. `handleBlockEvent`
                                    1. `handleTransaction` (Block, List<TransactionBody>): 
                                        1. setTotalOutput and setTotalFees to block object
                                    2. save (`block` table)
                        3. `publisher.publishEvent`(BlockHeaderEvent)
                            - Listener process
                                1. `handleFirstBlockEventToCreateIndex`
                                    1. If `blockHeaderEvent.getMetadata().getBlock() < 50000`  then `reApplyIndexes`
                        4. `publisher.publishEvent`(TransactionEvent)
                            - Listener process
                                1. `[UtxoProcessor]`->`handleTransactionEvent`
                                    1. `saveSpent` save list (`tx_input` table)
                                    2. `saveUnspent` save list (`address_utxo` table)
                                    3. `publisher.publishEvent`(AddressUtxoEvent)
                                2. `[TransactionProcessor]`->`handleTransactionEvent` saveAll (`transaction` table)
                                3. `[OutputDatumProcessor]`->`handleOutputDatumInTransaction`
                                    1. `handleOutputDatums`
                                        1. saveAll  to `datumCache`
                                4. `[ScriptRedeemerDatumProcessor]`->`handleScriptTransactionEvent`
                                    1. `handleScriptTransaction`
                                        1. saveAll  to `datumCache`
                                        2. saveAll to `scriptCache`
                                        3. saveAll (`transaction_scripts` table)
                                5. `[TransactionProcessor]`->`handleTransactionWitnesses`
                                    1. `handleScriptTransaction`
                        5. `publisher.publishEvent`(ScriptEvent)
                            - Listener process
                                
                                `handleScriptEvent` 
                                
                        6. `publisher.publishEvent`(AuxDataEvent)
                            - Listener process
                                1. `[MetadataProcessor]->handleAuxDataEvent` saveAll (`transaction_metadata` table)
                                2. `publisher.publishEvent`(TxMetadataEvent)
                        7. `publisher.publishEvent`(CertificateEvent)
                            - Listener process
                                1. `[CommitteeRegistrationProcessor]->handleCommitteeRegistration` 
                                    1. saveAll (`committee_registration` table)
                                    2. saveAll (`committee_deregistration`table)
                                2. `[DelegationVoteProcessor]->handleDelegationVote`
                                    1. saveAll (`delegation_vote`table)
                                3. `[DRepRegistrationProcessor]->handleDRepRegistration`
                                    1. saveAll (`drep_registration` table)
                                4. `[MIRProcessor]->handleMIR`
                                    1. save (`mir`table)
                                5. `[StakeRegProcessor]->processStakeRegistration`
                                    1. `saveRegistrations`  saveAll (`stake_registration` table)
                                    2. `saveDelegations` saveAll (`delegation`table)
                        8. `publisher.publishEvent`(MintBurnEvent)
                            - Listener process
                                1. `[AssetMintBurnProcessor]->handleAssetMintBurn`
                                    1. saveAll  (`assets` table)
                                    2. `publisher.publishEvent` (TxAssetEvent)
                        9. `publisher.publishEvent`(UpdateEvent)
                            - Listener process
                                1. `[ProtocolParamsUpdateProcessor]->handleUpdateEvent`
                                    1. saveAll  (`protocol_params_proposal`table)
                        10. `publisher.publishEvent`(GovernanceEvent)
                            - Listener process
                                1. `[VotingProcedureProcessor]->handleVotingProcedure`
                                    1. saveAll  (`voting_procedure` table)
                                2. `[GovActionProposalProcessor]->handleGovernanceAction`
                                    1. saveAll  (`gov_action_proposal`table)
                    2. `publisher.publishEvent`(ReadyForBalanceAggregationEvent)
                    3. `publisher.publishEvent`(CommitEvent)
                        - Listener process
                            1. `[DatumStorageImpl]->handleCommit` 
                                1. `saveDatumsInPartition` save list (`datum`table)
                            2. `[ScriptStorageImpl]->handleCommit` 
                                1. `saveScriptsInPartition`save list (`script`table)
                            3. `[UtxoStorageImpl]->handleCommit` 
                                1. `utxoCache.clear()` clear cache
                    4. `Set the cursor` save (`cursor_` table)