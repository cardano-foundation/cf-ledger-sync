package org.cardanofoundation.ledgersync.service;

import org.cardanofoundation.ledgersync.consumercommon.entity.Block;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;

import java.util.Collection;
import java.util.Map;

public interface TransactionService {

    /**
     * Prepare and handle all tx contents. Everything related to tx that needs to be
     * processed is done here
     *
     * @param blockMap         map of block hash and target block entity
     * @param aggregatedBlocks aggregated block objects
     */
    void prepareAndHandleTxs(Map<String, Block> blockMap,
                             Collection<AggregatedBlock> aggregatedBlocks);
}
