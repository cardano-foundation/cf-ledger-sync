package org.cardanofoundation.ledgersync.service;

import org.cardanofoundation.ledgersync.consumercommon.entity.CostModel;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.springframework.transaction.annotation.Transactional;

public interface CostModelService {
    String PLUTUS_V1_KEY = "PlutusV1";
    String PLUTUS_V2_KEY = "PlutusV2";

    CostModel getGenesisCostModel();

    void setGenesisCostModel(CostModel costModel);

    @Transactional
    void handleCostModel(AggregatedTx tx);

    CostModel findCostModelByHash(String hash);
}
