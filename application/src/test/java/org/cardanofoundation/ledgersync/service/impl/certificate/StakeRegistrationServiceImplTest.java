package org.cardanofoundation.ledgersync.service.impl.certificate;

import java.util.Map;

import com.bloxbean.cardano.yaci.core.model.certs.StakeCredType;
import com.bloxbean.cardano.yaci.core.model.certs.StakeCredential;
import com.bloxbean.cardano.yaci.core.model.certs.StakeRegistration;
import org.cardanofoundation.ledgersync.consumercommon.entity.StakeAddress;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.service.BatchCertificateDataService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class StakeRegistrationServiceImplTest {

  @Mock
  BatchCertificateDataService batchCertificateDataService;

  StakeRegistrationServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new StakeRegistrationServiceImpl(batchCertificateDataService);
  }

  @Test
  @DisplayName("Should handle stake registration certificate successfully")
  void shouldHandleStakeRegistrationCertificateSuccessfullyTest() {
    Tx tx = Mockito.mock(Tx.class);
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);
    String stakeKeyHash = "c568341dc347876c1c79e07de3e76265560bca4bb9e6af9f36e40923";
    StakeCredential credential = new StakeCredential(
        StakeCredType.ADDR_KEYHASH, stakeKeyHash);
    StakeRegistration stakeRegistration = StakeRegistration.builder()
            .stakeCredential(credential)
            .build();
    Map<String, StakeAddress> stakeAddressMap = Map.of(
        "e0" + stakeKeyHash, Mockito.mock(StakeAddress.class)
    );

    Mockito.when(aggregatedBlock.getNetwork()).thenReturn(1);

    Assertions.assertDoesNotThrow(() ->
        victim.handle(aggregatedBlock, stakeRegistration, 0, tx, null, stakeAddressMap));
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .saveStakeRegistration(Mockito.any());
    Mockito.verifyNoMoreInteractions(batchCertificateDataService);
  }
}
