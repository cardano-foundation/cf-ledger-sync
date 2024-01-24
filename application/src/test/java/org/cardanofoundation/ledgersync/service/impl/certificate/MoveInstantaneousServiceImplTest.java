package org.cardanofoundation.ledgersync.service.impl.certificate;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.bloxbean.cardano.yaci.core.model.certs.MoveInstataneous;
import com.bloxbean.cardano.yaci.core.model.certs.StakeCredType;
import com.bloxbean.cardano.yaci.core.model.certs.StakeCredential;
import org.cardanofoundation.ledgersync.consumercommon.entity.Reserve;
import org.cardanofoundation.ledgersync.consumercommon.entity.StakeAddress;
import org.cardanofoundation.ledgersync.consumercommon.entity.Treasury;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.service.BatchCertificateDataService;
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
class MoveInstantaneousServiceImplTest {

  @Mock
  BatchCertificateDataService batchCertificateDataService;

  @Captor
  ArgumentCaptor<List<Treasury>> treasuryCaptor;

  @Captor
  ArgumentCaptor<List<Reserve>> reserveCaptor;

  MoveInstantaneousServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new MoveInstantaneousServiceImpl(batchCertificateDataService);
  }

  @Test
  @DisplayName("Should handle MIR pot transfer successfully")
  void shouldHandleMIRPotTransferSuccessfullyTest() {
    Tx tx = Mockito.mock(Tx.class);
    MoveInstataneous moveInstantaneousTreasury = Mockito.mock(MoveInstataneous.class);
    MoveInstataneous moveInstantaneousReserves = Mockito.mock(MoveInstataneous.class);

    Mockito.when(moveInstantaneousTreasury.isTreasury()).thenReturn(false);
    Mockito.when(moveInstantaneousReserves.isTreasury()).thenReturn(true);
    Mockito.when(moveInstantaneousTreasury.getAccountingPotCoin()).thenReturn(BigInteger.TEN);
    Mockito.when(moveInstantaneousReserves.getAccountingPotCoin()).thenReturn(BigInteger.TEN);

    victim.handle(null, moveInstantaneousTreasury, 0, tx, null, Collections.emptyMap());
    victim.handle(null, moveInstantaneousReserves, 1, tx, null, Collections.emptyMap());
    Mockito.verify(batchCertificateDataService, Mockito.times(2)).savePotTransfer(Mockito.any());
    Mockito.verifyNoMoreInteractions(batchCertificateDataService);
  }

  @Test
  @DisplayName("Should handle MIR rewards successfully")
  void shouldHandleMIRSuccessfullyTest() {
    Tx tx = Mockito.mock(Tx.class);
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);
    MoveInstataneous moveInstantaneousTreasury = Mockito.mock(MoveInstataneous.class);
    MoveInstataneous moveInstantaneousReserves = Mockito.mock(MoveInstataneous.class);
    StakeAddress stakeAddress = Mockito.mock(StakeAddress.class);
    StakeAddress stakeAddress2 = Mockito.mock(StakeAddress.class);
    String stakeKeyHash = "e0acd71b5584acbd18cb1132f7923656cae9d05cfae08e75bd4d508dc2";
    String stakeKeyHash2 = "e01be0283019dc5e612a9c186f2134d73a2c3d40cbad5aaa3600f166c5";
    Map<String, StakeAddress> stakeAddressMap = Map.of(
        stakeKeyHash, stakeAddress,
        stakeKeyHash2, stakeAddress2
    );
    Map<StakeCredential, BigInteger> stakeCredentialCoinMap = Map.of(
        buildKeyHashStakeCredential(stakeKeyHash.substring(2)),
        BigInteger.valueOf(123456),
        buildKeyHashStakeCredential(stakeKeyHash2.substring(2)),
        BigInteger.valueOf(234567)
    );
    Map<String, BigInteger> stakeAddressCoinMap = Map.of(
        stakeKeyHash, BigInteger.valueOf(123456),
        stakeKeyHash2, BigInteger.valueOf(234567)
    );

    Mockito.when(aggregatedBlock.getNetwork()).thenReturn(1); // preprod
    Mockito.when(moveInstantaneousTreasury.isTreasury()).thenReturn(false);
    Mockito.when(moveInstantaneousTreasury.getStakeCredentialCoinMap())
        .thenReturn(stakeCredentialCoinMap);
    Mockito.when(moveInstantaneousReserves.isTreasury()).thenReturn(true);
    Mockito.when(moveInstantaneousReserves.getStakeCredentialCoinMap())
        .thenReturn(stakeCredentialCoinMap);
    Mockito.when(stakeAddress.getHashRaw()).thenReturn(stakeKeyHash);
    Mockito.when(stakeAddress2.getHashRaw()).thenReturn(stakeKeyHash2);

    victim.handle(aggregatedBlock, moveInstantaneousTreasury, 0, tx, null, stakeAddressMap);
    victim.handle(aggregatedBlock, moveInstantaneousReserves, 1, tx, null, stakeAddressMap);
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .saveTreasuries(treasuryCaptor.capture());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .saveReserves(reserveCaptor.capture());
    Mockito.verifyNoMoreInteractions(batchCertificateDataService);

    List<Treasury> treasuries = treasuryCaptor.getValue();
    Assertions.assertEquals(2, treasuries.size());
    Assertions.assertEquals(
        stakeAddressCoinMap.get(treasuries.get(0).getAddr().getHashRaw()),
        treasuries.get(0).getAmount());
    Assertions.assertEquals(
        stakeAddressCoinMap.get(treasuries.get(1).getAddr().getHashRaw()),
        treasuries.get(1).getAmount());

    List<Reserve> reserves = reserveCaptor.getValue();
    Assertions.assertEquals(2, reserves.size());
    Assertions.assertEquals(
        stakeAddressCoinMap.get(reserves.get(0).getAddr().getHashRaw()),
        reserves.get(0).getAmount());
    Assertions.assertEquals(
        stakeAddressCoinMap.get(reserves.get(1).getAddr().getHashRaw()),
        reserves.get(1).getAmount());
  }

  private StakeCredential buildKeyHashStakeCredential(String hash) {
    return new StakeCredential(StakeCredType.ADDR_KEYHASH, hash);
  }
}
