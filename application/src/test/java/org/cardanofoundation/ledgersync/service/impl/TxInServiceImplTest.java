package org.cardanofoundation.ledgersync.service.impl;


import com.bloxbean.cardano.yaci.core.model.RedeemerTag;
import org.cardanofoundation.ledgersync.consumercommon.entity.*;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx.TxBuilder;
import org.cardanofoundation.ledgersync.common.common.Era;
import org.cardanofoundation.ledgersync.aggregate.AggregatedAddressBalance;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx.AggregatedTxBuilder;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTxIn;
import org.cardanofoundation.ledgersync.repository.TxInRepository;
import org.cardanofoundation.ledgersync.repository.UnconsumeTxInRepository;
import org.cardanofoundation.ledgersync.service.BlockDataService;
import org.cardanofoundation.ledgersync.service.MultiAssetService;
import org.cardanofoundation.ledgersync.service.TxOutService;
import org.cardanofoundation.ledgersync.util.TestStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ExtendWith(MockitoExtension.class)
class TxInServiceImplTest {

  private static final int STAKE_KEY_HASH_LENGTH = 56;
  private static final Map<Pair<String, Short>, TxOut> txOutMap = new HashMap<>();

  @Mock
  BlockDataService blockDataService;

  @Mock
  TxOutService txOutService;

  @Mock
  MultiAssetService multiAssetService;

  @Mock
  TxInRepository txInRepository;

  @Mock
  UnconsumeTxInRepository unconsumeTxInRepository;

  @Captor
  ArgumentCaptor<Queue<TxIn>> txInQueueCaptor;

  @Captor
  ArgumentCaptor<List<UnconsumeTxIn>> unconsumeTxInsCaptor;

  @InjectMocks
  TxInServiceImpl victim;

  @BeforeEach
  void setUp() {
    txOutMap.clear();

    txOutMap.put(
        Pair.of("9fae390ccaeb5523f85a16cda141c2105f708738fb04218abbf825548b37f7aa", (short) 0),
        givenTxOut(0L,
            "DdzFFzCqrhsf7cannGRC1UusPpWLYJV3ZvgnJbUcEhtetHWKyrwGqZqT7RpbgyS2GcoYS7M1LoW1QGCFuA3EWuqbJ2N8GK36bFKRdS6Y",
            0, BigInteger.valueOf(384902000000L), null)
    );
    txOutMap.put(
        Pair.of("30cb1f668959f96ca5100ffbb1c524507a2fc7f0bb9407b5fb581f2574eda322", (short) 0),
        givenTxOut(1L,
            "addr1qx6tzadessnknvgqpjjt390ly99ug6htxf4dw3yh29jqd00zz6d6dw64q4xxspqwx0sewrrw7s53muh5qrwqvuhpgc2qrn2sgr",
            0, BigInteger.valueOf(1481480L), null)
    );
    txOutMap.put(
        Pair.of("f61ac94ddc07a3fa54f116a68c78ab7aa6edfb79b67e7ea5de44de2d8ea3b88e", (short) 0),
        givenTxOut(2L,
            "addr1q8saj9wppjzqq9aa8yyg4qjs0vn32zjr36ysw7zzy9y3xztl9fadz30naflhmq653up3tkz275gh5npdejwjj23l0rdqls5spd",
            0, BigInteger.valueOf(1796788225L), null)
    );
  }

  @DisplayName("Should skip tx in handling if no tx ins supplied")
  void shouldSkipTxInHandlingTest() {
    victim.handleTxIns(Collections.emptyList(), Collections.emptyMap(),
            Collections.emptyMap(), Collections.emptyMap(),
            Collections.emptyMap(), Collections.emptyMap());

    Mockito.verifyNoInteractions(blockDataService);
    Mockito.verifyNoInteractions(txOutService);
    Mockito.verifyNoInteractions(multiAssetService);
    Mockito.verifyNoInteractions(txInRepository);
    Mockito.verifyNoInteractions(unconsumeTxInRepository);
  }

  @Test
  @DisplayName("Should fail when tx in not found")
  void shouldFailWhenTxInNotFound() {
    Map<Pair<String, Short>, TxOut> newTxOutMap = Collections.emptyMap();
    Map<Long, List<MaTxOut>> newMaTxOutMap = Collections.emptyMap();
    Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap = Collections.emptyMap();
    // Mainnet hash
    String blockHash = "b526911a45a4910171d21bea8fd4761eb43dd0c49b2c4dab494693b25c02db84";
    String txHash = "72e4bc545f956c0ab831a0a18362e2990ec3a0e01c3577bce1721e7cb095fd3f";
    String txInId = "9fae390ccaeb5523f85a16cda141c2105f708738fb04218abbf825548b37f7aa";
    AggregatedBlock aggregatedBlock = givenAggregatedBlock(true);
    Collection<TxOut> txOuts = givenByronTxOuts();
    AggregatedTx aggregatedTx = givenAggregatedTx(blockHash, txHash, false, false, txOuts);
    AggregatedTxIn aggregatedTxIn = givenTxIn(txInId, 0, null);
    Tx tx = aggregatedTxToTx(aggregatedTx);
    Map<String, Set<AggregatedTxIn>> txInMap =
        Map.of(aggregatedTx.getHash(), Set.of(aggregatedTxIn));
    Map<String, Tx> txMap = Map.of(tx.getHash(), tx);

    Mockito.when(blockDataService.getAggregatedBlock(blockHash)).thenReturn(aggregatedBlock);
    Mockito.when(txOutService.getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());

    List<AggregatedTx> aggregatedTxList = List.of(aggregatedTx);
    Assertions.assertThrows(IllegalStateException.class, () ->
        victim.handleTxIns(aggregatedTxList, txInMap, txMap,
            newTxOutMap, newMaTxOutMap, redeemersMap));

    Mockito.verify(blockDataService, Mockito.times(1))
        .getAggregatedBlock(Mockito.anyString());
    Mockito.verifyNoMoreInteractions(blockDataService);
    Mockito.verify(txOutService, Mockito.times(1))
        .getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txOutService);
    Mockito.verifyNoInteractions(multiAssetService);
    Mockito.verifyNoInteractions(txInRepository);
    Mockito.verifyNoInteractions(unconsumeTxInRepository);
  }

  @Test
  @DisplayName("Should handle Byron tx ins successfully")
  void shouldHandleByronTxInsSuccessfullyTest() {
    Map<Pair<String, Short>, TxOut> newTxOutMap = Collections.emptyMap();
    Map<Long, List<MaTxOut>> newMaTxOutMap = Collections.emptyMap();
    Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap = Collections.emptyMap();
    // Mainnet hash
    String blockHash = "b526911a45a4910171d21bea8fd4761eb43dd0c49b2c4dab494693b25c02db84";
    String txHash = "72e4bc545f956c0ab831a0a18362e2990ec3a0e01c3577bce1721e7cb095fd3f";
    String txInId = "9fae390ccaeb5523f85a16cda141c2105f708738fb04218abbf825548b37f7aa";
    AggregatedBlock aggregatedBlock = givenAggregatedBlock(true);
    Collection<TxOut> txOuts = givenByronTxOuts();
    AggregatedTx aggregatedTx = givenAggregatedTx(blockHash, txHash, false, false, txOuts);
    AggregatedTxIn aggregatedTxIn = givenTxIn(txInId, 0, null);
    TxOut txOutFromTxIn = givenTxOutFromTxIn(aggregatedTxIn);
    Tx tx = aggregatedTxToTx(aggregatedTx);
    AggregatedAddressBalance addressBalance =
        AggregatedAddressBalance.from(txOutFromTxIn.getAddress());
    Map<String, AggregatedAddressBalance> aggregatedAddressBalanceMap = new HashMap<>();
    Map<String, BigInteger> expectedAddressBalance = Map.of(
        "DdzFFzCqrhsf7cannGRC1UusPpWLYJV3ZvgnJbUcEhtetHWKyrwGqZqT7RpbgyS2GcoYS7M1LoW1QGCFuA3EWuqbJ2N8GK36bFKRdS6Y",
        BigInteger.valueOf(-384902000000L)
    );
    aggregatedAddressBalanceMap.put(txOutFromTxIn.getAddress(), addressBalance);
    Map<String, Set<AggregatedTxIn>> txInMap =
        Map.of(aggregatedTx.getHash(), Set.of(aggregatedTxIn));
    Map<String, Tx> txMap = Map.of(tx.getHash(), tx);

    Mockito.when(blockDataService.getAggregatedBlock(blockHash)).thenReturn(aggregatedBlock);
    Mockito.when(txOutService.getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection()))
        .thenReturn(List.of(txOutFromTxIn));
    Mockito.when(blockDataService
            .getAggregatedAddressBalanceFromAddress(txOutFromTxIn.getAddress()))
        .thenReturn(addressBalance);

    victim.handleTxIns(List.of(aggregatedTx), txInMap, txMap,
        newTxOutMap, newMaTxOutMap, redeemersMap);

    Mockito.verify(blockDataService, Mockito.times(1))
        .getAggregatedAddressBalanceFromAddress(Mockito.anyString());
    Mockito.verify(blockDataService, Mockito.times(1))
        .getAggregatedBlock(Mockito.anyString());
    Mockito.verifyNoMoreInteractions(blockDataService);
    Mockito.verify(txOutService, Mockito.times(1))
        .getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txOutService);
    Mockito.verifyNoInteractions(multiAssetService);
    Mockito.verify(txInRepository, Mockito.times(1))
        .saveAll(txInQueueCaptor.capture());
    Mockito.verifyNoMoreInteractions(txInRepository);
    Mockito.verifyNoInteractions(unconsumeTxInRepository);

    Assertions.assertEquals(BigInteger.valueOf(171070), tx.getFee());
    Assertions.assertNull(tx.getDeposit());
    Queue<TxIn> txInQueue = txInQueueCaptor.getValue();
    TxIn txIn = txInQueue.remove();
    Assertions.assertEquals(txIn.getTxOut().getId(), txOutFromTxIn.getTx().getId());
    Assertions.assertEquals(txIn.getTxOutIndex(), txOutFromTxIn.getIndex());

    AggregatedAddressBalance aggregatedAddressBalance =
        aggregatedAddressBalanceMap.get(txOutFromTxIn.getAddress());
    Assertions.assertEquals(
        aggregatedAddressBalance.getTotalNativeBalance(),
        expectedAddressBalance.get(txOutFromTxIn.getAddress()));
  }

  @Test
  @DisplayName("Should handle non-Byron tx ins successfully")
  void shouldHandleNonByronTxInsSuccessfullyTest() {
    Map<Pair<String, Short>, TxOut> newTxOutMap = Collections.emptyMap();
    Map<Long, List<MaTxOut>> newMaTxOutMap = Collections.emptyMap();
    Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap = Collections.emptyMap();
    // Mainnet hash
    String blockHash = "958e9224c45f8f29fb534426d3c19aee02a6ff4b6414a89772af6480f97ea5a7";
    String txHash = "7514f13c2a263cd3ca38c3e04879c49e51fb56965ca3a3638f9ca1e9c9db9c32";
    String txInId = "30cb1f668959f96ca5100ffbb1c524507a2fc7f0bb9407b5fb581f2574eda322";
    AggregatedBlock aggregatedBlock = givenAggregatedBlock(false);
    Collection<TxOut> txOuts = givenNonByronTxOuts();
    AggregatedTx aggregatedTx = givenAggregatedTx(blockHash, txHash, true, true, txOuts);
    AggregatedTxIn aggregatedTxIn = givenTxIn(txInId, 0, null);
    TxOut txOutFromTxIn = givenTxOutFromTxIn(aggregatedTxIn);
    Tx tx = aggregatedTxToTx(aggregatedTx);
    AggregatedAddressBalance addressBalance =
        AggregatedAddressBalance.from(txOutFromTxIn.getAddress());
    Map<String, AggregatedAddressBalance> aggregatedAddressBalanceMap = new HashMap<>();
    Map<String, BigInteger> expectedAddressBalance = Map.of(
        "addr1qx6tzadessnknvgqpjjt390ly99ug6htxf4dw3yh29jqd00zz6d6dw64q4xxspqwx0sewrrw7s53muh5qrwqvuhpgc2qrn2sgr",
        BigInteger.valueOf(-1481480L)
    );
    aggregatedAddressBalanceMap.put(txOutFromTxIn.getAddress(), addressBalance);
    Map<String, Set<AggregatedTxIn>> txInMap =
        Map.of(aggregatedTx.getHash(), Set.of(aggregatedTxIn));
    Map<String, Tx> txMap = Map.of(tx.getHash(), tx);

    Mockito.when(blockDataService.getAggregatedBlock(blockHash)).thenReturn(aggregatedBlock);
    Mockito.when(txOutService.getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection()))
        .thenReturn(List.of(txOutFromTxIn));
    Mockito.when(blockDataService
            .getAggregatedAddressBalanceFromAddress(txOutFromTxIn.getAddress()))
        .thenReturn(addressBalance);

    victim.handleTxIns(List.of(aggregatedTx), txInMap, txMap,
        newTxOutMap, newMaTxOutMap, redeemersMap);

    Mockito.verify(blockDataService, Mockito.times(1))
        .getAggregatedAddressBalanceFromAddress(Mockito.anyString());
    Mockito.verify(blockDataService, Mockito.times(1))
        .getAggregatedBlock(Mockito.anyString());
    Mockito.verifyNoMoreInteractions(blockDataService);
    Mockito.verify(txOutService, Mockito.times(1))
        .getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txOutService);
    Mockito.verify(multiAssetService, Mockito.times(1))
        .findAllByTxOutIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(multiAssetService);
    Mockito.verify(txInRepository, Mockito.times(1))
        .saveAll(txInQueueCaptor.capture());
    Mockito.verifyNoMoreInteractions(txInRepository);
    Mockito.verifyNoInteractions(unconsumeTxInRepository);

    Assertions.assertEquals(BigInteger.valueOf(177645), tx.getFee());
    Assertions.assertEquals(100000L, tx.getDeposit());
    Queue<TxIn> txInQueue = txInQueueCaptor.getValue();
    TxIn txIn = txInQueue.remove();
    Assertions.assertEquals(txIn.getTxOut().getId(), txOutFromTxIn.getTx().getId());
    Assertions.assertEquals(txIn.getTxOutIndex(), txOutFromTxIn.getIndex());

    AggregatedAddressBalance aggregatedAddressBalance =
        aggregatedAddressBalanceMap.get(txOutFromTxIn.getAddress());
    Assertions.assertEquals(
        aggregatedAddressBalance.getTotalNativeBalance(),
        expectedAddressBalance.get(txOutFromTxIn.getAddress()));
  }

  @Test
  @DisplayName("Should handle non-Byron tx ins with multi asset successfully")
  void shouldHandleNonByronTxInsWithMultiAssetSuccessfullyTest() {
    Map<Pair<String, Short>, TxOut> newTxOutMap = Collections.emptyMap();
    Map<Long, List<MaTxOut>> newMaTxOutMap = Collections.emptyMap();
    Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap = Collections.emptyMap();
    // Mainnet hash
    String blockHash = "958e9224c45f8f29fb534426d3c19aee02a6ff4b6414a89772af6480f97ea5a7";
    String txHash = "7514f13c2a263cd3ca38c3e04879c49e51fb56965ca3a3638f9ca1e9c9db9c32";
    String txInId = "30cb1f668959f96ca5100ffbb1c524507a2fc7f0bb9407b5fb581f2574eda322";
    AggregatedBlock aggregatedBlock = givenAggregatedBlock(false);
    Collection<TxOut> txOuts = givenNonByronTxOuts();
    AggregatedTx aggregatedTx = givenAggregatedTx(blockHash, txHash, true, true, txOuts);
    AggregatedTxIn aggregatedTxIn = givenTxIn(txInId, 0, null);
    TxOut txOutFromTxIn = givenTxOutFromTxIn(aggregatedTxIn);
    String assetFingerprint = "asset1yeavs0w2rd762k3s902hnwas3rzq9yqt8wv35a";
    MaTxOut maTxOut = givenMaTxOut(1L, assetFingerprint, BigInteger.ONE);
    Tx tx = aggregatedTxToTx(aggregatedTx);
    AggregatedAddressBalance addressBalance =
        AggregatedAddressBalance.from(txOutFromTxIn.getAddress());
    Map<String, AggregatedAddressBalance> aggregatedAddressBalanceMap = new HashMap<>();
    Map<String, BigInteger> expectedAddressBalance = Map.of(
        "addr1qx6tzadessnknvgqpjjt390ly99ug6htxf4dw3yh29jqd00zz6d6dw64q4xxspqwx0sewrrw7s53muh5qrwqvuhpgc2qrn2sgr",
        BigInteger.valueOf(-1481480L)
    );
    Map<String, BigInteger> expectedAssetBalance = Map.of(assetFingerprint, BigInteger.valueOf(-1));
    aggregatedAddressBalanceMap.put(txOutFromTxIn.getAddress(), addressBalance);
    Map<String, Set<AggregatedTxIn>> txInMap =
        Map.of(aggregatedTx.getHash(), Set.of(aggregatedTxIn));
    Map<String, Tx> txMap = Map.of(tx.getHash(), tx);

    Mockito.when(blockDataService.getAggregatedBlock(blockHash)).thenReturn(aggregatedBlock);
    Mockito.when(txOutService.getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection()))
        .thenReturn(List.of(txOutFromTxIn));
    Mockito.when(blockDataService
            .getAggregatedAddressBalanceFromAddress(txOutFromTxIn.getAddress()))
        .thenReturn(addressBalance);
    Mockito.when(multiAssetService.findAllByTxOutIn(Mockito.anyCollection()))
        .thenReturn(List.of(maTxOut));

    victim.handleTxIns(List.of(aggregatedTx), txInMap, txMap,
        newTxOutMap, newMaTxOutMap, redeemersMap);

    Mockito.verify(blockDataService, Mockito.times(1))
        .getAggregatedAddressBalanceFromAddress(Mockito.anyString());
    Mockito.verify(blockDataService, Mockito.times(1))
        .getAggregatedBlock(Mockito.anyString());
    Mockito.verifyNoMoreInteractions(blockDataService);
    Mockito.verify(txOutService, Mockito.times(1))
        .getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txOutService);
    Mockito.verify(multiAssetService, Mockito.times(1))
        .findAllByTxOutIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(multiAssetService);
    Mockito.verify(txInRepository, Mockito.times(1))
        .saveAll(txInQueueCaptor.capture());
    Mockito.verifyNoMoreInteractions(txInRepository);
    Mockito.verifyNoInteractions(unconsumeTxInRepository);

    Assertions.assertEquals(BigInteger.valueOf(177645), tx.getFee());
    Assertions.assertEquals(100000L, tx.getDeposit());
    Queue<TxIn> txInQueue = txInQueueCaptor.getValue();
    TxIn txIn = txInQueue.remove();
    Assertions.assertEquals(txIn.getTxOut().getId(), txOutFromTxIn.getTx().getId());
    Assertions.assertEquals(txIn.getTxOutIndex(), txOutFromTxIn.getIndex());

    AggregatedAddressBalance aggregatedAddressBalance =
        aggregatedAddressBalanceMap.get(txOutFromTxIn.getAddress());
    Assertions.assertEquals(
        expectedAddressBalance.get(txOutFromTxIn.getAddress()),
        aggregatedAddressBalance.getTotalNativeBalance());
    Assertions.assertEquals(
        expectedAssetBalance.get(assetFingerprint),
        aggregatedAddressBalance.getMaBalances().get(Pair.of(txHash, assetFingerprint)).get()
    );
  }

  @Test
  @DisplayName("Should handle non-Byron tx ins with multi asset in current batch successfully")
  void shouldHandleNonByronTxInsWithMultiAssetInCurrentBatchSuccessfullyTest() {
    Map<Pair<String, Short>, TxOut> newTxOutMap = new ConcurrentHashMap<>();
    Map<Long, List<MaTxOut>> newMaTxOutMap = new ConcurrentHashMap<>();
    Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap = Collections.emptyMap();
    // Mainnet hash
    String blockHash = "958e9224c45f8f29fb534426d3c19aee02a6ff4b6414a89772af6480f97ea5a7";
    String txHash = "7514f13c2a263cd3ca38c3e04879c49e51fb56965ca3a3638f9ca1e9c9db9c32";
    String txInId = "30cb1f668959f96ca5100ffbb1c524507a2fc7f0bb9407b5fb581f2574eda322";
    AggregatedBlock aggregatedBlock = givenAggregatedBlock(false);
    Collection<TxOut> txOuts = givenNonByronTxOuts();
    AggregatedTx aggregatedTx = givenAggregatedTx(blockHash, txHash, true, true, txOuts);
    AggregatedTxIn aggregatedTxIn = givenTxIn(txInId, 0, null);
    TxOut txOutFromTxIn = givenTxOutFromTxIn(aggregatedTxIn);
    newTxOutMap.put(Pair.of(txInId, (short) 0), txOutFromTxIn);
    String assetFingerprint = "asset1yeavs0w2rd762k3s902hnwas3rzq9yqt8wv35a";
    MaTxOut maTxOut = givenMaTxOut(1L, assetFingerprint, BigInteger.ONE);
    newMaTxOutMap.put(1L, List.of(maTxOut));
    Tx tx = aggregatedTxToTx(aggregatedTx);
    AggregatedAddressBalance addressBalance =
        AggregatedAddressBalance.from(txOutFromTxIn.getAddress());
    Map<String, AggregatedAddressBalance> aggregatedAddressBalanceMap = new HashMap<>();
    Map<String, BigInteger> expectedAddressBalance = Map.of(
        "addr1qx6tzadessnknvgqpjjt390ly99ug6htxf4dw3yh29jqd00zz6d6dw64q4xxspqwx0sewrrw7s53muh5qrwqvuhpgc2qrn2sgr",
        BigInteger.valueOf(-1481480L)
    );
    Map<String, BigInteger> expectedAssetBalance = Map.of(assetFingerprint, BigInteger.valueOf(-1));
    aggregatedAddressBalanceMap.put(txOutFromTxIn.getAddress(), addressBalance);
    Map<String, Set<AggregatedTxIn>> txInMap =
        Map.of(aggregatedTx.getHash(), Set.of(aggregatedTxIn));
    Map<String, Tx> txMap = Map.of(tx.getHash(), tx);

    Mockito.when(blockDataService.getAggregatedBlock(blockHash)).thenReturn(aggregatedBlock);
    Mockito.when(txOutService.getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());
    Mockito.when(blockDataService
            .getAggregatedAddressBalanceFromAddress(txOutFromTxIn.getAddress()))
        .thenReturn(addressBalance);
    Mockito.when(multiAssetService.findAllByTxOutIn(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());

    victim.handleTxIns(List.of(aggregatedTx), txInMap, txMap,
        newTxOutMap, newMaTxOutMap, redeemersMap);

    Mockito.verify(blockDataService, Mockito.times(1))
        .getAggregatedAddressBalanceFromAddress(Mockito.anyString());
    Mockito.verify(blockDataService, Mockito.times(1))
        .getAggregatedBlock(Mockito.anyString());
    Mockito.verifyNoMoreInteractions(blockDataService);
    Mockito.verify(txOutService, Mockito.times(1))
        .getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txOutService);
    Mockito.verify(multiAssetService, Mockito.times(1))
        .findAllByTxOutIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(multiAssetService);
    Mockito.verify(txInRepository, Mockito.times(1))
        .saveAll(txInQueueCaptor.capture());
    Mockito.verifyNoMoreInteractions(txInRepository);
    Mockito.verifyNoInteractions(unconsumeTxInRepository);

    Assertions.assertEquals(BigInteger.valueOf(177645), tx.getFee());
    Assertions.assertEquals(100000L, tx.getDeposit());
    Queue<TxIn> txInQueue = txInQueueCaptor.getValue();
    TxIn txIn = txInQueue.remove();
    Assertions.assertEquals(txIn.getTxOut().getId(), txOutFromTxIn.getTx().getId());
    Assertions.assertEquals(txIn.getTxOutIndex(), txOutFromTxIn.getIndex());

    AggregatedAddressBalance aggregatedAddressBalance =
        aggregatedAddressBalanceMap.get(txOutFromTxIn.getAddress());
    Assertions.assertEquals(
        expectedAddressBalance.get(txOutFromTxIn.getAddress()),
        aggregatedAddressBalance.getTotalNativeBalance());
    Assertions.assertEquals(
        expectedAssetBalance.get(assetFingerprint),
        aggregatedAddressBalance.getMaBalances().get(Pair.of(txHash, assetFingerprint)).get()
    );
  }

  @Test
  @DisplayName("Should handle non-Byron tx ins with multi asset and redeemers in current batch successfully")
  void shouldHandleNonByronTxInsWithMultiAssetAndRedeemersInCurrentBatchSuccessfullyTest() {
    Map<Pair<String, Short>, TxOut> newTxOutMap = new ConcurrentHashMap<>();
    Map<Long, List<MaTxOut>> newMaTxOutMap = new ConcurrentHashMap<>();
    Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap = new ConcurrentHashMap<>();
    // Mainnet hash
    String blockHash = "958e9224c45f8f29fb534426d3c19aee02a6ff4b6414a89772af6480f97ea5a7";
    String txHash = "7514f13c2a263cd3ca38c3e04879c49e51fb56965ca3a3638f9ca1e9c9db9c32";
    String txInId = "30cb1f668959f96ca5100ffbb1c524507a2fc7f0bb9407b5fb581f2574eda322";
    Map<Pair<RedeemerTag, Integer>, Redeemer> redeemerInTxMap = redeemersMap
        .computeIfAbsent(txHash, unused -> new ConcurrentHashMap<>());
    int redeemerIdx = 0;
    redeemerInTxMap.put(Pair.of(RedeemerTag.Spend, redeemerIdx), Mockito.mock(Redeemer.class));
    AggregatedBlock aggregatedBlock = givenAggregatedBlock(false);
    Collection<TxOut> txOuts = givenNonByronTxOuts();
    AggregatedTx aggregatedTx = givenAggregatedTx(blockHash, txHash, true, true, txOuts);
    AggregatedTxIn aggregatedTxIn = givenTxIn(txInId, 0, redeemerIdx);
    TxOut txOutFromTxIn = givenTxOutFromTxIn(aggregatedTxIn);
    newTxOutMap.put(Pair.of(txInId, (short) 0), txOutFromTxIn);
    String assetFingerprint = "asset1yeavs0w2rd762k3s902hnwas3rzq9yqt8wv35a";
    MaTxOut maTxOut = givenMaTxOut(1L, assetFingerprint, BigInteger.ONE);
    newMaTxOutMap.put(1L, List.of(maTxOut));
    Tx tx = aggregatedTxToTx(aggregatedTx);
    AggregatedAddressBalance addressBalance =
        AggregatedAddressBalance.from(txOutFromTxIn.getAddress());
    Map<String, AggregatedAddressBalance> aggregatedAddressBalanceMap = new HashMap<>();
    Map<String, BigInteger> expectedAddressBalance = Map.of(
        "addr1qx6tzadessnknvgqpjjt390ly99ug6htxf4dw3yh29jqd00zz6d6dw64q4xxspqwx0sewrrw7s53muh5qrwqvuhpgc2qrn2sgr",
        BigInteger.valueOf(-1481480L)
    );
    Map<String, BigInteger> expectedAssetBalance = Map.of(assetFingerprint, BigInteger.valueOf(-1));
    aggregatedAddressBalanceMap.put(txOutFromTxIn.getAddress(), addressBalance);
    Map<String, Set<AggregatedTxIn>> txInMap =
        Map.of(aggregatedTx.getHash(), Set.of(aggregatedTxIn));
    Map<String, Tx> txMap = Map.of(tx.getHash(), tx);

    Mockito.when(blockDataService.getAggregatedBlock(blockHash)).thenReturn(aggregatedBlock);
    Mockito.when(txOutService.getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());
    Mockito.when(blockDataService
            .getAggregatedAddressBalanceFromAddress(txOutFromTxIn.getAddress()))
        .thenReturn(addressBalance);
    Mockito.when(multiAssetService.findAllByTxOutIn(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());

    victim.handleTxIns(List.of(aggregatedTx), txInMap, txMap,
        newTxOutMap, newMaTxOutMap, redeemersMap);

    Mockito.verify(blockDataService, Mockito.times(1))
        .getAggregatedAddressBalanceFromAddress(Mockito.anyString());
    Mockito.verify(blockDataService, Mockito.times(1))
        .getAggregatedBlock(Mockito.anyString());
    Mockito.verifyNoMoreInteractions(blockDataService);
    Mockito.verify(txOutService, Mockito.times(1))
        .getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txOutService);
    Mockito.verify(multiAssetService, Mockito.times(1))
        .findAllByTxOutIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(multiAssetService);
    Mockito.verify(txInRepository, Mockito.times(1))
        .saveAll(txInQueueCaptor.capture());
    Mockito.verifyNoMoreInteractions(txInRepository);
    Mockito.verifyNoInteractions(unconsumeTxInRepository);

    Assertions.assertEquals(BigInteger.valueOf(177645), tx.getFee());
    Assertions.assertEquals(100000L, tx.getDeposit());
    Queue<TxIn> txInQueue = txInQueueCaptor.getValue();
    TxIn txIn = txInQueue.remove();
    Assertions.assertEquals(txIn.getTxOut().getId(), txOutFromTxIn.getTx().getId());
    Assertions.assertEquals(txIn.getTxOutIndex(), txOutFromTxIn.getIndex());
    Assertions.assertNotNull(txIn.getRedeemer());

    AggregatedAddressBalance aggregatedAddressBalance =
        aggregatedAddressBalanceMap.get(txOutFromTxIn.getAddress());
    Assertions.assertEquals(
        expectedAddressBalance.get(txOutFromTxIn.getAddress()),
        aggregatedAddressBalance.getTotalNativeBalance());
    Assertions.assertEquals(
        expectedAssetBalance.get(assetFingerprint),
        aggregatedAddressBalance.getMaBalances().get(Pair.of(txHash, assetFingerprint)).get()
    );
  }

  @Test
  @DisplayName("Should skip unconsumed tx in handling if no unconsumed tx ins supplied")
  void shouldSkipUnconsumedTxInHandlingTest() {
    victim.handleUnconsumeTxIn(
        Collections.emptyMap(), Collections.emptyMap(),
        Collections.emptyMap(), Collections.emptyMap());

    Mockito.verifyNoInteractions(blockDataService);
    Mockito.verifyNoInteractions(txOutService);
    Mockito.verifyNoInteractions(multiAssetService);
    Mockito.verifyNoInteractions(txInRepository);
    Mockito.verifyNoInteractions(unconsumeTxInRepository);
  }

  @Test
  @DisplayName("Should handle unconsumed tx in successfully")
  void shouldHandleUnconsumedTxInsSuccessfullyTest() {
    // Mainnet hash
    String txHash = "3dd395a1802629b71c7ca385bef72eb5da37d7876b12d8d5decf8f425dff87f3";
    String txInId = "f61ac94ddc07a3fa54f116a68c78ab7aa6edfb79b67e7ea5de44de2d8ea3b88e";
    int txOutIdx = 0;
    Tx tx = Mockito.mock(Tx.class);
    AggregatedTxIn aggregatedTxIn = givenTxIn(txInId, txOutIdx, null);
    TxOut txOutFromTxIn = givenTxOutFromTxIn(aggregatedTxIn);

    Map<String, Set<AggregatedTxIn>> unconsumedTxInMap = new ConcurrentHashMap<>();
    Map<Pair<String, Short>, TxOut> newTxOutMap = Collections.emptyMap();
    Map<String, Tx> txMap = new ConcurrentHashMap<>();
    Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap = Collections.emptyMap();
    unconsumedTxInMap.put(txHash, Set.of(aggregatedTxIn));
    txMap.put(txHash, tx);

    Mockito.when(txOutService.getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection()))
        .thenReturn(List.of(txOutFromTxIn));

    victim.handleUnconsumeTxIn(unconsumedTxInMap, newTxOutMap, txMap, redeemersMap);

    Mockito.verifyNoInteractions(blockDataService);
    Mockito.verify(txOutService, Mockito.times(1))
        .getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txOutService);
    Mockito.verifyNoInteractions(multiAssetService);
    Mockito.verifyNoInteractions(txInRepository);
    Mockito.verify(unconsumeTxInRepository, Mockito.times(1))
        .saveAll(unconsumeTxInsCaptor.capture());
    Mockito.verifyNoMoreInteractions(unconsumeTxInRepository);

    List<UnconsumeTxIn> unconsumeTxIns = unconsumeTxInsCaptor.getValue();
    UnconsumeTxIn unconsumeTxIn = unconsumeTxIns.get(0);
    Assertions.assertEquals(
        txOutFromTxIn.getTx().getId(),
        unconsumeTxIn.getTxOut().getId());
    Assertions.assertEquals(txOutFromTxIn.getIndex(), unconsumeTxIn.getTxOutIndex());
    Assertions.assertEquals(tx, unconsumeTxIn.getTxIn());
  }

  @Test
  @DisplayName("Should handle unconsumed tx in w with tx out in current batch successfully")
  void shouldHandleUnconsumedTxInsWithTxOutInCurrentBatchSuccessfullyTest() {
    // Mainnet hash
    String txHash = "3dd395a1802629b71c7ca385bef72eb5da37d7876b12d8d5decf8f425dff87f3";
    String txInId = "f61ac94ddc07a3fa54f116a68c78ab7aa6edfb79b67e7ea5de44de2d8ea3b88e";
    int txOutIdx = 0;
    Tx tx = Mockito.mock(Tx.class);
    AggregatedTxIn aggregatedTxIn = givenTxIn(txInId, txOutIdx, 1);
    TxOut txOutFromTxIn = givenTxOutFromTxIn(aggregatedTxIn);

    Map<String, Set<AggregatedTxIn>> unconsumedTxInMap = new ConcurrentHashMap<>();
    Map<Pair<String, Short>, TxOut> newTxOutMap = new ConcurrentHashMap<>();
    Map<String, Tx> txMap = new ConcurrentHashMap<>();
    unconsumedTxInMap.put(txHash, Set.of(aggregatedTxIn));
    txMap.put(txHash, tx);

    newTxOutMap.put(Pair.of(txInId, (short) 0), txOutFromTxIn);

    Mockito.when(txOutService.getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());
    Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap = Collections.emptyMap();

    victim.handleUnconsumeTxIn(unconsumedTxInMap, newTxOutMap, txMap, redeemersMap);

    Mockito.verifyNoInteractions(blockDataService);
    Mockito.verify(txOutService, Mockito.times(1))
        .getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txOutService);
    Mockito.verifyNoInteractions(multiAssetService);
    Mockito.verifyNoInteractions(txInRepository);
    Mockito.verify(unconsumeTxInRepository, Mockito.times(1))
        .saveAll(unconsumeTxInsCaptor.capture());
    Mockito.verifyNoMoreInteractions(unconsumeTxInRepository);

    List<UnconsumeTxIn> unconsumeTxIns = unconsumeTxInsCaptor.getValue();
    UnconsumeTxIn unconsumeTxIn = unconsumeTxIns.get(0);
    Assertions.assertEquals(
        txOutFromTxIn.getTx().getId(),
        unconsumeTxIn.getTxOut().getId());
    Assertions.assertEquals(txOutFromTxIn.getIndex(), unconsumeTxIn.getTxOutIndex());
    Assertions.assertEquals(tx, unconsumeTxIn.getTxIn());
  }

  @Test
  @DisplayName("Should fail if tx out from unconsumed tx in not found")
  void shouldFailIfTxOutFromUnconsumedTxInNotFoundTest() {
    // Mainnet hash
    String txHash = "3dd395a1802629b71c7ca385bef72eb5da37d7876b12d8d5decf8f425dff87f3";
    String txInId = "f61ac94ddc07a3fa54f116a68c78ab7aa6edfb79b67e7ea5de44de2d8ea3b88e";
    int txOutIdx = 0;
    Tx tx = Mockito.mock(Tx.class);
    AggregatedTxIn aggregatedTxIn = givenTxIn(txInId, txOutIdx, 1);

    Map<String, Set<AggregatedTxIn>> unconsumedTxInMap = new ConcurrentHashMap<>();
    Map<Pair<String, Short>, TxOut> newTxOutMap = Collections.emptyMap();
    Map<String, Tx> txMap = new ConcurrentHashMap<>();
    Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap = new ConcurrentHashMap<>();
    unconsumedTxInMap.put(txHash, Set.of(aggregatedTxIn));
    txMap.put(txHash, tx);
    Map<Pair<RedeemerTag, Integer>, Redeemer> redeemerInTxMap = redeemersMap
            .computeIfAbsent(txHash, unused -> new ConcurrentHashMap<>());
    redeemerInTxMap.put(Pair.of(RedeemerTag.Spend, 1), Mockito.mock(Redeemer.class));
    Mockito.when(txOutService.getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());

    Assertions.assertThrows(IllegalStateException.class,
        () -> victim.handleUnconsumeTxIn(unconsumedTxInMap, newTxOutMap, txMap, redeemersMap));

    Mockito.verifyNoInteractions(blockDataService);
    Mockito.verify(txOutService, Mockito.times(1))
        .getTxOutCanUseByAggregatedTxIns(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txOutService);
    Mockito.verifyNoInteractions(multiAssetService);
    Mockito.verifyNoInteractions(txInRepository);
    Mockito.verifyNoInteractions(unconsumeTxInRepository);
  }

  private static AggregatedTx givenAggregatedTx(String blockHash, String txHash,
      boolean shouldHaveWithdrawals, boolean shouldHaveFee, Collection<TxOut> txOuts) {
    // Mainnet hash
    AggregatedTxBuilder aggregatedTxBuilder = AggregatedTx.builder();

    BigInteger outSum = BigInteger.ZERO;
    if (!CollectionUtils.isEmpty(txOuts)) {
      outSum = txOuts.stream()
          .map(TxOut::getValue)
          .reduce(BigInteger.ZERO, BigInteger::add);
    }

    return aggregatedTxBuilder
        .hash(txHash)
        .blockHash(blockHash)
        .fee(shouldHaveFee ? BigInteger.valueOf(177645) : BigInteger.ZERO)
        .withdrawals(shouldHaveWithdrawals ? givenWithdrawals() : Collections.emptyMap())
        .outSum(outSum)
        .build();
  }

  private static AggregatedTxIn givenTxIn(String txId, int index, Integer redeemerIdx) {
    return new AggregatedTxIn(index, txId, redeemerIdx);
  }

  private static Tx aggregatedTxToTx(AggregatedTx aggregatedTx) {
    return Tx.builder()
        .hash(aggregatedTx.getHash())
        .fee(aggregatedTx.getFee())
        .outSum(aggregatedTx.getOutSum())
        .build();
  }

  private static Map<String, BigInteger> givenWithdrawals() {
    String stakeKey = TestStringUtils.generateRandomHash(STAKE_KEY_HASH_LENGTH);
    return Map.of(stakeKey, BigInteger.valueOf(1325026557L));
  }

  private static AggregatedBlock givenAggregatedBlock(boolean shouldBeByron) {
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);

    Mockito.when(aggregatedBlock.getEra())
        .thenReturn(shouldBeByron ? Era.BYRON : Era.SHELLEY);

    return aggregatedBlock;
  }

  private static TxOut givenTxOutFromTxIn(AggregatedTxIn txIn) {
    TxBuilder<?, ?> txBuilder = Tx.builder();

    TxOut givenTxOut = txOutMap.get(Pair.of(txIn.getTxId(), (short) txIn.getIndex()));
    txBuilder.hash(txIn.getTxId());
    txBuilder.id(new Random().nextLong());

    return givenTxOut(givenTxOut.getId(), givenTxOut.getAddress(),
        givenTxOut.getIndex(), givenTxOut.getValue(), txBuilder.build());
  }

  // From mainnet
  private static Collection<TxOut> givenByronTxOuts() {
    TxOut txOut = givenTxOut(0L,
        "DdzFFzCqrhswZyPjeXBCxsmZ9GWDbVPU8Feq4pVZyYUH7pUqHWRPRDw7iZUZZGbbEohBsTwYGheT3cXzXiHR9mc64iokeFe8rAiin6hs",
        0, BigInteger.valueOf(384501828930L), null);
    TxOut txOut2 = givenTxOut(1L,
        "DdzFFzCqrht9bTEy9YDJgx88x64wRxFL6epX1iYJbcesK8qWzKHQWtDzzGSaMFsXbUV2ZXbZ4ZVKT6CAjXSyV2bNTVGmUnAs6SPqRu8h",
        1, BigInteger.valueOf(400000000L), null);
    return List.of(txOut, txOut2);
  }

  private static Collection<TxOut> givenNonByronTxOuts() {
    TxOut txOut = givenTxOut(0L,
        "addr1q8fxf72nxsk2f00wmkkew8r44g4rzqlq7r3jxdvxwrgzlrsxpl5q589yd5pjwz20exd5ra0dg2nk352f0nr8dp3yzq9sl4fjv7",
        0, BigInteger.valueOf(1481480L), null);
    TxOut txOut2 = givenTxOut(1L,
        "addr1q8338thk3hw9h23g8ahkqcvkksa62qqcpmr27mahahu8u6hzz6d6dw64q4xxspqwx0sewrrw7s53muh5qrwqvuhpgc2qff00f9",
        1, BigInteger.valueOf(1324748912L), null);
    return List.of(txOut, txOut2);
  }

  private static TxOut givenTxOut(long id, String address, int index, BigInteger value, Tx tx) {
    return TxOut.builder()
        .tx(tx)
        .id(id)
        .address(address)
        .index((short) index)
        .value(value)
        .build();
  }

  private static MaTxOut givenMaTxOut(long txOutId, String fingerprint, BigInteger quantity) {
    return MaTxOut.builder()
        .txOut(TxOut.builder().id(txOutId).build())
        .ident(MultiAsset.builder().fingerprint(fingerprint).build())
        .quantity(quantity)
        .build();
  }
}
