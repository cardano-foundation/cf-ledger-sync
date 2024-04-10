package org.cardanofoundation.ledgersync.service.impl.certificate.batch;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.consumercommon.entity.*;
import org.cardanofoundation.ledgersync.repository.*;
import org.cardanofoundation.ledgersync.service.BatchCertificateDataService;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BatchCertificateDataServiceImpl implements BatchCertificateDataService {

    private static final BatchCertificateData batchCertificateData = new BatchCertificateData();

    PoolHashRepository poolHashRepository;
    PoolMetadataRefRepository poolMetadataRefRepository;
    PoolOwnerRepository poolOwnerRepository;
    PoolRelayRepository poolRelayRepository;
    PoolRetireRepository poolRetireRepository;
    DelegationRepository delegationRepository;
    StakeDeregistrationRepository stakeDeregistrationRepository;
    StakeRegistrationRepository stakeRegistrationRepository;
    TreasuryRepository treasuryRepository;
    ReserveRepository reserveRepository;
    PotTransferRepository potTransferRepository;
    PoolUpdateRepository poolUpdateRepository;

    @Override
    public Optional<PoolHash> findPoolHashByHashRaw(String hashRaw) {
        return Optional.ofNullable(batchCertificateData.getPoolHashMap().get(hashRaw))
                .or(() -> poolHashRepository.findPoolHashByHashRaw(hashRaw));
    }

    @Override
    public Optional<PoolMetadataRef> findPoolMetadataRefByPoolHashAndUrlAndHash(
            PoolHash poolHash, String url, String hash) {
        return batchCertificateData.getPoolMetadataRefs().stream()
                .filter(poolMetadataRef -> {
                    PoolHash ph = poolMetadataRef.getPoolHash();
                    String poolMetadataRefUrl = poolMetadataRef.getUrl();
                    String poolMetadataRefHash = poolMetadataRef.getHash();
                    return ph.equals(poolHash)
                            && poolMetadataRefUrl.equals(url)
                            && poolMetadataRefHash.equals(hash);
                })
                .findFirst()
                .or(() -> {
                    if (Objects.isNull(poolHash.getId())) {
                        return Optional.empty();
                    }

                    return poolMetadataRefRepository
                            .findPoolMetadataRefByPoolHashAndUrlAndHash(poolHash, url, hash);
                });
    }

    @Override
    public Boolean poolUpdateExistsByPoolHash(PoolHash poolHash) {
        boolean poolUpdateExistInCache = batchCertificateData
                .getPoolUpdateMap().containsKey(poolHash.getId());

        return poolUpdateExistInCache ||
                (Objects.nonNull(poolHash.getId()) && poolUpdateRepository.existsByPoolHash(poolHash));
    }

    @Override
    public PoolHash savePoolHash(PoolHash poolHash) {
        batchCertificateData.getPoolHashMap().putIfAbsent(poolHash.getHashRaw(), poolHash);
        return poolHash;
    }

    @Override
    public PoolMetadataRef savePoolMetadataRef(PoolMetadataRef poolMetadataRef) {
        batchCertificateData.getPoolMetadataRefs().add(poolMetadataRef);
        return poolMetadataRef;
    }

    @Override
    public PoolUpdate savePoolUpdate(PoolUpdate poolUpdate) {
        Set<PoolUpdate> poolUpdates = batchCertificateData.getPoolUpdateMap()
                .computeIfAbsent(poolUpdate.getPoolHash().getId(), s -> new LinkedHashSet<>());
        poolUpdates.add(poolUpdate);
        return poolUpdate;
    }

    @Override
    public Collection<PoolOwner> savePoolOwners(Collection<PoolOwner> poolOwners) {
        poolOwners.forEach(batchCertificateData.getPoolOwners()::add);
        return poolOwners;
    }

    @Override
    public Collection<PoolRelay> savePoolRelays(Collection<PoolRelay> poolRelays) {
        poolRelays.forEach(batchCertificateData.getPoolRelays()::add);
        return poolRelays;
    }

    @Override
    public PoolRetire savePoolRetire(PoolRetire poolRetire) {
        batchCertificateData.getPoolRetires().add(poolRetire);
        return poolRetire;
    }

    @Override
    public Delegation saveDelegation(Delegation delegation) {
        batchCertificateData.getDelegations().add(delegation);
        return delegation;
    }

    @Override
    public StakeDeregistration saveStakeDeregistration(StakeDeregistration stakeDeregistration) {
        batchCertificateData.getStakeDeregistrations().add(stakeDeregistration);
        return stakeDeregistration;
    }

    @Override
    public StakeRegistration saveStakeRegistration(StakeRegistration stakeRegistration) {
        batchCertificateData.getStakeRegistrations().add(stakeRegistration);
        return stakeRegistration;
    }

    @Override
    public Collection<Treasury> saveTreasuries(Collection<Treasury> treasuries) {
        treasuries.forEach(batchCertificateData.getTreasuries()::add);
        return treasuries;
    }

    @Override
    public Collection<Reserve> saveReserves(Collection<Reserve> reserves) {
        reserves.forEach(batchCertificateData.getReserves()::add);
        return reserves;
    }

    @Override
    public PotTransfer savePotTransfer(PotTransfer potTransfer) {
        batchCertificateData.getPotTransfers().add(potTransfer);
        return potTransfer;
    }

    @Override
    public void saveAllAndClearBatchData() {
        poolHashRepository.saveAll(batchCertificateData.getPoolHashMap().values());
        poolUpdateRepository.saveAll(batchCertificateData.getPoolUpdateMap()
                .values()
                .stream()
                .flatMap(Collection::stream)
                .toList());
        poolMetadataRefRepository.saveAll(batchCertificateData.getPoolMetadataRefs());
        poolOwnerRepository.saveAll(batchCertificateData.getPoolOwners());
        poolRelayRepository.saveAll(batchCertificateData.getPoolRelays());
        poolRetireRepository.saveAll(batchCertificateData.getPoolRetires());
        delegationRepository.saveAll(batchCertificateData.getDelegations());
        stakeDeregistrationRepository.saveAll(batchCertificateData.getStakeDeregistrations());
        stakeRegistrationRepository.saveAll(batchCertificateData.getStakeRegistrations());
        treasuryRepository.saveAll(batchCertificateData.getTreasuries());
        reserveRepository.saveAll(batchCertificateData.getReserves());
        potTransferRepository.saveAll(batchCertificateData.getPotTransfers());
        batchCertificateData.clear();
    }
}
