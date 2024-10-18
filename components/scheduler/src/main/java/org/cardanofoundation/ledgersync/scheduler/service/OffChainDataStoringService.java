package org.cardanofoundation.ledgersync.scheduler.service;

public interface OffChainDataStoringService {

    /**
     * Validate and save the off-chain data
     */
    void validateAndPersistData();

}
