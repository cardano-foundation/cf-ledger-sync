package org.cardanofoundation.ledgersync.explorerconsumer.unit.service.impl.certificate;

import java.util.Collections;
import java.util.Map;

import com.bloxbean.cardano.yaci.core.model.certs.StakeCredType;
import com.bloxbean.cardano.yaci.core.model.certs.StakeCredential;
import com.bloxbean.cardano.yaci.core.model.certs.StakeDeregistration;
import org.cardanofoundation.explorer.consumercommon.entity.StakeAddress;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.service.BatchCertificateDataService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.certificate.StakeDeregistrationServiceImpl;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class StakeDeregistrationServiceImplTest {

    @Mock
    BatchCertificateDataService batchCertificateDataService;

    StakeDeregistrationServiceImpl victim;

    @BeforeEach
    void setUp() {
        victim = new StakeDeregistrationServiceImpl(batchCertificateDataService);
    }

    @Test
    @DisplayName("Should handle stake de-registration certificate successfully")
    void shouldHandleStakeDeregistrationCertificateSuccessfullyTest() {
        Tx tx = Mockito.mock(Tx.class);
        AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);
        String stakeKeyHash = "c568341dc347876c1c79e07de3e76265560bca4bb9e6af9f36e40923";
        StakeCredential credential = new StakeCredential(
                StakeCredType.ADDR_KEYHASH, stakeKeyHash);
        StakeDeregistration stakeDeregistration = StakeDeregistration.builder()
                .stakeCredential(credential)
                .build();
        Map<String, StakeAddress> stakeAddressMap = Map.of(
                "e0" + stakeKeyHash, Mockito.mock(StakeAddress.class)
        );

        Mockito.when(aggregatedBlock.getNetwork()).thenReturn(1);

        Assertions.assertDoesNotThrow(() ->
                victim.handle(aggregatedBlock, stakeDeregistration, 0, tx, null, stakeAddressMap));
        Mockito.verify(batchCertificateDataService, Mockito.times(1))
                .saveStakeDeregistration(Mockito.any());
        Mockito.verifyNoMoreInteractions(batchCertificateDataService);
    }

    @Test
    @DisplayName("Should fail on stake address not found")
    void shouldFailOnStakeAddressNotFoundTest() {
        Tx tx = Mockito.mock(Tx.class);
        AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);
        String stakeKeyHash = "c568341dc347876c1c79e07de3e76265560bca4bb9e6af9f36e40923";
        StakeCredential credential = new StakeCredential(
                StakeCredType.ADDR_KEYHASH, stakeKeyHash);
        StakeDeregistration stakeDeregistration = StakeDeregistration.builder()
                .stakeCredential(credential)
                .build();
        Map<String, StakeAddress> stakeAddressMap = Collections.emptyMap();

        Mockito.when(aggregatedBlock.getNetwork()).thenReturn(1);

        Assertions.assertThrows(IllegalStateException.class, () ->
                victim.handle(aggregatedBlock, stakeDeregistration, 0, tx, null, stakeAddressMap));
        Mockito.verifyNoInteractions(batchCertificateDataService);
    }
}
