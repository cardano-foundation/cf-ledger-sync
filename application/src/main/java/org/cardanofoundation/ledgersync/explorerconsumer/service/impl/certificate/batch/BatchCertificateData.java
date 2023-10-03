package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.certificate.batch;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.explorer.consumercommon.entity.*;

import java.util.*;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatchCertificateData {

    Map<String, PoolHash> poolHashMap;
    Set<PoolMetadataRef> poolMetadataRefs;
    List<PoolOwner> poolOwners;
    List<PoolRelay> poolRelays;
    List<PoolRetire> poolRetires;
    List<Delegation> delegations;
    List<StakeDeregistration> stakeDeregistrations;
    List<StakeRegistration> stakeRegistrations;
    List<Treasury> treasuries;
    List<Reserve> reserves;
    List<PotTransfer> potTransfers;

    // Key is pool hash entity's id
    Map<Long, Set<PoolUpdate>> poolUpdateMap;

    BatchCertificateData() {
        poolHashMap = new LinkedHashMap<>();
        poolMetadataRefs = new LinkedHashSet<>();
        poolOwners = new LinkedList<>();
        poolRelays = new LinkedList<>();
        poolRetires = new LinkedList<>();
        delegations = new LinkedList<>();
        stakeDeregistrations = new LinkedList<>();
        stakeRegistrations = new LinkedList<>();
        treasuries = new LinkedList<>();
        reserves = new LinkedList<>();
        potTransfers = new LinkedList<>();
        poolUpdateMap = new LinkedHashMap<>();
    }

    // This method must be called after handling all certificates
    public void clear() {
        poolHashMap.clear();
        poolMetadataRefs.clear();
        poolOwners.clear();
        poolRelays.clear();
        poolRetires.clear();
        delegations.clear();
        stakeDeregistrations.clear();
        stakeRegistrations.clear();
        treasuries.clear();
        reserves.clear();
        potTransfers.clear();
        poolUpdateMap.clear();

        poolHashMap = new LinkedHashMap<>();
        poolMetadataRefs = new LinkedHashSet<>();
        poolOwners = new LinkedList<>();
        poolRelays = new LinkedList<>();
        poolRetires = new LinkedList<>();
        delegations = new LinkedList<>();
        stakeDeregistrations = new LinkedList<>();
        stakeRegistrations = new LinkedList<>();
        treasuries = new LinkedList<>();
        reserves = new LinkedList<>();
        potTransfers = new LinkedList<>();
        poolUpdateMap = new LinkedHashMap<>();
    }
}
