package org.cardanofoundation.ledgersync.service.impl;

import com.bloxbean.cardano.yaci.core.model.Amount;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTxOut;
import org.cardanofoundation.ledgersync.consumercommon.entity.*;
import org.cardanofoundation.ledgersync.projection.MaTxMintProjection;
import org.cardanofoundation.ledgersync.projection.MaTxOutProjection;
import org.cardanofoundation.ledgersync.projection.MultiAssetTotalVolumeProjection;
import org.cardanofoundation.ledgersync.projection.MultiAssetTxCountProjection;
import org.cardanofoundation.ledgersync.repository.MaTxMintRepository;
import org.cardanofoundation.ledgersync.repository.MultiAssetRepository;
import org.cardanofoundation.ledgersync.repository.MultiAssetTxOutRepository;
import org.cardanofoundation.ledgersync.service.AggregatedDataCachingService;
import org.cardanofoundation.ledgersync.service.BlockDataService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class MultiAssetServiceImplTest {

  @Mock
  MaTxMintRepository maTxMintRepository;

  @Mock
  MultiAssetRepository multiAssetRepository;

  @Mock
  MultiAssetTxOutRepository multiAssetTxOutRepository;

  @Mock
  BlockDataService blockDataService;

  @Mock
  AggregatedDataCachingService aggregatedDataCachingService;

  @Captor
  ArgumentCaptor<Collection<MultiAsset>> multiAssetsCaptor;

  @Captor
  ArgumentCaptor<List<MaTxMint>> maTxMintsCaptor;

  MultiAssetServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new MultiAssetServiceImpl(
        maTxMintRepository, multiAssetRepository,
        multiAssetTxOutRepository, blockDataService,
        aggregatedDataCachingService
    );
  }

  @Test
  @DisplayName("Should skip asset mint handling if no asset minting exists")
  void shouldSkipAssetMintingTest() {
    AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
    Map<String, Tx> txMap = Collections.emptyMap();

    Assertions.assertDoesNotThrow(() -> victim.handleMultiAssetMint(List.of(aggregatedTx), txMap));
    Mockito.verifyNoInteractions(maTxMintRepository);
    Mockito.verifyNoInteractions(multiAssetRepository);
    Mockito.verifyNoInteractions(multiAssetTxOutRepository);
    Mockito.verifyNoInteractions(blockDataService);
    Mockito.verifyNoInteractions(aggregatedDataCachingService);
  }

  @Test
  @DisplayName("Should handle brand new asset mints successfully")
  void shouldHandleNewAssetMintsSuccessfullyTest() {
    Block block = Mockito.mock(Block.class);
    Map<String, Tx> txMap = Map.of(
        "a86d5246c1e5ce7d66446d0a68355abe6622545d8ffe7dd832a062f6cde010bd", Mockito.mock(Tx.class)
    );
    AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
    Amount amount = Mockito.mock(Amount.class);
    String policyId = "e2bab64ca481afc5a695b7db22fd0a7df4bf930158dfa652fb337999";

    Mockito.when(block.getBlockNo()).thenReturn(177242L);
    txMap.values().forEach(tx -> Mockito.when(tx.getBlock()).thenReturn(block));
    Mockito.when(amount.getPolicyId()).thenReturn(policyId);
    Mockito.when(amount.getAssetNameBytes())
              .thenReturn("SUMMITAWARDSDefi".getBytes(StandardCharsets.UTF_8));
    Mockito.when(amount.getQuantity()).thenReturn(BigInteger.ONE);
    Mockito.when(aggregatedTx.getHash())
        .thenReturn("a86d5246c1e5ce7d66446d0a68355abe6622545d8ffe7dd832a062f6cde010bd");
    Mockito.when(aggregatedTx.getMint()).thenReturn(List.of(amount));
    Mockito.when(multiAssetRepository.findMultiAssetsByFingerprintIn(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());

    Assertions.assertDoesNotThrow(() -> victim.handleMultiAssetMint(List.of(aggregatedTx), txMap));

    Mockito.verify(multiAssetRepository, Mockito.times(1))
        .findMultiAssetsByFingerprintIn(Mockito.anyCollection());
    Mockito.verify(multiAssetRepository, Mockito.times(1)).saveAll(multiAssetsCaptor.capture());
    Mockito.verifyNoMoreInteractions(multiAssetRepository);
    Mockito.verify(maTxMintRepository, Mockito.times(1)).saveAll(maTxMintsCaptor.capture());
    Mockito.verifyNoMoreInteractions(maTxMintRepository);
    Mockito.verify(blockDataService, Mockito.times(1))
        .setFingerprintFirstAppearedBlockNoAndTxIdx(
            Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
    Mockito.verifyNoMoreInteractions(blockDataService);
    Mockito.verifyNoInteractions(multiAssetTxOutRepository);
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .addTokenCount(Mockito.anyInt());
    Mockito.verifyNoMoreInteractions(aggregatedDataCachingService);

    Collection<MultiAsset> multiAssets = multiAssetsCaptor.getValue();
    List<MaTxMint> maTxMints = maTxMintsCaptor.getValue();
    MaTxMint maTxMint = maTxMints.get(0);
    MultiAsset multiAsset = new ArrayList<>(multiAssets).get(0);
    Assertions.assertEquals(multiAsset, maTxMint.getIdent());
    Assertions.assertEquals(BigInteger.ONE, maTxMint.getQuantity());
    Assertions.assertEquals("asset132r28qxkhg0wddjjpt2qffzd9m7g37arndlxsv",
        multiAsset.getFingerprint());
    Assertions.assertEquals(policyId, multiAsset.getPolicy());
  }

  @Test
  @DisplayName("Should handle existing asset mints successfully")
  void shouldHandleExistingAssetMintsSuccessfullyTest() {
    Map<String, Tx> txMap = Map.of(
        "a86d5246c1e5ce7d66446d0a68355abe6622545d8ffe7dd832a062f6cde010bd", Mockito.mock(Tx.class)
    );
    AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
    Amount amount = Mockito.mock(Amount.class);
    String policyId = "e2bab64ca481afc5a695b7db22fd0a7df4bf930158dfa652fb337999";
    String assetName = "SUMMITAWARDSDefi";
    String assetFingerprint = "asset132r28qxkhg0wddjjpt2qffzd9m7g37arndlxsv";
    MultiAsset existingAsset = MultiAsset.builder()
        .name(HexUtil.encodeHexString(assetName.getBytes(StandardCharsets.UTF_8)))
        .fingerprint(assetFingerprint)
        .policy(policyId)
        .supply(BigInteger.ONE)
        .build();

    Mockito.when(amount.getPolicyId()).thenReturn(policyId);
    Mockito.when(amount.getAssetNameBytes()).thenReturn(assetName.getBytes(StandardCharsets.UTF_8));
    Mockito.when(amount.getQuantity()).thenReturn(BigInteger.ONE);
    Mockito.when(aggregatedTx.getHash())
        .thenReturn("a86d5246c1e5ce7d66446d0a68355abe6622545d8ffe7dd832a062f6cde010bd");
    Mockito.when(aggregatedTx.getMint()).thenReturn(List.of(amount));
    Mockito.when(multiAssetRepository.findMultiAssetsByFingerprintIn(Mockito.anyCollection()))
        .thenReturn(List.of(existingAsset));

    Assertions.assertDoesNotThrow(() -> victim.handleMultiAssetMint(List.of(aggregatedTx), txMap));

    Mockito.verify(multiAssetRepository, Mockito.times(1))
        .findMultiAssetsByFingerprintIn(Mockito.anyCollection());
    Mockito.verify(multiAssetRepository, Mockito.times(1)).saveAll(multiAssetsCaptor.capture());
    Mockito.verifyNoMoreInteractions(multiAssetRepository);
    Mockito.verify(maTxMintRepository, Mockito.times(1)).saveAll(maTxMintsCaptor.capture());
    Mockito.verifyNoMoreInteractions(maTxMintRepository);
    Mockito.verifyNoInteractions(blockDataService);
    Mockito.verifyNoInteractions(multiAssetTxOutRepository);
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .addTokenCount(Mockito.anyInt());
    Mockito.verifyNoMoreInteractions(aggregatedDataCachingService);

    Collection<MultiAsset> multiAssets = multiAssetsCaptor.getValue();
    List<MaTxMint> maTxMints = maTxMintsCaptor.getValue();
    MaTxMint maTxMint = maTxMints.get(0);
    MultiAsset multiAsset = new ArrayList<>(multiAssets).get(0);
    Assertions.assertNotNull(maTxMint.getIdent());
    Assertions.assertEquals(BigInteger.ONE, maTxMint.getQuantity());
    Assertions.assertEquals(BigInteger.TWO, multiAsset.getSupply());
    Assertions.assertEquals(assetFingerprint, multiAsset.getFingerprint());
    Assertions.assertEquals(policyId, multiAsset.getPolicy());
  }

  @Test
  @DisplayName("Find multi assets by fingerprints test")
  void findMultiAssetByFingerprintsTest() {
    MultiAsset multiAsset = Mockito.mock(MultiAsset.class);
    Set<String> fingerprints = Set.of("asset132r28qxkhg0wddjjpt2qffzd9m7g37arndlxsv");

    Collection<MultiAsset> multiAssets =
        victim.findMultiAssetsByFingerprintIn(fingerprints);
    Assertions.assertTrue(CollectionUtils.isEmpty(multiAssets));

    Mockito.when(multiAssetRepository.findMultiAssetsByFingerprintIn(Mockito.anyCollection()))
        .thenReturn(List.of(multiAsset));

    multiAssets = victim.findMultiAssetsByFingerprintIn(fingerprints);
    Assertions.assertFalse(CollectionUtils.isEmpty(multiAssets));
    Assertions.assertEquals(1, multiAssets.size());
    Assertions.assertEquals(multiAsset, new ArrayList<>(multiAssets).get(0));
  }

  @Test
  @DisplayName("Build MaTxOut test")
  void buildMaTxOutTest() {
    AggregatedTxOut aggregatedTxOut = Mockito.mock(AggregatedTxOut.class);
    Amount amount = Mockito.mock(Amount.class);
    TxOut txOut = Mockito.mock(TxOut.class);
    String policyId = "e2bab64ca481afc5a695b7db22fd0a7df4bf930158dfa652fb337999";
    String assetName = "SUMMITAWARDSDefi";
    String assetFingerprint = "asset132r28qxkhg0wddjjpt2qffzd9m7g37arndlxsv";

    Mockito.when(amount.getPolicyId()).thenReturn(policyId);
    Mockito.when(amount.getAssetName()).thenReturn(assetName);
    Mockito.when(amount.getAssetNameBytes()).thenReturn(assetName.getBytes(StandardCharsets.UTF_8));
    Mockito.when(amount.getQuantity()).thenReturn(BigInteger.ONE);
    Mockito.when(aggregatedTxOut.getAmounts()).thenReturn(List.of(amount));

    MultiValueMap<String, MaTxOut> maTxOutMap =
        victim.buildMaTxOut(aggregatedTxOut, txOut);
    Assertions.assertFalse(CollectionUtils.isEmpty(maTxOutMap));
    Assertions.assertEquals(1, maTxOutMap.size());
    Assertions.assertEquals(1, maTxOutMap.get(assetFingerprint).size());

    MaTxOut maTxOut = maTxOutMap.get(assetFingerprint).get(0);
    Assertions.assertEquals(txOut, maTxOut.getTxOut());
    Assertions.assertEquals(BigInteger.ONE, maTxOut.getQuantity());
  }

  @Test
  @DisplayName("Should skip MaTxOut ident update if no MaTxOut supplied")
  void shouldSkipMaTxOutIdentUpdateIfNoMaTxOutSuppliedTest() {
    MultiValueMap<String, MaTxOut> maTxOutMap = new LinkedMultiValueMap<>();
    Assertions.assertTrue(CollectionUtils.isEmpty(victim.updateIdentMaTxOuts(maTxOutMap)));
    Mockito.verifyNoInteractions(blockDataService);
  }

  @Test
  @DisplayName("Should skip MaTxOut if ident was not minted before")
  void shouldSkipMaTxOutIfIdentNotMintedBeforeTest() {
    String txHash = "c4e64338c4bebe9a870abf946ffe46caede52e8b3d5831caf0717c016f70ea3e";
    String assetFingerprint = "asset132r28qxkhg0wddjjpt2qffzd9m7g37arndlxsv";
    MultiValueMap<String, MaTxOut> maTxOutMap = new LinkedMultiValueMap<>();
    Block block = Mockito.mock(Block.class);
    TxOut txOut = Mockito.mock(TxOut.class);
    Tx tx = Mockito.mock(Tx.class);
    MultiAsset multiAsset = Mockito.mock(MultiAsset.class);
    MaTxOut maTxOut = MaTxOut.builder()
        .txOut(txOut)
        .quantity(BigInteger.ONE)
        .build();

    maTxOutMap.add(assetFingerprint, maTxOut);
    Mockito.when(txOut.getIndex()).thenReturn((short) 0);
    Mockito.when(block.getBlockNo()).thenReturn(1234L);
    Mockito.when(tx.getHash()).thenReturn(txHash);
    Mockito.when(tx.getBlock()).thenReturn(block);
    Mockito.when(tx.getBlockIndex()).thenReturn(2L);
    Mockito.when(txOut.getTx()).thenReturn(tx);
    Mockito.when(multiAsset.getFingerprint()).thenReturn(assetFingerprint);

    Mockito.when(multiAssetRepository.findMultiAssetsByFingerprintIn(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());
    Mockito.when(blockDataService.getFingerprintFirstAppearedBlockNoAndTxIdx(Mockito.anyString()))
        .thenReturn(null);

    Collection<MaTxOut> maTxOuts = victim.updateIdentMaTxOuts(maTxOutMap);
    Assertions.assertTrue(CollectionUtils.isEmpty(maTxOuts));
    Mockito.verify(blockDataService, Mockito.times(1))
        .getFingerprintFirstAppearedBlockNoAndTxIdx(Mockito.anyString());
    Mockito.verify(blockDataService, Mockito.times(1))
        .saveAssetFingerprintNotMintedAtTx(Mockito.anyString(), Mockito.anyString());
    Mockito.verifyNoMoreInteractions(blockDataService);

    Mockito.when(multiAssetRepository.findMultiAssetsByFingerprintIn(Mockito.anyCollection()))
        .thenReturn(List.of(multiAsset));
    Mockito.when(blockDataService.getFingerprintFirstAppearedBlockNoAndTxIdx(Mockito.anyString()))
        .thenReturn(Pair.of(1234L, 3L));

    maTxOuts = victim.updateIdentMaTxOuts(maTxOutMap);
    Assertions.assertTrue(CollectionUtils.isEmpty(maTxOuts));
    Mockito.verify(blockDataService, Mockito.times(2))
        .getFingerprintFirstAppearedBlockNoAndTxIdx(Mockito.anyString());
    Mockito.verify(blockDataService, Mockito.times(2))
        .saveAssetFingerprintNotMintedAtTx(Mockito.anyString(), Mockito.anyString());
    Mockito.verifyNoMoreInteractions(blockDataService);

    Mockito.when(blockDataService.getFingerprintFirstAppearedBlockNoAndTxIdx(Mockito.anyString()))
        .thenReturn(Pair.of(1235L, 2L));

    maTxOuts = victim.updateIdentMaTxOuts(maTxOutMap);
    Assertions.assertTrue(CollectionUtils.isEmpty(maTxOuts));
    Mockito.verify(blockDataService, Mockito.times(3))
        .getFingerprintFirstAppearedBlockNoAndTxIdx(Mockito.anyString());
    Mockito.verify(blockDataService, Mockito.times(3))
        .saveAssetFingerprintNotMintedAtTx(Mockito.anyString(), Mockito.anyString());
    Mockito.verifyNoMoreInteractions(blockDataService);
  }

  @Test
  @DisplayName("Should update MaTxOut ident successfully")
  void shouldUpdateMaTxOutIdentSuccessfullyTest() {
    String assetFingerprint = "asset132r28qxkhg0wddjjpt2qffzd9m7g37arndlxsv";
    MultiValueMap<String, MaTxOut> maTxOutMap = new LinkedMultiValueMap<>();
    Block block = Mockito.mock(Block.class);
    TxOut txOut = Mockito.mock(TxOut.class);
    Tx tx = Mockito.mock(Tx.class);
    MultiAsset multiAsset = Mockito.mock(MultiAsset.class);
    MaTxOut maTxOut = MaTxOut.builder()
        .txOut(txOut)
        .quantity(BigInteger.ONE)
        .build();

    maTxOutMap.add(assetFingerprint, maTxOut);
    Mockito.when(block.getBlockNo()).thenReturn(1234L);
    Mockito.when(tx.getBlock()).thenReturn(block);
    Mockito.when(tx.getBlockIndex()).thenReturn(2L);
    Mockito.when(txOut.getTx()).thenReturn(tx);
    Mockito.when(multiAsset.getFingerprint()).thenReturn(assetFingerprint);

    Mockito.when(multiAssetRepository.findMultiAssetsByFingerprintIn(Mockito.anyCollection()))
        .thenReturn(List.of(multiAsset));
    Mockito.when(blockDataService.getFingerprintFirstAppearedBlockNoAndTxIdx(Mockito.anyString()))
        .thenReturn(Pair.of(1233L, 2L));

    Collection<MaTxOut> maTxOuts = victim.updateIdentMaTxOuts(maTxOutMap);
    Assertions.assertFalse(CollectionUtils.isEmpty(maTxOuts));
    Assertions.assertEquals(multiAsset, new ArrayList<>(maTxOuts).get(0).getIdent());
    Mockito.verify(blockDataService, Mockito.times(1))
        .getFingerprintFirstAppearedBlockNoAndTxIdx(Mockito.anyString());
    Mockito.verifyNoMoreInteractions(blockDataService);

    Mockito.when(multiAssetRepository.findMultiAssetsByFingerprintIn(Mockito.anyCollection()))
        .thenReturn(List.of(multiAsset));
    Mockito.when(blockDataService.getFingerprintFirstAppearedBlockNoAndTxIdx(Mockito.anyString()))
        .thenReturn(Pair.of(1234L, 1L));

    maTxOuts = victim.updateIdentMaTxOuts(maTxOutMap);
    Assertions.assertFalse(CollectionUtils.isEmpty(maTxOuts));
    Assertions.assertEquals(multiAsset, new ArrayList<>(maTxOuts).get(0).getIdent());
    Mockito.verify(blockDataService, Mockito.times(2))
        .getFingerprintFirstAppearedBlockNoAndTxIdx(Mockito.anyString());
    Mockito.verifyNoMoreInteractions(blockDataService);

    Mockito.when(multiAssetRepository.findMultiAssetsByFingerprintIn(Mockito.anyCollection()))
        .thenReturn(List.of(multiAsset));
    Mockito.when(blockDataService.getFingerprintFirstAppearedBlockNoAndTxIdx(Mockito.anyString()))
        .thenReturn(Pair.of(1233L, 2L));

    maTxOuts = victim.updateIdentMaTxOuts(maTxOutMap);
    Assertions.assertFalse(CollectionUtils.isEmpty(maTxOuts));
    Assertions.assertEquals(multiAsset, new ArrayList<>(maTxOuts).get(0).getIdent());
    Mockito.verify(blockDataService, Mockito.times(3))
        .getFingerprintFirstAppearedBlockNoAndTxIdx(Mockito.anyString());
    Mockito.verifyNoMoreInteractions(blockDataService);
  }

  @Test
  @DisplayName("Find MaTxOut by TxOut test")
  void findMaTxOutByTxOutTest() {
    String assetFingerprint = "asset132r28qxkhg0wddjjpt2qffzd9m7g37arndlxsv";
    MaTxOutProjection maTxOutProjection = Mockito.mock(MaTxOutProjection.class);
    TxOut txOut = Mockito.mock(TxOut.class);

    Mockito.when(txOut.getId()).thenReturn(1L);
    Collection<MaTxOut> maTxOuts = victim.findAllByTxOutIn(List.of(txOut));
    Assertions.assertTrue(CollectionUtils.isEmpty(maTxOuts));

    Mockito.when(maTxOutProjection.getFingerprint()).thenReturn(assetFingerprint);
    Mockito.when(maTxOutProjection.getTxOutId()).thenReturn(1L);
    Mockito.when(maTxOutProjection.getQuantity()).thenReturn(BigInteger.ONE);
    Mockito.when(multiAssetTxOutRepository.findAllByTxOutIdsIn(Mockito.anyCollection()))
        .thenReturn(List.of(maTxOutProjection));

    maTxOuts = victim.findAllByTxOutIn(List.of(txOut));
    Assertions.assertFalse(CollectionUtils.isEmpty(maTxOuts));
    MaTxOut maTxOut = new ArrayList<>(maTxOuts).get(0);
    Assertions.assertEquals(1L, maTxOut.getTxOut().getId());
    Assertions.assertEquals(assetFingerprint, maTxOut.getIdent().getFingerprint());
    Assertions.assertEquals(BigInteger.ONE, maTxOut.getQuantity());
  }

  @Test
  @DisplayName("Should skip asset rollback if no txs supplied")
  void shouldSkipMultiAssetRollbackIfNoTxsSuppliedTest() {
    victim.rollbackMultiAssets(Collections.emptyList());
    Mockito.verify(maTxMintRepository, Mockito.times(1))
        .findAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(maTxMintRepository);
    Mockito.verify(multiAssetRepository, Mockito.times(1))
        .getMultiAssetTxCountByTxs(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(multiAssetRepository);
  }


  @Test
  @DisplayName("Should rollback multi assets successfully")
  void shouldRollbackMultiAssetsSuccessfullyTest() {
    MaTxMintProjection maTxMintProjection = Mockito.mock(MaTxMintProjection.class);
    MaTxMintProjection maTxMintProjection2 = Mockito.mock(MaTxMintProjection.class);
    MultiAssetTxCountProjection multiAssetTxCountProjection =
        Mockito.mock(MultiAssetTxCountProjection.class);
    MultiAssetTotalVolumeProjection multiAssetTotalVolumeProjection =
        Mockito.mock(MultiAssetTotalVolumeProjection.class);
    MultiAsset givenMultiAsset = MultiAsset.builder()
        .id(1L)
        .txCount(10L)
        .supply(BigInteger.TEN)
        .totalVolume(BigInteger.TEN)
        .build();

    Mockito.when(maTxMintProjection.getQuantity()).thenReturn(BigInteger.ONE);
    Mockito.when(maTxMintProjection.getIdentId()).thenReturn(1L);
    Mockito.when(maTxMintProjection2.getQuantity()).thenReturn(BigInteger.ONE);
    Mockito.when(maTxMintProjection2.getIdentId()).thenReturn(1L);
    Mockito.when(multiAssetTxCountProjection.getTxCount()).thenReturn(3L);
    Mockito.when(multiAssetTxCountProjection.getIdentId()).thenReturn(1L);
    Mockito.when(multiAssetTotalVolumeProjection.getTotalVolume()).thenReturn(BigInteger.ONE);
    Mockito.when(multiAssetTotalVolumeProjection.getIdentId()).thenReturn(1L);
    Mockito.when(maTxMintRepository.findAllByTxIn(Mockito.anyCollection()))
        .thenReturn(List.of(maTxMintProjection, maTxMintProjection2));
    Mockito.when(multiAssetRepository.getMultiAssetTxCountByTxs(Mockito.anyCollection()))
        .thenReturn(List.of(multiAssetTxCountProjection));
    Mockito.when(multiAssetRepository.findAllById(Mockito.anyCollection()))
        .thenReturn(List.of(givenMultiAsset));
    Mockito.when(multiAssetRepository.getMultiAssetTotalVolumeByTxs(Mockito.anyCollection()))
        .thenReturn(List.of(multiAssetTotalVolumeProjection));

    victim.rollbackMultiAssets(List.of(new Tx()));

    Mockito.verify(maTxMintRepository, Mockito.times(1))
        .findAllByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(maTxMintRepository);
    Mockito.verify(multiAssetRepository, Mockito.times(1))
        .getMultiAssetTxCountByTxs(Mockito.anyCollection());
    Mockito.verify(multiAssetRepository, Mockito.times(1))
        .findAllById(Mockito.anyCollection());
    Mockito.verify(multiAssetRepository, Mockito.times(1))
        .getMultiAssetTotalVolumeByTxs(Mockito.anyCollection());
    Mockito.verify(multiAssetRepository, Mockito.times(1))
        .saveAll(multiAssetsCaptor.capture());
    Mockito.verifyNoMoreInteractions(multiAssetRepository);

    Collection<MultiAsset> multiAssets = multiAssetsCaptor.getValue();
    MultiAsset multiAsset = new ArrayList<>(multiAssets).get(0);
    Assertions.assertEquals(BigInteger.valueOf(8L), multiAsset.getSupply());
    Assertions.assertEquals(7L, multiAsset.getTxCount());
    Assertions.assertEquals(BigInteger.valueOf(9L), multiAsset.getTotalVolume());
  }
}
