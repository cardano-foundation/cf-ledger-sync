package org.cardanofoundation.ledgersync.service;

import org.cardanofoundation.ledgersync.consumercommon.entity.*;

import java.util.Collection;
import java.util.Optional;

/**
 * This interface is made to manage batched certificate data. This is needed due to high query times
 * of certificate data entities (e.g. pool registration), while most of the data were already
 * existed in the batch, causing overall performance slowdown
 * <p>
 * We tend to save the needed data into both the repository and cache layer, and to simplify this,
 * Java POJOs will be used for that. After handling all certificates, this cache layer is evicted to
 * make sure no data inconsistency exists
 */
public interface BatchCertificateDataService {


    /**
     * Find pool hash from raw hash
     *
     * @param hashRaw pool hash's raw hash to find
     * @return Optional pool hash entity if found, or empty if not found
     */
    Optional<PoolHash> findPoolHashByHashRaw(String hashRaw);

    /**
     * Find pool metadata reference entity
     *
     * @param poolHash pool metadata ref's associated pool hash
     * @param url      pool metadata's URL
     * @param hash     pool metadata hash
     * @return Optional pool metadata ref entity if found, or empty if not found
     */
    Optional<PoolMetadataRef> findPoolMetadataRefByPoolHashAndUrlAndHash(
            PoolHash poolHash, String url, String hash);

    /**
     * Check if a pool already had an update before (or already registered) by pool hash
     *
     * @param poolHash associated pool hash entity to find its updates
     * @return true if the pool was registered before, or false if not
     */
    Boolean poolUpdateExistsByPoolHash(PoolHash poolHash);

    /**
     * Save a pool hash entity to cached batch data
     *
     * @param poolHash pool hash entity to save
     * @return newly saved pool hash entity
     */
    PoolHash savePoolHash(PoolHash poolHash);

    /**
     * Save a pool metadata ref entity to cached batch data
     *
     * @param poolMetadataRef pool metadata ref entity to save
     * @return newly saved pool metadata ref entity
     */
    PoolMetadataRef savePoolMetadataRef(PoolMetadataRef poolMetadataRef);

    /**
     * Save a pool update entity to cached batch data
     *
     * @param poolUpdate pool update entity to save
     * @return newly saved pool update entity
     */
    PoolUpdate savePoolUpdate(PoolUpdate poolUpdate);

    /**
     * Save pool owner entities to cached batch data
     *
     * @param poolOwners pool owner entities to save
     * @return newly saved pool owner entities
     */
    Collection<PoolOwner> savePoolOwners(Collection<PoolOwner> poolOwners);

    /**
     * Save pool relay entities to cached batch data
     *
     * @param poolRelays pool relay entities to save
     * @return newly saved pool relay entities
     */
    Collection<PoolRelay> savePoolRelays(Collection<PoolRelay> poolRelays);

    /**
     * Save a pool retire entity to cached batch data
     *
     * @param poolRetire pool retire entity to save
     * @return newly saved pool retire entity
     */
    PoolRetire savePoolRetire(PoolRetire poolRetire);

    /**
     * Save a delegation entity to cached batch data
     *
     * @param delegation delegation entity to save
     * @return newly saved delegation entity
     */
    Delegation saveDelegation(Delegation delegation);

    /**
     * Save a stake deregistration entity to cached batch data
     *
     * @param stakeDeregistration stake deregistration entity to save
     * @return newly saved stake deregistration entity
     */
    StakeDeregistration saveStakeDeregistration(StakeDeregistration stakeDeregistration);

    /**
     * Save a stake registration entity to cached batch data
     *
     * @param stakeRegistration stake registration entity to save
     * @return newly saved stake registration entity
     */
    StakeRegistration saveStakeRegistration(StakeRegistration stakeRegistration);

    /**
     * Save treasury entities to cached batch data
     *
     * @param treasuries treasury entities to save
     * @return newly saved treasury entities
     */
    Collection<Treasury> saveTreasuries(Collection<Treasury> treasuries);

    /**
     * Save reserve entities to cached batch data
     *
     * @param reserves reserve entities to save
     * @return newly saved reserve entities
     */
    Collection<Reserve> saveReserves(Collection<Reserve> reserves);

    /**
     * Save a pot transfer entity to cached batch data
     *
     * @param potTransfer pot transfer entity to save
     * @return newly saved pot transfer entity
     */
    PotTransfer savePotTransfer(PotTransfer potTransfer);

    /**
     * Flush all cached data to database and evict the cache
     */
    void saveAllAndClearBatchData();
}
