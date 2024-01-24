package org.cardanofoundation.ledgersync.service.impl.certificate;

import com.bloxbean.cardano.yaci.core.model.certs.StakeCredType;
import com.bloxbean.cardano.yaci.core.model.certs.StakeCredential;
import com.bloxbean.cardano.yaci.core.model.certs.StakeDelegation;
import com.bloxbean.cardano.yaci.core.model.certs.StakePoolId;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.consumercommon.entity.PoolHash;
import org.cardanofoundation.ledgersync.consumercommon.entity.StakeAddress;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.service.BatchCertificateDataService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class StakeDelegationServiceImplTest {

  @Mock
  BatchCertificateDataService batchCertificateDataService;

  StakeDelegationServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new StakeDelegationServiceImpl(batchCertificateDataService);
  }

  @Test
  @DisplayName("Should handle stake delegation certificate successfully")
  void shouldHandleStakeDelegationCertificateSuccessfullyTest() {
    Tx tx = Mockito.mock(Tx.class);
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);
    String stakeKeyHash = "968d1021ebd7178e1fb0e79676982825cabc779b653e1234d58ce3c6";
    StakeCredential credential = StakeCredential.builder()
            .type(StakeCredType.ADDR_KEYHASH)
            .hash(stakeKeyHash)
            .build();

    StakePoolId stakePoolId = new StakePoolId(
        HexUtil.decodeHexString("a57cbcb8ecdf24f469928da924b5bc6e4cbc3b57859577211a0daf6f"));
      StakeDelegation stakeDelegation = StakeDelegation.builder()
              .stakeCredential(credential)
              .stakePoolId(stakePoolId)
              .build();
    Map<String, StakeAddress> stakeAddressMap = Map.of(
        "e0" + stakeKeyHash, Mockito.mock(StakeAddress.class)
    );

    Mockito.when(aggregatedBlock.getNetwork()).thenReturn(1);
    Mockito.when(batchCertificateDataService.findPoolHashByHashRaw(Mockito.anyString()))
        .thenReturn(Optional.of(Mockito.mock(PoolHash.class)));

    Assertions.assertDoesNotThrow(() ->
        victim.handle(aggregatedBlock, stakeDelegation, 2, tx, null, stakeAddressMap));
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .findPoolHashByHashRaw(Mockito.anyString());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolHash(Mockito.any());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .saveDelegation(Mockito.any());
    Mockito.verifyNoMoreInteractions(batchCertificateDataService);
  }

  @Test
  @DisplayName("Should fail on stake address not found")
  void shouldFailOnStakeAddressNotFoundTest() {
    Tx tx = Mockito.mock(Tx.class);
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);
    String stakeKeyHash = "968d1021ebd7178e1fb0e79676982825cabc779b653e1234d58ce3c6";
    StakeCredential credential = new StakeCredential(
        StakeCredType.ADDR_KEYHASH, stakeKeyHash);
    StakeDelegation stakeDelegation = StakeDelegation.builder()
                    .stakeCredential(credential)
                            .build();
    Map<String, StakeAddress> stakeAddressMap = Collections.emptyMap();

    Mockito.when(aggregatedBlock.getNetwork()).thenReturn(1);

    Assertions.assertThrows(IllegalStateException.class, () ->
        victim.handle(aggregatedBlock, stakeDelegation, 2, tx, null, stakeAddressMap));
    Mockito.verifyNoInteractions(batchCertificateDataService);
  }

  @Test
  @DisplayName("Should fail on pool hash not found")
  void shouldFailOnPoolHashNotFoundTest() {
    Tx tx = Mockito.mock(Tx.class);
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);
    String stakeKeyHash = "968d1021ebd7178e1fb0e79676982825cabc779b653e1234d58ce3c6";
    StakeCredential credential = new StakeCredential(
        StakeCredType.ADDR_KEYHASH, stakeKeyHash);
    StakePoolId stakePoolId = new StakePoolId(
        HexUtil.decodeHexString("a57cbcb8ecdf24f469928da924b5bc6e4cbc3b57859577211a0daf6f"));
      StakeDelegation stakeDelegation = StakeDelegation.builder()
              .stakeCredential(credential)
              .stakePoolId(stakePoolId)
              .build();

    Map<String, StakeAddress> stakeAddressMap = Map.of(
        "e0" + stakeKeyHash, Mockito.mock(StakeAddress.class)
    );

    Mockito.when(aggregatedBlock.getNetwork()).thenReturn(1);
    Mockito.when(batchCertificateDataService.findPoolHashByHashRaw(Mockito.anyString()))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(IllegalStateException.class, () ->
        victim.handle(aggregatedBlock, stakeDelegation, 2, tx, null, stakeAddressMap));
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .findPoolHashByHashRaw(Mockito.anyString());
    Mockito.verifyNoMoreInteractions(batchCertificateDataService);
  }
}
