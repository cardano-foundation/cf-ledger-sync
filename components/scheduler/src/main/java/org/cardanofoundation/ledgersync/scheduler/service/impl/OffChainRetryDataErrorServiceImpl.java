package org.cardanofoundation.ledgersync.scheduler.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

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

    final ExecutorService executor;
    final OffChainProcessRetryDataService govActionRetryServiceImpl;
    final OffChainProcessRetryDataService votingDataRetryServiceImpl;
    final OffChainProcessRetryDataService constitutionRetryServiceImpl;
    final OffChainProcessRetryDataService committeeDeregRetryServiceImpl;
    final OffChainProcessRetryDataService dRepRegistrationRetryServiceImpl;

    public OffChainRetryDataErrorServiceImpl(
            @Qualifier("offChainExecutor") ExecutorService executor,
            @Qualifier("govActionRetryServiceImpl") OffChainProcessRetryDataService govActionRetryServiceImpl,
            @Qualifier("votingDataRetryServiceImpl") OffChainProcessRetryDataService votingDataRetryServiceImpl,
            @Qualifier("constitutionRetryServiceImpl") OffChainProcessRetryDataService constitutionRetryServiceImpl,
            @Qualifier("committeeDeregRetryServiceImpl") OffChainProcessRetryDataService committeeDeregRetryServiceImpl,
            @Qualifier("dRepRegistrationRetryServiceImpl") OffChainProcessRetryDataService dRepRegistrationRetryServiceImpl) {

        this.executor = executor;
        this.govActionRetryServiceImpl = govActionRetryServiceImpl;
        this.votingDataRetryServiceImpl = votingDataRetryServiceImpl;
        this.constitutionRetryServiceImpl = constitutionRetryServiceImpl;
        this.committeeDeregRetryServiceImpl = committeeDeregRetryServiceImpl;
        this.dRepRegistrationRetryServiceImpl = dRepRegistrationRetryServiceImpl;
    }

    @Override
    public void retryOffChainErrorData() {
        long startTime = System.currentTimeMillis();
        log.info("Start retry error offchain data");

        try {
            List<OffChainProcessRetryDataService> services = Arrays.asList(
                govActionRetryServiceImpl,
                votingDataRetryServiceImpl,
                constitutionRetryServiceImpl,
                committeeDeregRetryServiceImpl,
                dRepRegistrationRetryServiceImpl);

            List<CompletableFuture<Void>> futures = services.stream()
                .map(service -> CompletableFuture.runAsync(service::process, executor))
                .toList();

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (CompletionException e) {
            log.error("Error processing retry tasks", e.getCause());
        }
        log.info("End retry error offchain data time taken: {} ms", System.currentTimeMillis() - startTime);
    }

}
