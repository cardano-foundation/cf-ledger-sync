package org.cardanofoundation.ledgersync.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface GenesisDataService {

    @Transactional
    void setupData(String genesisHash);

    Long getByronKnownTime();

    Integer getShelleyEpochLength();

    Integer getUpdateQuorum();

    Set<String> getDelegationKeyHashes();
}
