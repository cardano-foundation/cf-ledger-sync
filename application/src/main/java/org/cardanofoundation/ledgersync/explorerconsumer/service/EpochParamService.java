package org.cardanofoundation.ledgersync.explorerconsumer.service;

import org.cardanofoundation.explorer.consumercommon.entity.EpochParam;
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
