package org.cardanofoundation.ledgersync.service;

import org.cardanofoundation.ledgersync.consumercommon.entity.CostModel;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.service.impl.plutus.PlutusKey;
import org.springframework.transaction.annotation.Transactional;

public interface CostModelService {

    CostModel getGenesisCostModel(PlutusKey plutusKey);

    void setGenesisCostModel(PlutusKey plutusKey, CostModel costModel);

    @Transactional
    void handleCostModel(AggregatedTx tx);

    CostModel findCostModelByHash(String hash);
}
