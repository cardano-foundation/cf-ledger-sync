package org.cardanofoundation.ledgersync.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;

import org.cardanofoundation.ledgersync.consumercommon.entity.ReferenceTxIn;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxOut;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTxIn;
import org.cardanofoundation.ledgersync.repository.ReferenceInputRepository;
import org.cardanofoundation.ledgersync.service.TxOutService;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ReferenceInputServiceImplTest {

  @Mock
  ReferenceInputRepository referenceInputRepository;

  @Mock
  TxOutService txOutService;

  @Captor
  ArgumentCaptor<Collection<ReferenceTxIn>> referenceTxInsCaptor;

  @Captor
  ArgumentCaptor<Collection<AggregatedTxIn>> referenceInputsCaptor;

  ReferenceInputServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new ReferenceInputServiceImpl(referenceInputRepository, txOutService);
  }

  @Test
  @DisplayName("Should skip reference input saving if no reference inputs supplied")
  void shouldSkipReferenceInputSavingTest() {
    Map<String, Set<AggregatedTxIn>> referenceTxInMap = Collections.emptyMap();
    Map<String, Tx> txMap = Collections.emptyMap();
    Map<Pair<String, Short>, TxOut> newTxOutMap = Collections.emptyMap();

    victim.handleReferenceInputs(referenceTxInMap, txMap, newTxOutMap);

    Mockito.verify(txOutService, Mockito.times(1))
        .getTxOutCanUseByAggregatedTxIns(referenceInputsCaptor.capture());
    Mockito.verifyNoMoreInteractions(txOutService);
    Mockito.verify(referenceInputRepository, Mockito.times(1))
        .saveAll(referenceTxInsCaptor.capture());
    Mockito.verifyNoMoreInteractions(referenceInputRepository);

    Collection<AggregatedTxIn> referenceInputs = referenceInputsCaptor.getValue();
    Assertions.assertTrue(CollectionUtils.isEmpty(referenceInputs));

    Collection<ReferenceTxIn> referenceTxIns = referenceTxInsCaptor.getValue();
    Assertions.assertTrue(CollectionUtils.isEmpty(referenceTxIns));
  }

  @Test
  @DisplayName("Should handle reference inputs successfully")
  void shouldHandleReferenceInputSuccessfullyTest() {
    String txHash = "007f4409f067fc58c3c3cd1d44f7a9e418e5080e73173cc7db701cfb90c5f031";
    Set<AggregatedTxIn> aggregatedTxIns = givenAggregatedTxIns();
    Map<String, Set<AggregatedTxIn>> referenceTxInMap = Map.of(txHash, aggregatedTxIns);
    Map<String, Tx> txMap = Map.of(txHash, Mockito.mock(Tx.class));
    Map<Pair<String, Short>, TxOut> newTxOutMap = givenTxOutMapFromTxIn(aggregatedTxIns);

    Mockito.when(txOutService.getTxOutCanUseByAggregatedTxIns(Mockito.anySet()))
        .thenReturn(Collections.emptyList());

    victim.handleReferenceInputs(referenceTxInMap, txMap, newTxOutMap);

    Mockito.verify(referenceInputRepository, Mockito.times(1))
        .saveAll(referenceTxInsCaptor.capture());
    Mockito.verifyNoMoreInteractions(referenceInputRepository);

    Collection<ReferenceTxIn> referenceTxIns = referenceTxInsCaptor.getValue();
    Assertions.assertFalse(CollectionUtils.isEmpty(referenceTxIns));
    Assertions.assertEquals(6, referenceTxIns.size());
  }

  @Test
  @DisplayName("Should handle reference inputs with tx out from repository successfully")
  void shouldHandleReferenceInputWithTxOutFromRepositorySuccessfullyTest() {
    String txHash = "007f4409f067fc58c3c3cd1d44f7a9e418e5080e73173cc7db701cfb90c5f031";
    Set<AggregatedTxIn> aggregatedTxIns = givenAggregatedTxIns();
    Map<String, Set<AggregatedTxIn>> referenceTxInMap = Map.of(txHash, aggregatedTxIns);
    Map<String, Tx> txMap = Map.of(txHash, Mockito.mock(Tx.class));
    Map<Pair<String, Short>, TxOut> newTxOutMap = Collections.emptyMap();

    Map<Pair<String, Short>, TxOut> givenTxOutMap = givenTxOutMapFromTxIn(aggregatedTxIns);
    Mockito.when(txOutService.getTxOutCanUseByAggregatedTxIns(Mockito.anySet()))
        .thenReturn(givenTxOutMap.values());

    victim.handleReferenceInputs(referenceTxInMap, txMap, newTxOutMap);

    Mockito.verify(referenceInputRepository, Mockito.times(1))
        .saveAll(referenceTxInsCaptor.capture());
    Mockito.verifyNoMoreInteractions(referenceInputRepository);

    Collection<ReferenceTxIn> referenceTxIns = referenceTxInsCaptor.getValue();
    Assertions.assertFalse(CollectionUtils.isEmpty(referenceTxIns));
    Assertions.assertEquals(6, referenceTxIns.size());
  }

  private static Set<AggregatedTxIn> givenAggregatedTxIns() {
    return Set.of(
        new AggregatedTxIn(0, "aa80303b33aeb356d7df1355fee6af948be362b4220a0d5ad5e96ae73d9560ba", null),
        new AggregatedTxIn(0, "ba7d913e1c92004ceaef0c20c3ca142ee397eab673c115bb35dfd4c447d94c07", null),
        new AggregatedTxIn(0, "5f3ea4ca0cf7d93432de946d7d5e2d78df3a7f6bc5e2e7ccfdf19995a4e3b5e9", null),
        new AggregatedTxIn(1, "cc784da3e4a1e04411c6c1c2abf1265218b208db39b19b5428ab6c384090d268", null),
        new AggregatedTxIn(0, "ad7b8068953574670b14e0b95027c2132413eaee829466889a96eb24fec1e1cc", null),
        new AggregatedTxIn(0, "06977fc2872331fb54510146aec1d2c367fa777208e3893cea2a2444991fbdb7", null)
    );
  }

  private static Map<Pair<String, Short>, TxOut> givenTxOutMapFromTxIn(Set<AggregatedTxIn> txIns) {
    return txIns.stream()
        .collect(Collectors.toMap(
            txIn -> Pair.of(txIn.getTxId(), (short) txIn.getIndex()),
            txIn -> {
              Tx tx = new Tx();
              tx.setHash(txIn.getTxId());

              TxOut txOut = new TxOut();
              txOut.setIndex((short) txIn.getIndex());
              txOut.setTx(tx);
              return txOut;
            }
        ));
  }
}
