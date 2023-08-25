package org.cardanofoundation.ledgersync.explorerconsumer.unit.service.impl.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.cardanofoundation.explorer.consumercommon.entity.Block;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.AddressTokenRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.AddressTxBalanceRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.BlockRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.DatumRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.DelegationRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.EpochParamRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.ExtraKeyWitnessRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.FailedTxOutRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.MaTxMintRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.MultiAssetTxOutRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.ParamProposalRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.PoolMetadataRefRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.PoolOwnerRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.PoolRelayRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.PoolRetireRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.PoolUpdateRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.PotTransferRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.RedeemerDataRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.RedeemerRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.ReferenceInputRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.ReserveRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.RollbackHistoryRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.ScriptRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.StakeDeregistrationRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.StakeRegistrationRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TreasuryRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxInRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxMetadataRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxOutRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.UnconsumeTxInRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.WithdrawalRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.AddressBalanceService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.AggregatedDataCachingService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.EpochService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.MultiAssetService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.TxChartService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.block.RollbackServiceImpl;
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
  AddressTokenRepository addressTokenRepository;

  @Mock
  AddressTxBalanceRepository addressTxBalanceRepository;

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
  AddressBalanceService addressBalanceService;

  @Mock
  MultiAssetService multiAssetService;

  @Mock
  TxChartService txChartService;

  @Mock
  AggregatedDataCachingService aggregatedDataCachingService;

  private static final long ROLLBACK_BLOCK_NO = 123456;

  RollbackServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new RollbackServiceImpl(
        blockRepository, txRepository, addressTokenRepository, addressTxBalanceRepository,
        datumRepository, delegationRepository, extraKeyWitnessRepository, failedTxOutRepository,
        maTxMintRepository, multiAssetTxOutRepository, epochParamRepository,
        paramProposalRepository,
        poolMetadataRefRepository, poolOwnerRepository, poolRelayRepository, poolRetireRepository,
        poolUpdateRepository, potTransferRepository, redeemerRepository, redeemerDataRepository,
        referenceInputRepository, reserveRepository, scriptRepository,
        stakeDeregistrationRepository,
        stakeRegistrationRepository, treasuryRepository, txInRepository, txMetadataRepository,
        txOutRepository, unconsumeTxInRepository, withdrawalRepository, rollbackHistoryRepository,
        epochService, addressBalanceService, multiAssetService, txChartService,
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
    Mockito.verifyNoInteractions(addressTokenRepository);
    Mockito.verifyNoInteractions(addressTxBalanceRepository);
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
    Mockito.verifyNoInteractions(addressBalanceService);
    Mockito.verifyNoInteractions(multiAssetService);
    Mockito.verifyNoInteractions(txChartService);
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
    Mockito.verifyNoInteractions(addressTokenRepository);
    Mockito.verifyNoInteractions(addressTxBalanceRepository);
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
    Mockito.verifyNoInteractions(addressBalanceService);
    Mockito.verifyNoInteractions(multiAssetService);
    Mockito.verifyNoInteractions(txChartService);
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
    Mockito.verify(addressTokenRepository, Mockito.times(1))
        .deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(addressTokenRepository);
    Mockito.verify(addressTxBalanceRepository, Mockito.times(1))
        .deleteAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(addressTxBalanceRepository);
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
    Mockito.verify(addressBalanceService, Mockito.times(1))
        .rollbackAddressBalances(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(addressBalanceService);
    Mockito.verify(multiAssetService, Mockito.times(1))
        .rollbackMultiAssets(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(multiAssetService);
    Mockito.verify(txChartService, Mockito.times(1))
        .rollbackTxChart(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txChartService);
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .subtractTxCount(Mockito.anyInt());
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .subtractBlockCount(Mockito.anyInt());
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .saveLatestTxs();
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1)).commit();
    Mockito.verifyNoMoreInteractions(aggregatedDataCachingService);
  }
}
