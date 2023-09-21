package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bloxbean.cardano.yaci.core.model.ProtocolParamUpdate;
import com.bloxbean.cardano.yaci.core.model.Update;
import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;

import org.cardanofoundation.explorer.consumercommon.entity.ParamProposal;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.ParamProposalRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.CostModelService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.ParamProposalServiceImpl;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ParamProposalServiceImplTest {

  @Mock
  ParamProposalRepository paramProposalRepository;

  @Mock
  CostModelService costModelService;

  ParamProposalServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new ParamProposalServiceImpl(paramProposalRepository, costModelService);
  }

  @Test
  @DisplayName("Should skip param proposals handling if no protocol param update supplied")
  void shouldSkipParamProposalsHandlingTest() {
    AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
    Map<String, Tx> txMap = Collections.emptyMap();

    List<ParamProposal> paramProposals = victim.handleParamProposals(List.of(aggregatedTx), txMap);
    Assertions.assertTrue(CollectionUtils.isEmpty(paramProposals));
    Mockito.verifyNoInteractions(costModelService);
    Mockito.verifyNoInteractions(paramProposalRepository);
  }

  @Test
  @DisplayName("Should handle param proposals successfully")
  void shouldHandleParamProposalsSuccessfullyTest() {
    String txHash = "a00696a0c2d70c381a265a845e43c55e1d00f96b27c06defc015dc92eb206240";
    AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
    Map<String, Tx> txMap = Map.of(txHash, Mockito.mock(Tx.class));
    ProtocolParamUpdate protocolParamUpdate = ProtocolParamUpdate.builder()
        .protocolMajorVer(3)
        .protocolMinorVer(0)
        .costModelsHash("dffd1848e8ef26aadb1d4d05612825596ab697b27d2ea422fec077dd0de93254")
        .build();
    Map<String, ProtocolParamUpdate> protocolParamUpdateMap = buildProtocolParamUpdates(
        Pair.of("637f2e950b0fd8f8e3e811c5fbeb19e411e7a2bf37272b84b29c1a0b", protocolParamUpdate));
    Update update = new Update(protocolParamUpdateMap, 4);

    Mockito.when(aggregatedTx.getHash()).thenReturn(txHash);
    Mockito.when(aggregatedTx.getUpdate()).thenReturn(update);
    Mockito.when(paramProposalRepository.saveAll(Mockito.anyCollection()))
        .then(AdditionalAnswers.returnsFirstArg());

    List<ParamProposal> paramProposals = victim.handleParamProposals(List.of(aggregatedTx), txMap);
    Assertions.assertFalse(CollectionUtils.isEmpty(paramProposals));
    Mockito.verify(costModelService, Mockito.times(1)).handleCostModel(Mockito.any());
    Mockito.verify(costModelService, Mockito.times(1)).findCostModelByHash(Mockito.anyString());
    Mockito.verifyNoMoreInteractions(costModelService);
    Mockito.verify(paramProposalRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(paramProposalRepository);
  }


  @Test
  @DisplayName("Should handle param proposal key deposit successfully")
  void shouldHandleKeyDepositParamProposalsSuccessfullyTest() {
    String txHash = "a00696a0c2d70c381a265a845e43c55e1d00f96b27c06defc015dc92eb206240";
    AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
    Map<String, Tx> txMap = Map.of(txHash, Mockito.mock(Tx.class));
    ProtocolParamUpdate protocolParamUpdate = ProtocolParamUpdate.builder()
        .keyDeposit(BigInteger.TEN)
        .build();
    Map<String, ProtocolParamUpdate> protocolParamUpdateMap = buildProtocolParamUpdates(
        Pair.of("637f2e950b0fd8f8e3e811c5fbeb19e411e7a2bf37272b84b29c1a0b", protocolParamUpdate));
    Update update = new Update(protocolParamUpdateMap, 4);

    Mockito.when(aggregatedTx.getHash()).thenReturn(txHash);
    Mockito.when(aggregatedTx.getUpdate()).thenReturn(update);
    Mockito.when(paramProposalRepository.saveAll(Mockito.anyCollection()))
        .then(AdditionalAnswers.returnsFirstArg());

    List<ParamProposal> paramProposals = victim.handleParamProposals(List.of(aggregatedTx), txMap);
    Assertions.assertFalse(CollectionUtils.isEmpty(paramProposals));
    Assertions.assertEquals(paramProposals.get(0).getKeyDeposit(), BigInteger.TEN);
    Mockito.verify(paramProposalRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(paramProposalRepository);
  }

  @Test
  @DisplayName("Should handle param proposal key deposit is null successfully")
  void shouldHandleNullKeyDepositParamProposalsSuccessfullyTest() {
    String txHash = "a00696a0c2d70c381a265a845e43c55e1d00f96b27c06defc015dc92eb206240";
    AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
    Map<String, Tx> txMap = Map.of(txHash, Mockito.mock(Tx.class));
    ProtocolParamUpdate protocolParamUpdate = ProtocolParamUpdate.builder()
        .keyDeposit(null)
        .build();
    Map<String, ProtocolParamUpdate> protocolParamUpdateMap = buildProtocolParamUpdates(
        Pair.of("637f2e950b0fd8f8e3e811c5fbeb19e411e7a2bf37272b84b29c1a0b", protocolParamUpdate));
    Update update = new Update(protocolParamUpdateMap, 4);

    Mockito.when(aggregatedTx.getHash()).thenReturn(txHash);
    Mockito.when(aggregatedTx.getUpdate()).thenReturn(update);
    Mockito.when(paramProposalRepository.saveAll(Mockito.anyCollection()))
        .then(AdditionalAnswers.returnsFirstArg());

    List<ParamProposal> paramProposals = victim.handleParamProposals(List.of(aggregatedTx), txMap);
    Assertions.assertFalse(CollectionUtils.isEmpty(paramProposals));
    Assertions.assertEquals(paramProposals.get(0).getKeyDeposit(), null);
    Mockito.verify(paramProposalRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(paramProposalRepository);
  }


  @Test
  @DisplayName("Should handle param proposal max epoch is null successfully")
  void shouldHandleNullMaxEpochParamProposalsSuccessfullyTest() {
    String txHash = "a00696a0c2d70c381a265a845e43c55e1d00f96b27c06defc015dc92eb206240";
    AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
    Map<String, Tx> txMap = Map.of(txHash, Mockito.mock(Tx.class));
    ProtocolParamUpdate protocolParamUpdate = ProtocolParamUpdate.builder()
        .maxEpoch(null)
        .build();
    Map<String, ProtocolParamUpdate> protocolParamUpdateMap = buildProtocolParamUpdates(
        Pair.of("637f2e950b0fd8f8e3e811c5fbeb19e411e7a2bf37272b84b29c1a0b", protocolParamUpdate));
    Update update = new Update(protocolParamUpdateMap, 4);

    Mockito.when(aggregatedTx.getHash()).thenReturn(txHash);
    Mockito.when(aggregatedTx.getUpdate()).thenReturn(update);
    Mockito.when(paramProposalRepository.saveAll(Mockito.anyCollection()))
        .then(AdditionalAnswers.returnsFirstArg());

    List<ParamProposal> paramProposals = victim.handleParamProposals(List.of(aggregatedTx), txMap);
    Assertions.assertFalse(CollectionUtils.isEmpty(paramProposals));
    Assertions.assertEquals(paramProposals.get(0).getMaxEpoch(), null);
    Mockito.verify(paramProposalRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(paramProposalRepository);
  }

  @Test
  @DisplayName("Should handle param proposal max epoch successfully")
  void shouldHandleMaxEpochParamProposalsSuccessfullyTest() {
    String txHash = "a00696a0c2d70c381a265a845e43c55e1d00f96b27c06defc015dc92eb206240";
    AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
    Map<String, Tx> txMap = Map.of(txHash, Mockito.mock(Tx.class));
    ProtocolParamUpdate protocolParamUpdate = ProtocolParamUpdate.builder()
        .maxEpoch(BigInteger.TEN.intValue())
        .build();
    Map<String, ProtocolParamUpdate> protocolParamUpdateMap = buildProtocolParamUpdates(
        Pair.of("637f2e950b0fd8f8e3e811c5fbeb19e411e7a2bf37272b84b29c1a0b", protocolParamUpdate));
    Update update = new Update(protocolParamUpdateMap, 4);

    Mockito.when(aggregatedTx.getHash()).thenReturn(txHash);
    Mockito.when(aggregatedTx.getUpdate()).thenReturn(update);
    Mockito.when(paramProposalRepository.saveAll(Mockito.anyCollection()))
        .then(AdditionalAnswers.returnsFirstArg());

    List<ParamProposal> paramProposals = victim.handleParamProposals(List.of(aggregatedTx), txMap);
    Assertions.assertFalse(CollectionUtils.isEmpty(paramProposals));
    Assertions.assertEquals(paramProposals.get(0).getMaxEpoch(), BigInteger.TEN);
    Mockito.verify(paramProposalRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(paramProposalRepository);
  }


  @SafeVarargs
  private static Map<String, ProtocolParamUpdate> buildProtocolParamUpdates(
      Pair<String, ProtocolParamUpdate>...protocolParamUpdatePairs) {
    Map<String, ProtocolParamUpdate> protocolParamUpdateMap = new LinkedHashMap<>();

    for (Pair<String, ProtocolParamUpdate> protocolParamUpdatePair : protocolParamUpdatePairs) {
      String key = protocolParamUpdatePair.getFirst();
      ProtocolParamUpdate value = protocolParamUpdatePair.getSecond();
      protocolParamUpdateMap.put(key, value);
    }

    return protocolParamUpdateMap;
  }
}
