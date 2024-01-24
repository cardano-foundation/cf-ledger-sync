package org.cardanofoundation.ledgersync.service;

import org.cardanofoundation.ledgersync.consumercommon.entity.EpochParam;
import org.springframework.transaction.annotation.Transactional;

public interface EpochParamService {

    void setDefShelleyEpochParam(EpochParam defShelleyEpochParam);

    void setDefAlonzoEpochParam(EpochParam defAlonzoEpochParam);

    void setDefBabbageEpochParam(EpochParam defBabbageEpochParam);

    /**
     * Handle epoch params
     */
    @Transactional
    void handleEpochParams();
}
