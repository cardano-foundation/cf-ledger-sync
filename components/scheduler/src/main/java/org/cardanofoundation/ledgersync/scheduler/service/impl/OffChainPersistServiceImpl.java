package org.cardanofoundation.ledgersync.scheduler.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

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

    final ExecutorService executor;
    final OffChainProcessPersistDataService govActionPersistServiceImpl;
    final OffChainProcessPersistDataService votingDataPersistServiceImpl;
    final OffChainProcessPersistDataService constitutionPersistServiceImpl;
    final OffChainProcessPersistDataService committeeDeregPersistServiceImpl;
    final OffChainProcessPersistDataService dRepRegistrationPersistServiceImpl;

    public OffChainPersistServiceImpl(
            @Qualifier("offChainExecutor") ExecutorService executor,
            @Qualifier("govActionPersistServiceImpl") OffChainProcessPersistDataService govActionPersistServiceImpl,
            @Qualifier("votingDataPersistServiceImpl") OffChainProcessPersistDataService votingDataPersistServiceImpl,
            @Qualifier("constitutionPersistServiceImpl") OffChainProcessPersistDataService constitutionPersistServiceImpl,
            @Qualifier("committeeDeregPersistServiceImpl") OffChainProcessPersistDataService committeeDeregPersistServiceImpl,
            @Qualifier("dRepRegistrationPersistServiceImpl") OffChainProcessPersistDataService dRepRegistrationPersistServiceImpl) {

        this.executor = executor;
        this.govActionPersistServiceImpl = govActionPersistServiceImpl;
        this.votingDataPersistServiceImpl = votingDataPersistServiceImpl;
        this.constitutionPersistServiceImpl = constitutionPersistServiceImpl;
        this.committeeDeregPersistServiceImpl = committeeDeregPersistServiceImpl;
        this.dRepRegistrationPersistServiceImpl = dRepRegistrationPersistServiceImpl;
    }

    @Override
    public void validateAndPersistData() {
        long startTime = System.currentTimeMillis();
        log.info("Start validating off-chain data");

        try {
            List<OffChainProcessPersistDataService> services = Arrays.asList(
                govActionPersistServiceImpl,
                votingDataPersistServiceImpl,
                constitutionPersistServiceImpl,
                committeeDeregPersistServiceImpl,
                dRepRegistrationPersistServiceImpl);

            List<CompletableFuture<Void>> futures = services.stream()
                .map(service -> CompletableFuture.runAsync(service::process, executor))
                .toList();

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            log.error("Error processing validating off-chain data tasks", e.getCause());
        }
        log.info("End validating off-chain data, taken time: {} ms", System.currentTimeMillis() - startTime);
    }

}
