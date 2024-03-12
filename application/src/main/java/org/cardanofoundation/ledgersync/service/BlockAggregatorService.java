package org.cardanofoundation.ledgersync.service;


import lombok.RequiredArgsConstructor;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;

@RequiredArgsConstructor
public abstract class BlockAggregatorService<T> // NOSONAR
        implements SyncServiceInstance<T> {


    protected final SlotLeaderService slotLeaderService;
    protected final BlockDataService blockDataService;

    /**
     * Convert CDDL block data to aggregated block data
     *
     * @param block CDDL block data
     * @return aggregated block object
     */
    public abstract AggregatedBlock aggregateBlock(T block);

}
