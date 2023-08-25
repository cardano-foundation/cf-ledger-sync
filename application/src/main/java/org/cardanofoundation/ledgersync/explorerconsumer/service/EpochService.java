package org.cardanofoundation.ledgersync.explorerconsumer.service;

import org.cardanofoundation.explorer.consumercommon.entity.Block;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;

import java.util.Collection;
import java.util.List;

public interface EpochService {

    /**
     * Handle epoch data of aggregated block
     *
     * @param aggregatedBlocks collection of aggregated block objects to handle epoch data
     */
    void handleEpoch(Collection<AggregatedBlock> aggregatedBlocks);

    /**
     * Rollback epoch statistics from a list of blocks being rolled back
     *
     * @param rollbackBlocks list of blocks being rolled back
     */
    void rollbackEpochStats(List<Block> rollbackBlocks);
}
