package org.cardanofoundation.ledgersync.aggregate.account.service;

public interface BlockSyncService {

    /**
     * Starts batched block syncing process
     */
    void startBlockSyncing();
}
