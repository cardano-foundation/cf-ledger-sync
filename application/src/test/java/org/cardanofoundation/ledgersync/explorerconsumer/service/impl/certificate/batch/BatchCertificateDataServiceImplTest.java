package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.certificate.batch;

import java.util.List;
import java.util.Optional;

import org.cardanofoundation.explorer.consumercommon.entity.Delegation;
import org.cardanofoundation.explorer.consumercommon.entity.PoolHash;
import org.cardanofoundation.explorer.consumercommon.entity.PoolMetadataRef;
import org.cardanofoundation.explorer.consumercommon.entity.PoolOwner;
import org.cardanofoundation.explorer.consumercommon.entity.PoolRelay;
import org.cardanofoundation.explorer.consumercommon.entity.PoolRetire;
import org.cardanofoundation.explorer.consumercommon.entity.PoolUpdate;
import org.cardanofoundation.explorer.consumercommon.entity.PotTransfer;
import org.cardanofoundation.explorer.consumercommon.entity.Reserve;
import org.cardanofoundation.explorer.consumercommon.entity.StakeDeregistration;
import org.cardanofoundation.explorer.consumercommon.entity.StakeRegistration;
import org.cardanofoundation.explorer.consumercommon.entity.Treasury;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.DelegationRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.PoolHashRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.PoolMetadataRefRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.PoolOwnerRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.PoolRelayRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.PoolRetireRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.PoolUpdateRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.PotTransferRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.ReserveRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.StakeDeregistrationRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.StakeRegistrationRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TreasuryRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.certificate.batch.BatchCertificateDataServiceImpl;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class BatchCertificateDataServiceImplTest {

  @Mock
  PoolHashRepository poolHashRepository;

  @Mock
  PoolMetadataRefRepository poolMetadataRefRepository;

  @Mock
  PoolOwnerRepository poolOwnerRepository;

  @Mock
  PoolRelayRepository poolRelayRepository;

  @Mock
  PoolRetireRepository poolRetireRepository;

  @Mock
  DelegationRepository delegationRepository;

  @Mock
  StakeDeregistrationRepository stakeDeregistrationRepository;

  @Mock
  StakeRegistrationRepository stakeRegistrationRepository;

  @Mock
  TreasuryRepository treasuryRepository;

  @Mock
  ReserveRepository reserveRepository;

  @Mock
  PotTransferRepository potTransferRepository;

  @Mock
  PoolUpdateRepository poolUpdateRepository;

  BatchCertificateDataServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new BatchCertificateDataServiceImpl(
        poolHashRepository, poolMetadataRefRepository,
        poolOwnerRepository, poolRelayRepository,
        poolRetireRepository, delegationRepository,
        stakeDeregistrationRepository, stakeRegistrationRepository,
        treasuryRepository, reserveRepository,
        potTransferRepository, poolUpdateRepository
    );
  }

  @Test
  @DisplayName("Should get PoolHash from repository successfully")
  void shouldGetPoolHashFromRepositorySuccessfullyTest() {
    PoolHash poolHash = Mockito.mock(PoolHash.class);
    String hashRaw = "a57cbcb8ecdf24f469928da924b5bc6e4cbc3b57859577211a0daf6f";
    Mockito.when(poolHash.getHashRaw()).thenReturn(hashRaw);

    Mockito.when(poolHashRepository.findPoolHashByHashRaw(hashRaw))
        .thenReturn(Optional.of(poolHash));

    Optional<PoolHash> poolHashOptional = victim.findPoolHashByHashRaw(hashRaw);
    Assertions.assertFalse(poolHashOptional.isEmpty());
    Assertions.assertEquals(hashRaw, poolHashOptional.get().getHashRaw());
  }

  @Test
  @DisplayName("Should get PoolMetadataRef from repository successfully")
  void shouldGetPoolMetadataRefFromRepositorySuccessfullyTest() {
    String poolMetadataHashRaw = "bf44709dd714742688eeff2b6ca5573fe312a2e5f49d564c4c2311923c63952c";
    String poolMetadataUrl = "https://angelstakepool.net/preprod/angel.json";
    PoolHash poolHash = Mockito.mock(PoolHash.class);
    PoolMetadataRef poolMetadataRef = Mockito.mock(PoolMetadataRef.class);

    Mockito.when(poolMetadataRef.getPoolHash()).thenReturn(poolHash);
    Mockito.when(poolMetadataRef.getHash()).thenReturn(poolMetadataHashRaw);
    Mockito.when(poolMetadataRef.getUrl()).thenReturn(poolMetadataUrl);
    Mockito.when(
            poolMetadataRefRepository
                .findPoolMetadataRefByPoolHashAndUrlAndHash(
                    poolHash, poolMetadataUrl, poolMetadataHashRaw))
        .thenReturn(Optional.of(poolMetadataRef));

    Optional<PoolMetadataRef> poolMetadataRefOptional = victim
        .findPoolMetadataRefByPoolHashAndUrlAndHash(poolHash, poolMetadataUrl, poolMetadataHashRaw);
    Assertions.assertFalse(poolMetadataRefOptional.isEmpty());
    Assertions.assertEquals(poolHash, poolMetadataRefOptional.get().getPoolHash());
    Assertions.assertEquals(poolMetadataHashRaw, poolMetadataRefOptional.get().getHash());
    Assertions.assertEquals(poolMetadataUrl, poolMetadataRefOptional.get().getUrl());
  }

  @Test
  @DisplayName("Should save and get PoolHash successfully")
  void shouldSaveAndGetPoolHashSuccessfullyTest() {
    PoolHash poolHash = Mockito.mock(PoolHash.class);
    String hashRaw = "a57cbcb8ecdf24f469928da924b5bc6e4cbc3b57859577211a0daf6f";
    Mockito.when(poolHash.getHashRaw()).thenReturn(hashRaw);

    victim.savePoolHash(poolHash);

    Optional<PoolHash> poolHashOptional = victim.findPoolHashByHashRaw(hashRaw);
    Assertions.assertFalse(poolHashOptional.isEmpty());
    Assertions.assertEquals(hashRaw, poolHashOptional.get().getHashRaw());
  }

  @Test
  @DisplayName("Should save and get PoolMetadataRef successfully")
  void shouldSaveAndGetPoolMetadataRefSuccessfullyTest() {
    String poolMetadataHashRaw = "bf44709dd714742688eeff2b6ca5573fe312a2e5f49d564c4c2311923c63952c";
    String poolMetadataUrl = "https://angelstakepool.net/preprod/angel.json";
    PoolHash poolHash = Mockito.mock(PoolHash.class);
    PoolMetadataRef poolMetadataRef = Mockito.mock(PoolMetadataRef.class);

    Mockito.when(poolMetadataRef.getPoolHash()).thenReturn(poolHash);
    Mockito.when(poolMetadataRef.getHash()).thenReturn(poolMetadataHashRaw);
    Mockito.when(poolMetadataRef.getUrl()).thenReturn(poolMetadataUrl);

    victim.savePoolMetadataRef(poolMetadataRef);

    Optional<PoolMetadataRef> poolMetadataRefOptional = victim
        .findPoolMetadataRefByPoolHashAndUrlAndHash(poolHash, poolMetadataUrl, poolMetadataHashRaw);
    Assertions.assertFalse(poolMetadataRefOptional.isEmpty());
    Assertions.assertEquals(poolHash, poolMetadataRefOptional.get().getPoolHash());
    Assertions.assertEquals(poolMetadataHashRaw, poolMetadataRefOptional.get().getHash());
    Assertions.assertEquals(poolMetadataUrl, poolMetadataRefOptional.get().getUrl());
  }

  @Test
  @DisplayName("Pool update exists check should return true from cache")
  void poolUpdateExistsShouldReturnTrueFromCacheTest() {
    PoolHash poolHash = Mockito.mock(PoolHash.class);
    PoolUpdate poolUpdate = Mockito.mock(PoolUpdate.class);

    Mockito.when(poolHash.getId()).thenReturn(1L);
    Mockito.when(poolUpdate.getPoolHash()).thenReturn(poolHash);

    victim.savePoolUpdate(poolUpdate);
    Assertions.assertTrue(victim.poolUpdateExistsByPoolHash(poolHash));
  }

  @Test
  @DisplayName("Pool update exists check should return true from repository")
  void poolUpdateExistsShouldReturnTrueFromRepositoryTest() {
    PoolHash poolHash = Mockito.mock(PoolHash.class);

    Mockito.when(poolHash.getId()).thenReturn(1L);
    Mockito.when(poolUpdateRepository.existsByPoolHash(poolHash)).thenReturn(Boolean.TRUE);

    Assertions.assertTrue(victim.poolUpdateExistsByPoolHash(poolHash));
  }

  @Test
  @DisplayName("Pool update exists check should return false")
  void poolUpdateExistsShouldReturnFalseTest() {
    PoolHash poolHash = Mockito.mock(PoolHash.class);

    // When PoolHash is newly cached (id == null)
    Assertions.assertFalse(victim.poolUpdateExistsByPoolHash(poolHash));

    // When PoolHash exists from database
    Mockito.when(poolHash.getId()).thenReturn(1L);
    Mockito.when(poolUpdateRepository.existsByPoolHash(poolHash)).thenReturn(Boolean.FALSE);
    Assertions.assertFalse(victim.poolUpdateExistsByPoolHash(poolHash));
  }

  @Test
  @DisplayName("Should save PoolUpdate successfully")
  void shouldSavePoolUpdateSuccessfullyTest() {
    PoolHash poolHash = Mockito.mock(PoolHash.class);
    PoolUpdate poolUpdate = Mockito.mock(PoolUpdate.class);

    Mockito.when(poolHash.getId()).thenReturn(1L);
    Mockito.when(poolUpdate.getPoolHash()).thenReturn(poolHash);

    Assertions.assertDoesNotThrow(() -> victim.savePoolUpdate(poolUpdate));
  }

  @Test
  @DisplayName("Should save PoolOwner collection successfully")
  void shouldSavePoolOwnersSuccessfullyTest() {
    PoolOwner poolOwner = Mockito.mock(PoolOwner.class);
    PoolOwner poolOwner2 = Mockito.mock(PoolOwner.class);
    Assertions.assertDoesNotThrow(() -> victim.savePoolOwners(List.of(poolOwner, poolOwner2)));
  }

  @Test
  @DisplayName("Should save PoolRelay collection successfully")
  void shouldSavePoolRelaysSuccessfullyTest() {
    PoolRelay poolRelay = Mockito.mock(PoolRelay.class);
    PoolRelay poolRelay2 = Mockito.mock(PoolRelay.class);
    Assertions.assertDoesNotThrow(() -> victim.savePoolRelays(List.of(poolRelay, poolRelay2)));
  }

  @Test
  @DisplayName("Should save PoolRetire successfully")
  void shouldSavePoolRetireSuccessfullyTest() {
    PoolRetire poolRetire = Mockito.mock(PoolRetire.class);
    Assertions.assertDoesNotThrow(() -> victim.savePoolRetire(poolRetire));
  }

  @Test
  @DisplayName("Should save Delegation successfully")
  void shouldSaveDelegationSuccessfullyTest() {
    Delegation delegation = Mockito.mock(Delegation.class);
    Assertions.assertDoesNotThrow(() -> victim.saveDelegation(delegation));
  }

  @Test
  @DisplayName("Should save StakeDeregistration successfully")
  void shouldSaveStakeDeregistrationSuccessfullyTest() {
    StakeDeregistration stakeDeregistration = Mockito.mock(StakeDeregistration.class);
    Assertions.assertDoesNotThrow(() -> victim.saveStakeDeregistration(stakeDeregistration));
  }

  @Test
  @DisplayName("Should save StakeRegistration successfully")
  void shouldSaveStakeRegistrationSuccessfullyTest() {
    StakeRegistration stakeRegistration = Mockito.mock(StakeRegistration.class);
    Assertions.assertDoesNotThrow(() -> victim.saveStakeRegistration(stakeRegistration));
  }

  @Test
  @DisplayName("Should save Treasury collection successfully")
  void shouldSaveTreasuriesSuccessfullyTest() {
    Treasury treasury = Mockito.mock(Treasury.class);
    Treasury treasury2 = Mockito.mock(Treasury.class);
    Assertions.assertDoesNotThrow(() -> victim.saveTreasuries(List.of(treasury, treasury2)));
  }

  @Test
  @DisplayName("Should save Reserve collection successfully")
  void shouldSaveReservesSuccessfullyTest() {
    Reserve reserve = Mockito.mock(Reserve.class);
    Reserve reserve2 = Mockito.mock(Reserve.class);
    Assertions.assertDoesNotThrow(() -> victim.saveReserves(List.of(reserve, reserve2)));
  }

  @Test
  @DisplayName("Should save PotTransfer successfully")
  void shouldSavePotTransferSuccessfullyTest() {
    PotTransfer potTransfer = Mockito.mock(PotTransfer.class);
    Assertions.assertDoesNotThrow(() -> victim.savePotTransfer(potTransfer));
  }

  @Test
  @DisplayName("Should save all successfully")
  void shouldSaveAllSuccessfullyTest() {
    Assertions.assertDoesNotThrow(() -> victim.saveAllAndClearBatchData());
  }
}
