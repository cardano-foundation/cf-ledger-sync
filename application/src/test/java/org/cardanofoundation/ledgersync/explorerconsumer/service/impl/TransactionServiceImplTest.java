package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.bloxbean.cardano.yaci.core.model.certs.Certificate;
import com.bloxbean.cardano.yaci.core.model.certs.StakeCredType;
import org.cardanofoundation.explorer.consumercommon.entity.Block;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTxIn;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTxOut;
import org.cardanofoundation.ledgersync.explorerconsumer.dto.EUTXOWrapper;
import org.cardanofoundation.ledgersync.explorerconsumer.factory.CertificateSyncServiceFactory;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.ExtraKeyWitnessRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.cardanofoundation.ledgersync.explorerconsumer.util.CertificateUtil.buildStakeDelegationCert;
import static org.cardanofoundation.ledgersync.explorerconsumer.util.CertificateUtil.buildStakeRegistrationCert;
import static org.cardanofoundation.ledgersync.explorerconsumer.util.TestStringUtils.generateRandomHash;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {
  private static final int BLOCK_HASH_LENGTH = 64;
  private static final int TX_HASH_LENGTH = 64;
  private static final int STAKE_KEY_HASH_LENGTH = 56;
  private static final int REQUIRED_SIGNER_HASH_LENGTH = 56;

  @Mock
  TxRepository txRepository;

  @Mock
  ExtraKeyWitnessRepository extraKeyWitnessRepository;

  @Mock
  MultiAssetService multiAssetService;

  @Mock
  StakeAddressService stakeAddressService;

  @Mock
  ParamProposalService paramProposalService;

  @Mock
  AddressBalanceService addressBalanceService;

  @Mock
  WithdrawalsService withdrawalsService;

  @Mock
  TxMetaDataService txMetaDataService;

  @Mock
  RedeemerService redeemerService;

  @Mock
  ScriptService scriptService;

  @Mock
  DatumService datumService;

  @Mock
  BlockDataService blockDataService;

  @Mock
  TxOutService txOutService;

  @Mock
  TxInService txInService;

  @Mock
  ReferenceInputService referenceInputService;

  @Mock
  AggregatedDataCachingService aggregatedDataCachingService;

  @Mock
  CertificateSyncServiceFactory certificateSyncServiceFactory;

  @Mock
  BatchCertificateDataService batchCertificateDataService;

  @Mock
  TxMetaDataHashService txMetaDataHashService;

  @Mock
  TxWitnessService txWitnessService;

  @Mock
  TxBootstrapWitnessService txBootstrapWitnessService;

  TransactionServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new TransactionServiceImpl(
        txRepository, extraKeyWitnessRepository, multiAssetService, stakeAddressService,
        paramProposalService, addressBalanceService, withdrawalsService, txMetaDataService,
        redeemerService, scriptService, datumService, blockDataService, txOutService, txInService,
        referenceInputService, aggregatedDataCachingService,
        certificateSyncServiceFactory, batchCertificateDataService,
        txMetaDataHashService, txWitnessService, txBootstrapWitnessService
    );
  }

  @Test
  @DisplayName("Should skip tx handling if no txs were supplied")
  void shouldSkipTxHandlingIfNoTxsSuppliedTest() {
    Map<String, Block> blockMap = Collections.emptyMap();
    Collection<AggregatedBlock> aggregatedBlocks = Collections.emptyList();

    victim.prepareAndHandleTxs(blockMap, aggregatedBlocks);

    Mockito.verifyNoInteractions(txRepository);
    Mockito.verifyNoInteractions(extraKeyWitnessRepository);
    Mockito.verifyNoInteractions(multiAssetService);
    Mockito.verifyNoInteractions(stakeAddressService);
    Mockito.verifyNoInteractions(paramProposalService);
    Mockito.verifyNoInteractions(addressBalanceService);
    Mockito.verifyNoInteractions(withdrawalsService);
    Mockito.verifyNoInteractions(txMetaDataService);
    Mockito.verifyNoInteractions(redeemerService);
    Mockito.verifyNoInteractions(scriptService);
    Mockito.verifyNoInteractions(datumService);
    Mockito.verifyNoInteractions(blockDataService);
    Mockito.verifyNoInteractions(txOutService);
    Mockito.verifyNoInteractions(txInService);
    Mockito.verifyNoInteractions(referenceInputService);
    Mockito.verifyNoInteractions(aggregatedDataCachingService);
    Mockito.verifyNoInteractions(certificateSyncServiceFactory);
    Mockito.verifyNoInteractions(batchCertificateDataService);
  }

  @Test
  @DisplayName("Should handle txs successfully")
  void shouldHandleTxsSuccessfullyTest() {
    Map<String, Block> blockMap = new LinkedHashMap<>();
    Collection<AggregatedBlock> aggregatedBlocks = new ArrayList<>();

    IntStream.range(0, 10).forEach(idx -> {
      String blockHash = generateRandomHash(BLOCK_HASH_LENGTH);
      blockMap.put(blockHash, givenBlockEntity(blockHash, idx));

      AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);
      List<AggregatedTx> aggregatedTxList = IntStream.range(0, 10)
          .boxed()
          .map(blockIdx -> givenAggregatedTxWithSufficientData(blockHash, blockIdx))
          .toList();
      Mockito.when(aggregatedBlock.getTxList()).thenReturn(aggregatedTxList);
      aggregatedBlocks.add(aggregatedBlock);
    });

    Mockito.when(txOutService.prepareTxOuts(Mockito.anyMap(), Mockito.anyMap(), Mockito.anyMap(),
            Mockito.anyMap()))
        .thenReturn(new EUTXOWrapper(new ArrayList<>(), new ArrayList<>()));

    victim.prepareAndHandleTxs(blockMap, aggregatedBlocks);

    Mockito.verify(txRepository, Mockito.times(1))
        .saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txRepository);
    Mockito.verify(extraKeyWitnessRepository, Mockito.times(1))
        .findByHashIn(Mockito.anyCollection());
    Mockito.verify(extraKeyWitnessRepository, Mockito.times(1))
        .saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(extraKeyWitnessRepository);
    Mockito.verify(multiAssetService, Mockito.times(1))
        .handleMultiAssetMint(Mockito.anyCollection(), Mockito.anyMap());
    Mockito.verifyNoMoreInteractions(multiAssetService);
    Mockito.verify(stakeAddressService, Mockito.times(1))
        .handleStakeAddressesFromTxs(Mockito.anyMap(), Mockito.anyMap());
    Mockito.verifyNoMoreInteractions(stakeAddressService);
    Mockito.verify(paramProposalService, Mockito.times(1))
        .handleParamProposals(Mockito.anyCollection(), Mockito.anyMap());
    Mockito.verifyNoMoreInteractions(paramProposalService);
    Mockito.verify(addressBalanceService, Mockito.times(1))
        .handleAddressBalance(Mockito.anyMap(), Mockito.anyMap(), Mockito.anyMap());
    Mockito.verifyNoMoreInteractions(addressBalanceService);
    Mockito.verify(withdrawalsService, Mockito.times(1))
        .handleWithdrawal(
            Mockito.anyCollection(), Mockito.anyMap(),
            Mockito.anyMap(), Mockito.anyMap());
    Mockito.verifyNoMoreInteractions(withdrawalsService);
    Mockito.verify(txMetaDataService, Mockito.times(1))
        .handleAuxiliaryDataMaps(Mockito.anyMap());
    Mockito.verifyNoMoreInteractions(txMetaDataService);
    Mockito.verify(redeemerService, Mockito.times(2))
        .handleRedeemers(Mockito.anyCollection(), Mockito.anyMap(), Mockito.anyMap());
    Mockito.verifyNoMoreInteractions(redeemerService);
    Mockito.verify(scriptService, Mockito.times(1))
        .handleScripts(Mockito.anyCollection(), Mockito.anyMap());
    Mockito.verifyNoMoreInteractions(scriptService);
    Mockito.verify(datumService, Mockito.times(1))
        .handleDatum(Mockito.anyCollection(), Mockito.anyMap());
    Mockito.verifyNoMoreInteractions(datumService);
    Mockito.verify(blockDataService, Mockito.times(1))
        .getStakeAddressTxHashMap();
    Mockito.verify(blockDataService, Mockito.times(1))
        .getAggregatedAddressBalanceMap();
    Mockito.verify(blockDataService, Mockito.times(100))
        .getAggregatedBlock(Mockito.anyString());
    Mockito.verifyNoMoreInteractions(blockDataService);
    Mockito.verify(txOutService, Mockito.times(2))
        .prepareTxOuts(Mockito.anyMap(), Mockito.anyMap(), Mockito.anyMap(), Mockito.anyMap());
    Mockito.verify(txOutService, Mockito.times(1))
        .handleFailedTxOuts(
            Mockito.anyCollection(), Mockito.anyCollection(),
            Mockito.anyMap(), Mockito.anyMap(), Mockito.anyMap());
    Mockito.verifyNoMoreInteractions(txOutService);
    Mockito.verify(txInService, Mockito.times(2))
        .handleTxIns(Mockito.anyCollection(), Mockito.anyMap(),
            Mockito.anyMap(), Mockito.anyMap(), Mockito.anyMap(), Mockito.anyMap());
    Mockito.verify(txInService, Mockito.times(2))
        .handleUnconsumeTxIn(Mockito.anyMap(), Mockito.anyMap(), Mockito.anyMap(), Mockito.anyMap());
    Mockito.verifyNoMoreInteractions(txInService);
    Mockito.verify(referenceInputService, Mockito.times(1))
        .handleReferenceInputs(Mockito.anyMap(), Mockito.anyMap(), Mockito.anyMap());
    Mockito.verifyNoMoreInteractions(referenceInputService);
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .addTxCount(Mockito.anyInt());
    Mockito.verifyNoMoreInteractions(aggregatedDataCachingService);
    Mockito.verify(certificateSyncServiceFactory, Mockito.times(100))
        .handle(Mockito.any(), Mockito.any(), Mockito.anyInt(),
            Mockito.any(), Mockito.any(), Mockito.anyMap());
    Mockito.verifyNoMoreInteractions(certificateSyncServiceFactory);
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .saveAllAndClearBatchData();
    Mockito.verifyNoMoreInteractions(batchCertificateDataService);

    AggregatedTx aggregatedTxToTest = aggregatedBlocks.stream().toList().get(0).getTxList().get(0);
    Mockito.verify(aggregatedTxToTest, Mockito.times(3)).getBlockHash();
    Mockito.verify(aggregatedTxToTest, Mockito.times(1)).getBlockIndex();
    Mockito.verify(aggregatedTxToTest, Mockito.times(1)).getOutSum();
    Mockito.verify(aggregatedTxToTest, Mockito.times(1)).getFee();
    Mockito.verify(aggregatedTxToTest, Mockito.times(2)).isValidContract();
    Mockito.verify(aggregatedTxToTest, Mockito.times(1)).getDeposit();
    Mockito.verify(aggregatedTxToTest, Mockito.times(5)).getHash();
    Mockito.verify(aggregatedTxToTest, Mockito.times(4)).getCertificates();
    Mockito.verify(aggregatedTxToTest, Mockito.times(2)).getRequiredSigners();
    Mockito.verify(aggregatedTxToTest, Mockito.times(1)).getDeposit();
    Mockito.verify(aggregatedTxToTest, Mockito.never()).getWitnesses();
    Mockito.verify(aggregatedTxToTest, Mockito.times(1)).getTxInputs();
    Mockito.verify(aggregatedTxToTest, Mockito.times(2)).getTxOutputs();


    AggregatedTx aggregatedTxToTest2 = aggregatedBlocks.stream().toList().get(1).getTxList().get(1);
    Mockito.verify(aggregatedTxToTest2, Mockito.times(1)).getBlockHash();
    Mockito.verify(aggregatedTxToTest2, Mockito.times(1)).getBlockIndex();
    Mockito.verify(aggregatedTxToTest2, Mockito.times(1)).getOutSum();
    Mockito.verify(aggregatedTxToTest2, Mockito.times(1)).getFee();
    Mockito.verify(aggregatedTxToTest2, Mockito.times(2)).isValidContract();
    Mockito.verify(aggregatedTxToTest2, Mockito.times(1)).getDeposit();
    Mockito.verify(aggregatedTxToTest2, Mockito.times(5)).getHash();
    Mockito.verify(aggregatedTxToTest2, Mockito.never()).getCertificates();
    Mockito.verify(aggregatedTxToTest2, Mockito.times(2)).getRequiredSigners();
    Mockito.verify(aggregatedTxToTest2, Mockito.times(1)).getDeposit();
    Mockito.verify(aggregatedTxToTest2, Mockito.never()).getWitnesses();
    Mockito.verify(aggregatedTxToTest2, Mockito.times(1)).getTxInputs();
    Mockito.verify(aggregatedTxToTest2, Mockito.never()).getTxOutputs();
    Mockito.verify(aggregatedTxToTest2, Mockito.never()).getCertificates();
  }

  private static AggregatedTx givenAggregatedTxWithSufficientData(String blockHash, int blockIdx) {
    Set<String> requiredSigners = IntStream.range(0, 10)
        .mapToObj(unused -> generateRandomHash(REQUIRED_SIGNER_HASH_LENGTH))
        .collect(Collectors.toSet());
    AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
    Mockito.when(aggregatedTx.getBlockHash()).thenReturn(blockHash);
    Mockito.when(aggregatedTx.getHash()).thenReturn(generateRandomHash(TX_HASH_LENGTH));
    Mockito.when(aggregatedTx.getBlockIndex()).thenReturn(Long.valueOf(blockIdx));
    Mockito.when(aggregatedTx.isValidContract()).thenReturn(blockIdx % 2 == 0);
    Mockito.when(aggregatedTx.getDeposit()).thenReturn(0L);
    AggregatedTxIn aggregatedTx1Mock = Mockito.mock(AggregatedTxIn.class);
    AggregatedTxOut aggregatedTxOutMock = Mockito.mock(AggregatedTxOut.class);
    Mockito.when(aggregatedTx.getTxInputs()).thenReturn(Set.of(aggregatedTx1Mock));
    Mockito.lenient().when(aggregatedTx.getTxOutputs()).thenReturn(List.of(aggregatedTxOutMock));
    Mockito.when(aggregatedTx.getCollateralInputs()).thenReturn(blockIdx % 2 == 0
            ? Collections.emptySet()
            : Set.of(aggregatedTx1Mock));
    AggregatedTxOut aggregatedTxOut = blockIdx % 2 == 0
            ? null
            : aggregatedTxOutMock;
    Mockito.lenient().when(aggregatedTx.getCollateralReturn()).thenReturn(aggregatedTxOut);
    Mockito.lenient().when(aggregatedTx.getCertificates()).thenReturn(givenCertificates());
    Mockito.when(aggregatedTx.getRequiredSigners()).thenReturn(requiredSigners);

    return aggregatedTx;
  }

  private static Block givenBlockEntity(String blockHash, long blockNo) {
    return Block.builder().hash(blockHash).blockNo(blockNo).build();
  }

  private static List<Certificate> givenCertificates() {
    return List.of(
        buildStakeRegistrationCert(StakeCredType.SCRIPTHASH,
            generateRandomHash(STAKE_KEY_HASH_LENGTH)),
        buildStakeDelegationCert(StakeCredType.SCRIPTHASH,
            generateRandomHash(STAKE_KEY_HASH_LENGTH))
    );
  }
}
