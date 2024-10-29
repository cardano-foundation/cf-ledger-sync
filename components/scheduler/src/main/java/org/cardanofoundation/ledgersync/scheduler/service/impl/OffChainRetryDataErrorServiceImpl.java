package org.cardanofoundation.ledgersync.scheduler.service.impl;

import org.cardanofoundation.ledgersync.scheduler.service.OffChainRetryDataErrorService;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.OffChainProcessRetryDataService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class OffChainRetryDataErrorServiceImpl implements OffChainRetryDataErrorService {

    final OffChainProcessRetryDataService govActionRetryServiceImpl;
    final OffChainProcessRetryDataService votingDataRetryServiceImpl;
    final OffChainProcessRetryDataService constitutionRetryServiceImpl;

    public OffChainRetryDataErrorServiceImpl(
        @Qualifier("govActionRetryServiceImpl") OffChainProcessRetryDataService govActionRetryServiceImpl,
        @Qualifier("votingDataRetryServiceImpl") OffChainProcessRetryDataService votingDataRetryServiceImpl,
        @Qualifier("constitutionRetryServiceImpl") OffChainProcessRetryDataService constitutionRetryServiceImpl
    ) {
        this.govActionRetryServiceImpl = govActionRetryServiceImpl;
        this.votingDataRetryServiceImpl = votingDataRetryServiceImpl;
        this.constitutionRetryServiceImpl = constitutionRetryServiceImpl;
    }

    @Override
    public void retryOffChainErrorData() {
        long startTime = System.currentTimeMillis();
        log.info("Start retry error offchain data");

        govActionRetryServiceImpl.process();
        votingDataRetryServiceImpl.process();
        constitutionRetryServiceImpl.process();

        log.info("End retry error offchain data time taken: {} ms", System.currentTimeMillis() - startTime);
    }

}
