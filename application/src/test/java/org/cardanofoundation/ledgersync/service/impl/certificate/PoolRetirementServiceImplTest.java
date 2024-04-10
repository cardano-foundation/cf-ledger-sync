package org.cardanofoundation.ledgersync.service.impl.certificate;

import com.bloxbean.cardano.yaci.core.model.certs.PoolRetirement;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.consumercommon.entity.PoolHash;
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
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PoolRetirementServiceImplTest {

  @Mock
  BatchCertificateDataService batchCertificateDataService;

  PoolRetirementServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new PoolRetirementServiceImpl(batchCertificateDataService);
  }

  @Test
  @DisplayName("Should handle pool retirement certificate data successfully")
  void shouldHandlePoolRetirementCertificateDataSuccessfullyTest() {
    PoolRetirement poolRetirement = Mockito.mock(PoolRetirement.class);
    Tx tx = Mockito.mock(Tx.class);

    Mockito.when(poolRetirement.getPoolKeyHash())
        .thenReturn("2bdbd083f0890ad3700bc9b7b3be010cafdf64e3bf34a311a874eb29");
    Mockito.when(poolRetirement.getEpoch()).thenReturn(30L);
    Mockito.when(batchCertificateDataService.findPoolHashByHashRaw(Mockito.anyString()))
        .thenReturn(Optional.of(Mockito.mock(PoolHash.class)));

    Assertions.assertDoesNotThrow(() ->
        victim.handle(null, poolRetirement, 0, tx, null, Collections.emptyMap()));
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .findPoolHashByHashRaw(Mockito.anyString());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolHash(Mockito.any());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolRetire(Mockito.any());
    Mockito.verifyNoMoreInteractions(batchCertificateDataService);
  }

  @Test
  @DisplayName("Should fail on pool hash not found")
  void shouldFailOnPoolHashNotFoundTest() {
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);
    PoolRetirement poolRetirement = Mockito.mock(PoolRetirement.class);
    Tx tx = Mockito.mock(Tx.class);

    Mockito.when(poolRetirement.getPoolKeyHash())
        .thenReturn("2bdbd083f0890ad3700bc9b7b3be010cafdf64e3bf34a311a874eb29");
    Mockito.when(batchCertificateDataService.findPoolHashByHashRaw(Mockito.anyString()))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(IllegalStateException.class, () ->
        victim.handle(aggregatedBlock, poolRetirement, 0, tx, null, null));
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .findPoolHashByHashRaw(Mockito.anyString());
    Mockito.verifyNoMoreInteractions(batchCertificateDataService);
  }
}
