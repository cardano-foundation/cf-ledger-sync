package org.cardanofoundation.ledgersync.service;

import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.consumercommon.entity.CostModel;
import org.springframework.transaction.annotation.Transactional;

public interface CostModelService {

    @Transactional
    void handleCostModel(AggregatedTx tx);

    CostModel findCostModelByHash(String hash);
}
