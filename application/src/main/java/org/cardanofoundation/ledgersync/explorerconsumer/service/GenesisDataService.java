package org.cardanofoundation.ledgersync.explorerconsumer.service;

import org.springframework.transaction.annotation.Transactional;

public interface GenesisDataService {

    @Transactional
    void setupData(String genesisHash);

    Long getByronKnownTime();

    Integer getShelleyEpochLength();
}
