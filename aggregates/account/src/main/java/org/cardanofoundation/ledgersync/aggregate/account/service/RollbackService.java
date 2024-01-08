package org.cardanofoundation.ledgersync.aggregate.account.service;

public interface RollbackService {

    /**
     * Rollback all block data from block number
     *
     * @param blockNo block number to start rolling back
     */
    void rollBackFrom(long slot);
}
