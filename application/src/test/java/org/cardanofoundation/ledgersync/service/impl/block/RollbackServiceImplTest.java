package org.cardanofoundation.ledgersync.service.impl.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.cardanofoundation.ledgersync.consumercommon.entity.Block;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.repository.*;
import org.cardanofoundation.ledgersync.service.AggregatedDataCachingService;
import org.cardanofoundation.ledgersync.service.EpochService;
import org.cardanofoundation.ledgersync.service.MultiAssetService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class RollbackServiceImplTest {

  @Mock
  BlockRepository blockRepository;

  @Mock
  TxRepository txRepository;

  @Mock
  DatumRepository datumRepository;

  @Mock
  DelegationRepository delegationRepository;

  @Mock
  ExtraKeyWitnessRepository extraKeyWitnessRepository;

  @Mock
  FailedTxOutRepository failedTxOutRepository;

  @Mock
  MaTxMintRepository maTxMintRepository;

  @Mock
  MultiAssetTxOutRepository multiAssetTxOutRepository;

  @Mock
  EpochParamRepository epochParamRepository;

  @Mock
  ParamProposalRepository paramProposalRepository;

  @Mock
  PoolMetadataRefRepository poolMetadataRefRepository;

  @Mock
  PoolOwnerRepository poolOwnerRepository;

  @Mock
  PoolRelayRepository poolRelayRepository;

  @Mock
  PoolRetireRepository poolRetireRepository;

  @Mock
  PoolUpdateRepository poolUpdateRepository;

  @Mock
  PotTransferRepository potTransferRepository;

  @Mock
  RedeemerRepository redeemerRepository;

  @Mock
  RedeemerDataRepository redeemerDataRepository;

  @Mock
  ReferenceInputRepository referenceInputRepository;

  @Mock
  ReserveRepository reserveRepository;

  @Mock
  ScriptRepository scriptRepository;

  @Mock
  StakeDeregistrationRepository stakeDeregistrationRepository;

  @Mock
  StakeRegistrationRepository stakeRegistrationRepository;

  @Mock
  TreasuryRepository treasuryRepository;

  @Mock
  TxInRepository txInRepository;

  @Mock
  TxMetadataRepository txMetadataRepository;

  @Mock
  TxOutRepository txOutRepository;

  @Mock
  UnconsumeTxInRepository unconsumeTxInRepository;

  @Mock
  WithdrawalRepository withdrawalRepository;

  @Mock
  RollbackHistoryRepository rollbackHistoryRepository;

  @Mock
  EpochService epochService;

  @Mock
  MultiAssetService multiAssetService;

  @Mock
  AggregatedDataCachingService aggregatedDataCachingService;

  @Mock
  TxBootstrapWitnessRepository txBootstrapWitnessRepository;

  @Mock
  TxWitnessRepository txWitnessRepository;

  private static final long ROLLBACK_BLOCK_NO = 123456;

  RollbackServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new RollbackServiceImpl(
        blockRepository, txRepository,
        datumRepository, delegationRepository, extraKeyWitnessRepository, failedTxOutRepository,
        maTxMintRepository, multiAssetTxOutRepository, epochParamRepository,
        paramProposalRepository,
        poolMetadataRefRepository, poolOwnerRepository, poolRelayRepository, poolRetireRepository,
        poolUpdateRepository, potTransferRepository, redeemerRepository, redeemerDataRepository,
        referenceInputRepository, reserveRepository, scriptRepository,
        stakeDeregistrationRepository,
        stakeRegistrationRepository, treasuryRepository, txInRepository, txMetadataRepository,
        txOutRepository, unconsumeTxInRepository, withdrawalRepository, rollbackHistoryRepository,
        txBootstrapWitnessRepository, txWitnessRepository,
        epochService, multiAssetService,
        aggregatedDataCachingService
    );
  }

  @Test
  @DisplayName("Should skip rollback if target rollback block not found")
  void shouldSkipRollbackIfBlockNotFoundTest() { // NOSONAR
    Optional<Block> block = Optional.empty();

    Mockito.when(blockRepository.findBlockByBlockNo(ROLLBACK_BLOCK_NO)).thenReturn(block);

    victim.rollBackFrom(ROLLBACK_BLOCK_NO);

    Mockito.verify(blockRepository, Mockito.times(1)).findBlockByBlockNo(Mockito.anyLong());
    Mockito.verifyNoMoreInteractions(blockRepository);
    Mockito.verifyNoInteractions(txRepository);
    Mockito.verifyNoInteractions(datumRepository);
    Mockito.verifyNoInteractions(delegationRepository);
    Mockito.verifyNoInteractions(extraKeyWitnessRepository);
    Mockito.verifyNoInteractions(failedTxOutRepository);
    Mockito.verifyNoInteractions(maTxMintRepository);
    Mockito.verifyNoInteractions(multiAssetTxOutRepository);
    Mockito.verifyNoInteractions(epochParamRepository);
    Mockito.verifyNoInteractions(paramProposalRepository);
    Mockito.verifyNoInteractions(poolMetadataRefRepository);
    Mockito.verifyNoInteractions(poolOwnerRepository);
    Mockito.verifyNoInteractions(poolRelayRepository);
    Mockito.verifyNoInteractions(poolRetireRepository);
    Mockito.verifyNoInteractions(poolUpdateRepository);
    Mockito.verifyNoInteractions(potTransferRepository);
    Mockito.verifyNoInteractions(redeemerRepository);
    Mockito.verifyNoInteractions(redeemerDataRepository);
    Mockito.verifyNoInteractions(referenceInputRepository);
    Mockito.verifyNoInteractions(reserveRepository);
    Mockito.verifyNoInteractions(scriptRepository);
    Mockito.verifyNoInteractions(stakeDeregistrationRepository);
    Mockito.verifyNoInteractions(stakeRegistrationRepository);
    Mockito.verifyNoInteractions(treasuryRepository);
    Mockito.verifyNoInteractions(txInRepository);
    Mockito.verifyNoInteractions(txMetadataRepository);
    Mockito.verifyNoInteractions(txOutRepository);
    Mockito.verifyNoInteractions(unconsumeTxInRepository);
    Mockito.verifyNoInteractions(withdrawalRepository);
    Mockito.verifyNoInteractions(rollbackHistoryRepository);
    Mockito.verifyNoInteractions(epochService);
    Mockito.verifyNoInteractions(multiAssetService);
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .saveLatestTxs();
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1)).commit();
    Mockito.verifyNoMoreInteractions(aggregatedDataCachingService);
  }

  @Test
  @DisplayName("Rollback empty block")
  void rollbackEmptyBlockTest() { // NOSONAR
    Optional<Block> block = Optional.of(Mockito.mock(Block.class));

    Mockito.when(blockRepository.findBlockByBlockNo(ROLLBACK_BLOCK_NO)).thenReturn(block);
    Mockito.when(blockRepository.findAllByBlockNoGreaterThanOrderByBlockNoDesc(Mockito.anyLong()))
        .thenReturn(new ArrayList<>());
    Mockito.when(txRepository.findAllByBlockIn(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());

    victim.rollBackFrom(ROLLBACK_BLOCK_NO);

    Mockito.verify(blockRepository, Mockito.times(1)).findBlockByBlockNo(Mockito.anyLong());
    Mockito.verify(blockRepository, Mockito.times(1))
        .findAllByBlockNoGreaterThanOrderByBlockNoDesc(Mockito.anyLong());
    Mockito.verify(blockRepository, Mockito.times(1)).deleteAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(blockRepository);
    Mockito.verify(txRepository, Mockito.times(1)).findAllByBlockIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txRepository);
    Mockito.verifyNoInteractions(datumRepository);
    Mockito.verifyNoInteractions(delegationRepository);
    Mockito.verifyNoInteractions(extraKeyWitnessRepository);
    Mockito.verifyNoInteractions(failedTxOutRepository);
    Mockito.verifyNoInteractions(maTxMintRepository);
    Mockito.verifyNoInteractions(multiAssetTxOutRepository);
    Mockito.verifyNoInteractions(epochParamRepository);
    Mockito.verifyNoInteractions(paramProposalRepository);
    Mockito.verifyNoInteractions(poolMetadataRefRepository);
    Mockito.verifyNoInteractions(poolOwnerRepository);
    Mockito.verifyNoInteractions(poolRelayRepository);
    Mockito.verifyNoInteractions(poolRetireRepository);
    Mockito.verifyNoInteractions(poolUpdateRepository);
    Mockito.verifyNoInteractions(potTransferRepository);
    Mockito.verifyNoInteractions(redeemerRepository);
    Mockito.verifyNoInteractions(redeemerDataRepository);
    Mockito.verifyNoInteractions(referenceInputRepository);
    Mockito.verifyNoInteractions(reserveRepository);
    Mockito.verifyNoInteractions(scriptRepository);
    Mockito.verifyNoInteractions(stakeDeregistrationRepository);
    Mockito.verifyNoInteractions(stakeRegistrationRepository);
    Mockito.verifyNoInteractions(treasuryRepository);
    Mockito.verifyNoInteractions(txInRepository);
    Mockito.verifyNoInteractions(txMetadataRepository);
    Mockito.verifyNoInteractions(txOutRepository);
    Mockito.verifyNoInteractions(unconsumeTxInRepository);
    Mockito.verifyNoInteractions(withdrawalRepository);
    Mockito.verify(rollbackHistoryRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(rollbackHistoryRepository);
    Mockito.verify(epochService, Mockito.times(1)).rollbackEpochStats(Mockito.anyList());
    Mockito.verifyNoMoreInteractions(epochService);
    Mockito.verifyNoInteractions(multiAssetService);
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .subtractBlockCount(Mockito.anyInt());
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .saveLatestTxs();
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1)).commit();
    Mockito.verifyNoMoreInteractions(aggregatedDataCachingService);
  }

  @Test
  @DisplayName("Rollback block with tx")
  void rollbackBlockWithTxTest() { // NOSONAR
    Optional<Block> block = Optional.of(Mockito.mock(Block.class));

    Mockito.when(blockRepository.findBlockByBlockNo(ROLLBACK_BLOCK_NO)).thenReturn(block);
    Mockito.when(blockRepository.findAllByBlockNoGreaterThanOrderByBlockNoDesc(Mockito.anyLong()))
        .thenReturn(new ArrayList<>());
    Mockito.when(txRepository.findAllByBlockIn(Mockito.anyCollection()))
        .thenReturn(List.of(Mockito.mock(Tx.class)));

    victim.rollBackFrom(ROLLBACK_BLOCK_NO);

    Mockito.verify(blockRepository, Mockito.times(1)).findBlockByBlockNo(Mockito.anyLong());
    Mockito.verify(blockRepository, Mockito.times(1))
        .findAllByBlockNoGreaterThanOrderByBlockNoDesc(Mockito.anyLong());
    Mockito.verify(blockRepository, Mockito.times(1)).deleteAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(blockRepository);
    Mockito.verify(txRepository, Mockito.times(1)).findAllByBlockIn(Mockito.anyCollection());
    Mockito.verify(txRepository, Mockito.times(1)).deleteAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txRepository);
    Mockito.verify(datumRepository, Mockito.times(1)).deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(datumRepository);
    Mockito.verify(delegationRepository, Mockito.times(1)).deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(delegationRepository);
    Mockito.verify(extraKeyWitnessRepository, Mockito.times(1))
        .deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(extraKeyWitnessRepository);
    Mockito.verify(failedTxOutRepository, Mockito.times(1))
        .deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(failedTxOutRepository);
    Mockito.verify(maTxMintRepository, Mockito.times(1)).deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(maTxMintRepository);
    Mockito.verify(multiAssetTxOutRepository, Mockito.times(1))
        .deleteAllByTxOutTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(multiAssetTxOutRepository);
    Mockito.verify(epochParamRepository, Mockito.times(1))
        .deleteAllByBlockIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(epochParamRepository);
    Mockito.verify(paramProposalRepository, Mockito.times(1))
        .deleteAllByRegisteredTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(paramProposalRepository);
    Mockito.verify(poolMetadataRefRepository, Mockito.times(1))
        .deleteAllByRegisteredTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(poolMetadataRefRepository);
    Mockito.verify(poolOwnerRepository, Mockito.times(1))
        .deleteAllByPoolUpdateRegisteredTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(poolOwnerRepository);
    Mockito.verify(poolRelayRepository, Mockito.times(1))
        .deleteAllByPoolUpdateRegisteredTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(poolRelayRepository);
    Mockito.verify(poolRetireRepository, Mockito.times(1))
        .deleteAllByAnnouncedTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(poolRetireRepository);
    Mockito.verify(poolUpdateRepository, Mockito.times(1))
        .deleteAllByRegisteredTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(poolUpdateRepository);
    Mockito.verify(potTransferRepository, Mockito.times(1))
        .deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(potTransferRepository);
    Mockito.verify(redeemerRepository, Mockito.times(1)).deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(redeemerRepository);
    Mockito.verify(redeemerDataRepository, Mockito.times(1))
        .deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(redeemerDataRepository);
    Mockito.verify(referenceInputRepository, Mockito.times(1))
        .deleteAllByTxInIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(referenceInputRepository);
    Mockito.verify(reserveRepository, Mockito.times(1)).deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(reserveRepository);
    Mockito.verify(scriptRepository, Mockito.times(1)).deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(scriptRepository);
    Mockito.verify(stakeDeregistrationRepository, Mockito.times(1))
        .deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(stakeDeregistrationRepository);
    Mockito.verify(stakeRegistrationRepository, Mockito.times(1))
        .deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(stakeRegistrationRepository);
    Mockito.verify(treasuryRepository, Mockito.times(1)).deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(treasuryRepository);
    Mockito.verify(txInRepository, Mockito.times(1)).deleteAllByTxInputIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txInRepository);
    Mockito.verify(txMetadataRepository, Mockito.times(1)).deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txMetadataRepository);
    Mockito.verify(txOutRepository, Mockito.times(1)).deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txOutRepository);
    Mockito.verify(unconsumeTxInRepository, Mockito.times(1))
        .deleteAllByTxInIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(unconsumeTxInRepository);
    Mockito.verify(withdrawalRepository, Mockito.times(1)).deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(withdrawalRepository);
    Mockito.verify(rollbackHistoryRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(rollbackHistoryRepository);
    Mockito.verify(epochService, Mockito.times(1)).rollbackEpochStats(Mockito.anyList());
    Mockito.verifyNoMoreInteractions(epochService);
    Mockito.verify(multiAssetService, Mockito.times(1))
        .rollbackMultiAssets(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(multiAssetService);
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .subtractTxCount(Mockito.anyInt());
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .subtractBlockCount(Mockito.anyInt());
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .saveLatestTxs();
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1)).commit();
    Mockito.verify(txBootstrapWitnessRepository, Mockito.times(1))
            .deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verify(txWitnessRepository, Mockito.times(1))
            .deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(aggregatedDataCachingService);
  }
}
