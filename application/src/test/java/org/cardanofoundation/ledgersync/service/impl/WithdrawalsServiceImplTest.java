package org.cardanofoundation.ledgersync.service.impl;

import com.bloxbean.cardano.client.plutus.spec.RedeemerTag;
import org.cardanofoundation.ledgersync.consumercommon.entity.Redeemer;
import org.cardanofoundation.ledgersync.consumercommon.entity.StakeAddress;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.repository.WithdrawalRepository;
import org.cardanofoundation.ledgersync.util.TestStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ExtendWith(MockitoExtension.class)
class WithdrawalsServiceImplTest {

  private static final int TX_HASH_LENGTH = 64;
  private static final int STAKE_KEY_HASH_LENGTH = 56;

  @Mock
  WithdrawalRepository withdrawalRepository;

  WithdrawalsServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new WithdrawalsServiceImpl(withdrawalRepository);
  }

  @Test
  @DisplayName("Should skip withdrawals handling if no withdrawals were supplied")
  void shouldSkipWithdrawalsHandlingTest() {
    Collection<AggregatedTx> aggregatedTxs = Collections.emptyList();
    Map<String, Tx> txMap = Collections.emptyMap();
    Map<String, StakeAddress> stakeAddressMap = Collections.emptyMap();
    Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap = Collections.emptyMap();

    victim.handleWithdrawal(aggregatedTxs, txMap, stakeAddressMap, redeemersMap);

    Mockito.verifyNoInteractions(withdrawalRepository);
  }

  @Test
  @DisplayName("Should fail if cannot find reward address")
  void shouldFailIfCannotFindRewardAddressTest() {
    Collection<AggregatedTx> aggregatedTxs = new ArrayList<>();
    Map<String, Tx> txMap = new HashMap<>();
    Map<String, StakeAddress> stakeAddressMap = Collections.emptyMap();
    Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap = Collections.emptyMap();

    IntStream.range(0, 1).forEach(i -> {
      String txHash = TestStringUtils.generateRandomHash(TX_HASH_LENGTH);
      AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
      Map<String, StakeAddress> givenStakeAddressMap = givenStakeAddressMap(2);
      Map<String, BigInteger> withdrawals = givenWithdrawals(givenStakeAddressMap.keySet());

      Mockito.when(aggregatedTx.getHash()).thenReturn(txHash);
      Mockito.when(aggregatedTx.getWithdrawals()).thenReturn(withdrawals);

      aggregatedTxs.add(aggregatedTx);
    });

    Assertions.assertThrows(IllegalStateException.class, () ->
        victim.handleWithdrawal(aggregatedTxs, txMap, stakeAddressMap, redeemersMap));

    Mockito.verifyNoInteractions(withdrawalRepository);
  }

  @Test
  @DisplayName("Should handle withdrawals successfully")
  void shouldHandleWithdrawalsSuccessfullyTest() {
    Collection<AggregatedTx> aggregatedTxs = new ArrayList<>();
    Map<String, Tx> txMap = new HashMap<>();
    Map<String, StakeAddress> stakeAddressMap = givenStakeAddressMap(10);
    Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap = Collections.emptyMap();

    IntStream.range(0, 10).forEach(i -> {
      String txHash = TestStringUtils.generateRandomHash(TX_HASH_LENGTH);
      txMap.put(txHash, new Tx());

      AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
      Map<String, BigInteger> withdrawals = givenWithdrawals(stakeAddressMap.keySet());

      Mockito.when(aggregatedTx.getHash()).thenReturn(txHash);
      Mockito.when(aggregatedTx.getWithdrawals()).thenReturn(withdrawals);

      aggregatedTxs.add(aggregatedTx);
    });

    victim.handleWithdrawal(aggregatedTxs, txMap, stakeAddressMap, redeemersMap);

    Mockito.verify(withdrawalRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(withdrawalRepository);
  }

  private static Map<String, BigInteger> givenWithdrawals(Collection<String> rewardAddresses) {
    return rewardAddresses.stream()
        .collect(Collectors.toMap(
            Function.identity(),
            unused -> BigInteger.ZERO
        ));
  }

  private static Map<String, StakeAddress> givenStakeAddressMap(int stakeAddressCount) {
    return IntStream.range(0, stakeAddressCount)
        .boxed()
        .collect(Collectors.toMap(
            unused -> TestStringUtils.generateRandomHash(STAKE_KEY_HASH_LENGTH),
            unused -> new StakeAddress()
        ));
  }
}
