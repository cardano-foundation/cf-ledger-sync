package org.cardanofoundation.ledgersync.explorerconsumer.unit.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import org.cardanofoundation.explorer.consumercommon.entity.StakeAddress;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.StakeAddressRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.StakeAddressServiceImpl;
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
class StakeAddressServiceImplTest {

  @Mock
  StakeAddressRepository stakeAddressRepository;

  @Captor
  ArgumentCaptor<List<StakeAddress>> stakeAddressesCaptor;

  StakeAddressServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new StakeAddressServiceImpl(stakeAddressRepository);
  }

  @Test
  @DisplayName("Should not save anything when stake address is not supplied")
  void shouldSaveNothingTest() {
    Map<String, String> stakeAddressTxHashMap = Collections.emptyMap();
    Map<String, Tx> txMap = Collections.emptyMap();

    Map<String, StakeAddress> stakeAddressMap =
        victim.handleStakeAddressesFromTxs(stakeAddressTxHashMap, txMap);
    Assertions.assertTrue(CollectionUtils.isEmpty(stakeAddressMap));
    Mockito.verify(stakeAddressRepository, Mockito.times(1))
        .saveAll(stakeAddressesCaptor.capture());
    Mockito.verifyNoMoreInteractions(stakeAddressRepository);

    List<StakeAddress> stakeAddressesForSaving = stakeAddressesCaptor.getValue();
    Assertions.assertTrue(CollectionUtils.isEmpty(stakeAddressesForSaving));
  }

  @Test
  @DisplayName("Should create new stake addresses successfully")
  void shouldCreateNewStakeAddressesSuccessfullyTest() {
    String txHash = "a3d6f2627a56fe7921eeda546abfe164321881d41549b7f2fbf09ea0b718d758";
    Map<String, String> stakeAddressTxHashMap = givenStakeAddressTxHashMap(txHash);
    Map<String, Tx> txMap = Map.of(txHash, new Tx());

    Mockito.when(stakeAddressRepository.findByHashRawIn(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());

    Map<String, StakeAddress> stakeAddressMap =
        victim.handleStakeAddressesFromTxs(stakeAddressTxHashMap, txMap);
    Assertions.assertFalse(CollectionUtils.isEmpty(stakeAddressMap));
    Assertions.assertEquals(4, stakeAddressMap.size());

    Mockito.verify(stakeAddressRepository, Mockito.times(1))
        .findByHashRawIn(Mockito.anyCollection());
    Mockito.verify(stakeAddressRepository, Mockito.times(1))
        .saveAll(stakeAddressesCaptor.capture());
    Mockito.verifyNoMoreInteractions(stakeAddressRepository);

    List<StakeAddress> stakeAddressesForSaving = stakeAddressesCaptor.getValue();
    Assertions.assertFalse(CollectionUtils.isEmpty(stakeAddressesForSaving));
    Assertions.assertEquals(4, stakeAddressesForSaving.size());
  }

  @Test
  @DisplayName("Should create new stake addresses and also return existing stake addresses successfully")
  void shouldCreateNewStakeAddressesAndAlsoReturnExistingStakeAddressesSuccessfullyTest() {
    String txHash = "a3d6f2627a56fe7921eeda546abfe164321881d41549b7f2fbf09ea0b718d758";
    Map<String, String> stakeAddressTxHashMap = givenStakeAddressTxHashMap(txHash);
    List<StakeAddress> existingStakeAddresses = stakeAddressTxHashMap.keySet()
        .stream()
        .limit(2)
        .map(stakeKeyHash -> StakeAddress.builder().hashRaw(stakeKeyHash).build())
        .collect(Collectors.toList());
    Map<String, Tx> txMap = Map.of(txHash, new Tx());

    Mockito.when(stakeAddressRepository.findByHashRawIn(Mockito.anyCollection()))
        .thenReturn(existingStakeAddresses);

    Map<String, StakeAddress> stakeAddressMap =
        victim.handleStakeAddressesFromTxs(stakeAddressTxHashMap, txMap);
    Assertions.assertFalse(CollectionUtils.isEmpty(stakeAddressMap));
    Assertions.assertEquals(4, stakeAddressMap.size());

    Mockito.verify(stakeAddressRepository, Mockito.times(1))
        .findByHashRawIn(Mockito.anyCollection());
    Mockito.verify(stakeAddressRepository, Mockito.times(1))
        .saveAll(stakeAddressesCaptor.capture());
    Mockito.verifyNoMoreInteractions(stakeAddressRepository);

    List<StakeAddress> stakeAddressesForSaving = stakeAddressesCaptor.getValue();
    Assertions.assertFalse(CollectionUtils.isEmpty(stakeAddressesForSaving));
    Assertions.assertEquals(2, stakeAddressesForSaving.size());
  }

  private static Map<String, String> givenStakeAddressTxHashMap(String txHash) {
    List<String> stakeAddresses = List.of(
        "f0001f75da350c69845231fa588e6cb62f8f052ab9e3854081992fa98a",
        "e0acd71b5584acbd18cb1132f7923656cae9d05cfae08e75bd4d508dc2",
        "f000698856af634b64785bba57dee5a882c1a202d9a4f3b7772cede5d6",
        "e0392ae9e068e55e8b5c27acc58b0bab8ea568c0aae2f6fc49be23a7ad"
    );
    return stakeAddresses.stream().collect(Collectors.toMap(Function.identity(), unused -> txHash));
  }
}
