package org.cardanofoundation.ledgersync.explorerconsumer.service;

import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;

public interface MetricCollectorService {
    public void collectRollbackMetric();

    public void collectEraAndEpochProcessingMetric(AggregatedBlock aggregatedBlock);

    public void collectCurrentBlockMetric(AggregatedBlock aggregatedBlock);

    public void collectCurrentEraMetric(AggregatedBlock aggregatedBlock);

    public void collectNetworkSyncPercentMetric(AggregatedBlock aggregatedBlock);

    public void collectCountBlockProcessingMetric(AggregatedBlock firstBlock, AggregatedBlock lastBlock);

}
