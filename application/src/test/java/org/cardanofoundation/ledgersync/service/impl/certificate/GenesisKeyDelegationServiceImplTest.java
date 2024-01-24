package org.cardanofoundation.ledgersync.service.impl.certificate;

import java.util.Collections;

import com.bloxbean.cardano.yaci.core.model.certs.GenesisKeyDelegation;
import org.cardanofoundation.ledgersync.consumercommon.entity.Redeemer;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;
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
