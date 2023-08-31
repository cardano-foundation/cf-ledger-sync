package org.cardanofoundation.ledgersync.explorerconsumer.unit.service.impl.certificate;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.bloxbean.cardano.yaci.core.model.PoolParams;
import com.bloxbean.cardano.yaci.core.model.Relay;
import com.bloxbean.cardano.yaci.core.model.certs.PoolRegistration;
import org.cardanofoundation.explorer.consumercommon.entity.PoolHash;
import org.cardanofoundation.explorer.consumercommon.entity.PoolMetadataRef;
import org.cardanofoundation.explorer.consumercommon.entity.PoolRelay;
import org.cardanofoundation.explorer.consumercommon.entity.PoolUpdate;
import org.cardanofoundation.explorer.consumercommon.entity.StakeAddress;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.service.BatchCertificateDataService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.certificate.PoolRegistrationServiceImpl;
import org.mockito.AdditionalAnswers;
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
class PoolRegistrationServiceImplTest {

  @Mock
  BatchCertificateDataService batchCertificateDataService;

  @Captor
  ArgumentCaptor<PoolUpdate> poolUpdateCaptor;

  @Captor
  ArgumentCaptor<List<PoolRelay>> poolRelaysCaptor;

  PoolRegistrationServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new PoolRegistrationServiceImpl(batchCertificateDataService);
  }

  @Test
  @DisplayName("Should handle brand new pool registration certificate data successfully")
  void shouldHandleBrandNewPoolRegistrationCertificateDataSuccessfullyTest() {
    String rewardAccount = "e0a6f3c57b3e463eb6cffb842b65197356e96b7cb59590a86f8a0b23f9";
    Map<String, StakeAddress> stakeAddressMap = Map.of(
        rewardAccount, Mockito.mock(StakeAddress.class)
    );
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);
    Tx tx = Mockito.mock(Tx.class);
    PoolParams poolParams = PoolParams.builder()
        .operator("74f5dd2551c2c0fd71aebb95e1f58d0b742c0fd2ff1712644f709bdc")
        .vrfKeyHash("7cc0d9a8931e434c5632d5a696a2c50483d7ed86f4f2e97e558f14e863a2ccbf")
        .pledge(BigInteger.valueOf(11111000000L))
        .cost(BigInteger.valueOf(777000000L))
        .margin("11/50")
        .rewardAccount(rewardAccount)
        .poolOwners(Set.of(
            rewardAccount.substring(2))) // pool owner is the same as reward account in this case
        .relays(List.of(
            Relay.builder()
                .port(3001)
                .dnsName("preprod-relay1.angelstakepool.net")
//                .relayType(RelayType.SINGLE_HOST_NAME) //TODO refactor check
                .build()))
        .poolMetadataUrl("https://angelstakepool.net/preprod/angel.json")
        .poolMetadataHash("bf44709dd714742688eeff2b6ca5573fe312a2e5f49d564c4c2311923c63952c")
        .build();
    PoolRegistration poolRegistration = PoolRegistration.builder()
        .poolParams(poolParams)
        .build();
   // poolRegistration.setIndex(1); //TODO refactor check

    Mockito.when(aggregatedBlock.getEpochNo()).thenReturn(1);
    Mockito.when(batchCertificateDataService.findPoolHashByHashRaw(Mockito.anyString()))
        .thenReturn(Optional.empty());
    Mockito.when(batchCertificateDataService.savePoolHash(Mockito.any()))
        .then(AdditionalAnswers.returnsFirstArg());
    Mockito.when(batchCertificateDataService
            .findPoolMetadataRefByPoolHashAndUrlAndHash(
                Mockito.any(), Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Optional.empty());
    Mockito.when(batchCertificateDataService.savePoolMetadataRef(Mockito.any()))
        .then(AdditionalAnswers.returnsFirstArg());
    Mockito.when(batchCertificateDataService.poolUpdateExistsByPoolHash(Mockito.any()))
        .thenReturn(Boolean.FALSE);

    victim.handle(aggregatedBlock, poolRegistration, 1, tx, null, stakeAddressMap);

    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .findPoolHashByHashRaw(Mockito.anyString());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolHash(Mockito.any());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .findPoolMetadataRefByPoolHashAndUrlAndHash(Mockito.any(), Mockito.anyString(),
            Mockito.anyString());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolMetadataRef(Mockito.any());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .poolUpdateExistsByPoolHash(Mockito.any());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolUpdate(poolUpdateCaptor.capture());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolOwners(Mockito.anyCollection());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolRelays(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(batchCertificateDataService);

    PoolUpdate poolUpdate = poolUpdateCaptor.getValue();
    Assertions.assertEquals(3, poolUpdate.getActiveEpochNo());
  }

  @Test
  @DisplayName("Should handle pool registration certificate with existing data successfully")
  void shouldHandlePoolRegistrationCertificateWithExistingDataSuccessfullyTest() {
    String rewardAccount = "e0a6f3c57b3e463eb6cffb842b65197356e96b7cb59590a86f8a0b23f9";
    Map<String, StakeAddress> stakeAddressMap = Map.of(
        rewardAccount, Mockito.mock(StakeAddress.class)
    );
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);
    Tx tx = Mockito.mock(Tx.class);
    PoolParams poolParams = PoolParams.builder()
        .operator("74f5dd2551c2c0fd71aebb95e1f58d0b742c0fd2ff1712644f709bdc")
        .vrfKeyHash("7cc0d9a8931e434c5632d5a696a2c50483d7ed86f4f2e97e558f14e863a2ccbf")
        .pledge(BigInteger.valueOf(11111000000L))
        .cost(BigInteger.valueOf(777000000L))
        .margin("11/50")
        .rewardAccount(rewardAccount)
        .poolOwners(Set.of(
            rewardAccount.substring(2))) // pool owner is the same as reward account in this case
        .relays(List.of(
            Relay.builder()
                .port(3001)
                .dnsName("preprod-relay1.angelstakepool.net")
              //  .relayType(RelayType.SINGLE_HOST_NAME) //TODO refactor check
                .build()))
        .poolMetadataUrl("https://angelstakepool.net/preprod/angel.json")
        .poolMetadataHash("bf44709dd714742688eeff2b6ca5573fe312a2e5f49d564c4c2311923c63952c")
        .build();
    PoolRegistration poolRegistration = PoolRegistration.builder()
        .poolParams(poolParams)
        .build();
//    poolRegistration.setIndex(1); //TODO refactor check

    Mockito.when(aggregatedBlock.getEpochNo()).thenReturn(1);
    Mockito.when(batchCertificateDataService.findPoolHashByHashRaw(Mockito.anyString()))
        .thenReturn(Optional.of(Mockito.mock(PoolHash.class)));
    Mockito.when(batchCertificateDataService.savePoolHash(Mockito.any()))
        .then(AdditionalAnswers.returnsFirstArg());
    Mockito.when(batchCertificateDataService
            .findPoolMetadataRefByPoolHashAndUrlAndHash(
                Mockito.any(), Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Optional.of(Mockito.mock(PoolMetadataRef.class)));
    Mockito.when(batchCertificateDataService.savePoolMetadataRef(Mockito.any()))
        .then(AdditionalAnswers.returnsFirstArg());
    Mockito.when(batchCertificateDataService.poolUpdateExistsByPoolHash(Mockito.any()))
        .thenReturn(Boolean.TRUE);

    victim.handle(aggregatedBlock, poolRegistration, 1, tx, null, stakeAddressMap);

    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .findPoolHashByHashRaw(Mockito.anyString());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolHash(Mockito.any());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .findPoolMetadataRefByPoolHashAndUrlAndHash(Mockito.any(), Mockito.anyString(),
            Mockito.anyString());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolMetadataRef(Mockito.any());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .poolUpdateExistsByPoolHash(Mockito.any());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolUpdate(poolUpdateCaptor.capture());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolOwners(Mockito.anyCollection());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolRelays(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(batchCertificateDataService);

    PoolUpdate poolUpdate = poolUpdateCaptor.getValue();
    Assertions.assertEquals(4, poolUpdate.getActiveEpochNo());
  }

  @Test
  @DisplayName("Should handle pool registration certificate with no metadata reference successfully")
  void shouldHandlePoolRegistrationCertificateWithNoMetadataReferenceSuccessfullyTest() {
    String rewardAccount = "e0acd71b5584acbd18cb1132f7923656cae9d05cfae08e75bd4d508dc2";
    String poolOwner = "968d1021ebd7178e1fb0e79676982825cabc779b653e1234d58ce3c6";
    Map<String, StakeAddress> stakeAddressMap = Map.of(
        rewardAccount, Mockito.mock(StakeAddress.class),
        "e0" + poolOwner, Mockito.mock(StakeAddress.class)
    );
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);
    Tx tx = Mockito.mock(Tx.class);
    PoolParams poolParams = PoolParams.builder()
        .operator("a57cbcb8ecdf24f469928da924b5bc6e4cbc3b57859577211a0daf6f")
        .vrfKeyHash("868173d343611103acbdb3452b922bbca5e580d08da4e8f7abf3fb0f2284338a")
        .pledge(BigInteger.valueOf(100000000000000L))
        .cost(BigInteger.valueOf(500000000L))
        .margin("1/1")
        .rewardAccount(rewardAccount)
        .poolOwners(Set.of(poolOwner)) // pool owner is the same as reward account in this case
        .relays(List.of(
            Relay.builder()
                .port(30000)
                .dnsName("preprod-node.world.dev.cardano.org")
//                .relayType(RelayType.SINGLE_HOST_NAME) //TODO refactor check
                .build()))
        .build();
    PoolRegistration poolRegistration = PoolRegistration.builder()
        .poolParams(poolParams)
        .build();
//    poolRegistration.setIndex(1); //TODO refactor check

    Mockito.when(aggregatedBlock.getEpochNo()).thenReturn(1);
    Mockito.when(batchCertificateDataService.findPoolHashByHashRaw(Mockito.anyString()))
        .thenReturn(Optional.of(Mockito.mock(PoolHash.class)));
    Mockito.when(batchCertificateDataService.savePoolHash(Mockito.any()))
        .then(AdditionalAnswers.returnsFirstArg());
    Mockito.when(batchCertificateDataService.poolUpdateExistsByPoolHash(Mockito.any()))
        .thenReturn(Boolean.TRUE);

    victim.handle(aggregatedBlock, poolRegistration, 1, tx, null, stakeAddressMap);

    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .findPoolHashByHashRaw(Mockito.anyString());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolHash(Mockito.any());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .poolUpdateExistsByPoolHash(Mockito.any());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolUpdate(poolUpdateCaptor.capture());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolOwners(Mockito.anyCollection());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolRelays(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(batchCertificateDataService);

    PoolUpdate poolUpdate = poolUpdateCaptor.getValue();
    Assertions.assertEquals(4, poolUpdate.getActiveEpochNo());
  }

  @Test
  @DisplayName("Should handle pool registration certificate with all pool relay types successfully")
  void shouldHandlePoolRegistrationCertificateWithAllPoolRelayTypesSuccessfullyTest() {
    String rewardAccount = "e0acd71b5584acbd18cb1132f7923656cae9d05cfae08e75bd4d508dc2";
    String poolOwner = "968d1021ebd7178e1fb0e79676982825cabc779b653e1234d58ce3c6";
    Map<String, StakeAddress> stakeAddressMap = Map.of(
        rewardAccount, Mockito.mock(StakeAddress.class),
        "e0" + poolOwner, Mockito.mock(StakeAddress.class)
    );
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);
    Tx tx = Mockito.mock(Tx.class);
    PoolParams poolParams = PoolParams.builder()
        .operator("a57cbcb8ecdf24f469928da924b5bc6e4cbc3b57859577211a0daf6f")
        .vrfKeyHash("868173d343611103acbdb3452b922bbca5e580d08da4e8f7abf3fb0f2284338a")
        .pledge(BigInteger.valueOf(100000000000000L))
        .cost(BigInteger.valueOf(500000000L))
        .margin("1/1")
        .rewardAccount(rewardAccount)
        .poolOwners(Set.of(poolOwner)) // pool owner is the same as reward account in this case
        .relays(List.of(
            Relay.builder()
                .port(1234)
                .ipv4("12.345.67.89")
                .ipv6("2001:0db8:85a3:0000:0000:8a2e:0370:7334")
//                .relayType(RelayType.SINGLE_HOST_ADDR) //TODO refactor check
                .build(),
            Relay.builder()
                .port(30000)
                .dnsName("preprod-node.world.dev.cardano.org")
//                .relayType(RelayType.SINGLE_HOST_NAME) //TODO refactor check
                .build(),
            Relay.builder()
                .dnsName("relays-new.cardano-testnet.iohkdev.io")
//                .relayType(RelayType.MULTI_HOST_NAME)  //TODO refactor check
                .build()
        ))
        .build();
    PoolRegistration poolRegistration = PoolRegistration.builder()
        .poolParams(poolParams)
        .build();
//    poolRegistration.setIndex(1);  //TODO refactor check

    Mockito.when(aggregatedBlock.getEpochNo()).thenReturn(1);
    Mockito.when(batchCertificateDataService.findPoolHashByHashRaw(Mockito.anyString()))
        .thenReturn(Optional.of(Mockito.mock(PoolHash.class)));
    Mockito.when(batchCertificateDataService.savePoolHash(Mockito.any()))
        .then(AdditionalAnswers.returnsFirstArg());
    Mockito.when(batchCertificateDataService.poolUpdateExistsByPoolHash(Mockito.any()))
        .thenReturn(Boolean.TRUE);

    victim.handle(aggregatedBlock, poolRegistration, 1, tx, null, stakeAddressMap);

    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .findPoolHashByHashRaw(Mockito.anyString());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolHash(Mockito.any());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .poolUpdateExistsByPoolHash(Mockito.any());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolUpdate(Mockito.any());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolOwners(Mockito.anyCollection());
    Mockito.verify(batchCertificateDataService, Mockito.times(1))
        .savePoolRelays(poolRelaysCaptor.capture());
    Mockito.verifyNoMoreInteractions(batchCertificateDataService);

    List<PoolRelay> poolRelays = poolRelaysCaptor.getValue();
    Assertions.assertEquals(1234, poolRelays.get(0).getPort());
    Assertions.assertEquals("12.345.67.89", poolRelays.get(0).getIpv4());
    Assertions.assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", poolRelays.get(0).getIpv6());
    Assertions.assertNull(poolRelays.get(0).getDnsName());
    Assertions.assertNull(poolRelays.get(0).getDnsSrvName());

    Assertions.assertEquals(30000, poolRelays.get(1).getPort());
    Assertions.assertNull(poolRelays.get(1).getIpv4());
    Assertions.assertNull(poolRelays.get(1).getIpv6());
    Assertions.assertEquals("preprod-node.world.dev.cardano.org", poolRelays.get(1).getDnsName());
    Assertions.assertNull(poolRelays.get(1).getDnsSrvName());

    Assertions.assertNull(poolRelays.get(2).getPort());
    Assertions.assertNull(poolRelays.get(2).getIpv4());
    Assertions.assertNull(poolRelays.get(2).getIpv6());
    Assertions.assertNull(poolRelays.get(2).getDnsName());
    Assertions.assertEquals("relays-new.cardano-testnet.iohkdev.io",
        poolRelays.get(2).getDnsSrvName());
  }
}
