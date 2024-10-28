package org.cardanofoundation.ledgersync.scheduler.jobs;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.scheduler.service.OffChainPersistService;
import org.cardanofoundation.ledgersync.scheduler.OffChainDataProperties;
import org.cardanofoundation.ledgersync.scheduler.service.OffChainRetryDataErrorService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class OffChainDataScheduler {

    final OffChainPersistService offChainPersistService;
    final OffChainRetryDataErrorService offChainRetryDataErrorService;
    final OffChainDataProperties offChainDataProperties;

    @Transactional
//    @Scheduled(initialDelayString = "${ledger-sync.scheduler.off-chain-data.initial-delay}",
//            fixedDelayString = "${ledger-sync.scheduler.off-chain-data.fixed-delay}")
    @Scheduled(initialDelayString = "#{offChainDataProperties.getInitialDelay() * 1000}",
            fixedDelayString = "#{offChainDataProperties.getFixedDelay() * 1000}")
    public void fetchOffChain() {
        log.info("-----------Start job fetch pool offline data-----------");
        final var startTime = System.currentTimeMillis();
       offChainPersistService.validateAndPersistData();
        log.info(
                "----------End job fetch pool offline data, time taken: {} ms----------",
                (System.currentTimeMillis() - startTime));
    }

    @Transactional
    @Scheduled(initialDelayString = "#{offChainDataProperties.getInitialDelayFetchError() * 1000}",
        fixedDelayString = "#{offChainDataProperties.getFixedDelayFetchError() * 1000}")
    public void retryFetchError() {
        log.info("-----------Start job retry offchain data-----------");
        final var startTime = System.currentTimeMillis();
        offChainRetryDataErrorService.retryOffChainErrorData();
        log.info(
            "----------End job retry offchain data, time taken: {} ms----------",
            (System.currentTimeMillis() - startTime));
    }
}
