package org.cardanofoundation.ledgersync.scheduler.service.impl;

import org.cardanofoundation.ledgersync.scheduler.service.OffChainPersistService;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.OffChainProcessPersistDataService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class OffChainPersistServiceImpl implements OffChainPersistService {

    final OffChainProcessPersistDataService govActionPersistServiceImpl;
    final OffChainProcessPersistDataService votingDataPersistServiceImpl;
    final OffChainProcessPersistDataService constitutionPersistServiceImpl;
    final OffChainProcessPersistDataService committeeDeregPersistServiceImpl;

    public OffChainPersistServiceImpl(
            @Qualifier("govActionPersistServiceImpl") OffChainProcessPersistDataService govActionPersistServiceImpl,
            @Qualifier("votingDataPersistServiceImpl") OffChainProcessPersistDataService votingDataPersistServiceImpl,
            @Qualifier("constitutionPersistServiceImpl") OffChainProcessPersistDataService constitutionPersistServiceImpl,
            @Qualifier("committeeDeregPersistServiceImpl") OffChainProcessPersistDataService committeeDeregPersistServiceImpl) {

        this.govActionPersistServiceImpl = govActionPersistServiceImpl;
        this.votingDataPersistServiceImpl = votingDataPersistServiceImpl;
        this.constitutionPersistServiceImpl = constitutionPersistServiceImpl;
        this.committeeDeregPersistServiceImpl = committeeDeregPersistServiceImpl;
    }

    @Override
    public void validateAndPersistData() {
        long startTime = System.currentTimeMillis();
        log.info("Start validating off-chain data");

        govActionPersistServiceImpl.process();
        votingDataPersistServiceImpl.process();
        constitutionPersistServiceImpl.process();
        committeeDeregPersistServiceImpl.process();

        log.info("End validating off-chain data, taken time: {} ms", System.currentTimeMillis() - startTime);
    }

}
