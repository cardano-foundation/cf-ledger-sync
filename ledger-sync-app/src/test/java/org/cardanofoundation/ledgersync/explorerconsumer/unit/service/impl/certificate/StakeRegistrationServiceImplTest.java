package org.cardanofoundation.ledgersync.explorerconsumer.unit.service.impl.certificate;

import java.util.Map;

import org.cardanofoundation.explorer.consumercommon.entity.StakeAddress;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.common.common.certs.StakeCredential;
import org.cardanofoundation.ledgersync.common.common.certs.StakeCredentialType;
import org.cardanofoundation.ledgersync.common.common.certs.StakeRegistration;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.service.BatchCertificateDataService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.certificate.StakeRegistrationServiceImpl;
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
        StakeCredentialType.ADDR_KEYHASH, stakeKeyHash);
    StakeRegistration stakeRegistration = new StakeRegistration();
    stakeRegistration.setStakeCredential(credential);
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