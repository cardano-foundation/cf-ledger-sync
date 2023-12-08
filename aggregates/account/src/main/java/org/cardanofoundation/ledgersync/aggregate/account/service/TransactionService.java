package org.cardanofoundation.ledgersync.aggregate.account.service;

import org.cardanofoundation.ledgersync.aggregate.account.domain.AggregatedBlock;

import java.util.Collection;

public interface TransactionService {

    /**
     * Prepare and handle all tx contents. Everything related to tx that needs to be
     * processed is done here
     *
     * @param blockMap         map of block hash and target block entity
     * @param aggregatedBlocks aggregated block objects
     */
    void prepareAndHandleTxs(Collection<AggregatedBlock> aggregatedBlocks);
}
