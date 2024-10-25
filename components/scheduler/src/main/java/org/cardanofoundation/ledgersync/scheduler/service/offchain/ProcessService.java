package org.cardanofoundation.ledgersync.scheduler.service.offchain;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDataCheckpoint;

public interface ProcessService {
    void process(long startTime, OffChainDataCheckpoint cpType);

}
