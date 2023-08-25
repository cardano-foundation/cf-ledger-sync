package org.cardanofoundation.ledgersync.explorerconsumer.unit.service.impl.certificate;

import java.util.Collections;

import org.cardanofoundation.explorer.consumercommon.entity.Redeemer;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.common.common.certs.GenesisKeyDelegation;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.certificate.GenesisKeyDelegationServiceImpl;
import org.mockito.Mockito;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GenesisKeyDelegationServiceImplTest {

  @Test
  @DisplayName("No-op test")
  void noOpTest() {
    // This test is there just to add coverage
    GenesisKeyDelegationServiceImpl victim = new GenesisKeyDelegationServiceImpl();
    Assertions.assertDoesNotThrow(() -> victim.handle(
        Mockito.mock(AggregatedBlock.class),
        Mockito.mock(GenesisKeyDelegation.class),
        0,
        Mockito.mock(Tx.class),
        Mockito.mock(Redeemer.class),
        Collections.emptyMap()
    ));
  }
}
