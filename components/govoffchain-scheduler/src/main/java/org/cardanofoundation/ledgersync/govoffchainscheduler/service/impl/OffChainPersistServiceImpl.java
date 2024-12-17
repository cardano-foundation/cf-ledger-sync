package org.cardanofoundation.ledgersync.govoffchainscheduler.service.impl;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import org.cardanofoundation.ledgersync.govoffchainscheduler.service.OffChainPersistService;
import org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.OffChainProcessPersistDataService;
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
    final Lock lock;
    final OffChainProcessPersistDataService govActionPersistServiceImpl;
    final OffChainProcessPersistDataService votingDataPersistServiceImpl;
    final OffChainProcessPersistDataService constitutionPersistServiceImpl;
    final OffChainProcessPersistDataService committeeDeregPersistServiceImpl;
    final OffChainProcessPersistDataService dRepRegistrationPersistServiceImpl;

    public OffChainPersistServiceImpl(
            @Qualifier("offChainExecutor") ExecutorService executor,
            @Qualifier("virtualThreadLock") Lock lock,
            @Qualifier("govActionPersistServiceImpl") OffChainProcessPersistDataService govActionPersistServiceImpl,
            @Qualifier("votingDataPersistServiceImpl") OffChainProcessPersistDataService votingDataPersistServiceImpl,
            @Qualifier("constitutionPersistServiceImpl") OffChainProcessPersistDataService constitutionPersistServiceImpl,
            @Qualifier("committeeDeregPersistServiceImpl") OffChainProcessPersistDataService committeeDeregPersistServiceImpl,
            @Qualifier("dRepRegistrationPersistServiceImpl") OffChainProcessPersistDataService dRepRegistrationPersistServiceImpl) {

        this.executor = executor;
        this.lock = lock;
        this.govActionPersistServiceImpl = govActionPersistServiceImpl;
        this.votingDataPersistServiceImpl = votingDataPersistServiceImpl;
        this.constitutionPersistServiceImpl = constitutionPersistServiceImpl;
        this.committeeDeregPersistServiceImpl = committeeDeregPersistServiceImpl;
        this.dRepRegistrationPersistServiceImpl = dRepRegistrationPersistServiceImpl;
    }

    @Override
    public void validateAndPersistData() {
        long startTime = System.currentTimeMillis();
        try {
            if (lock.tryLock(4, TimeUnit.MINUTES)) {
                try {
                    log.info("Start validating off-chain data");
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
                } catch (CompletionException e) {
                    log.error("Error processing validating off-chain data tasks", e.getCause());
                } finally {
                    lock.unlock();
                }
            } else {
                log.error("Thread could not acquire the lock (timeout when retry error offchain)");
            }
        } catch (InterruptedException e) {
            log.error("thread is interrupted: ", e.getCause());
        }
        log.info("End validating off-chain data, taken time: {} ms", System.currentTimeMillis() - startTime);
    }

}
