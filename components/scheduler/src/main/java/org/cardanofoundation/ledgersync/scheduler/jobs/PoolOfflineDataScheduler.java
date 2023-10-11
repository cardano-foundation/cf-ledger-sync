package org.cardanofoundation.ledgersync.scheduler.jobs;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.scheduler.PoolOfflineDataProperties;
import org.cardanofoundation.ledgersync.scheduler.dto.PoolData;
import org.cardanofoundation.ledgersync.scheduler.service.PoolOfflineDataFetchingService;
import org.cardanofoundation.ledgersync.scheduler.service.PoolOfflineDataStoringService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PoolOfflineDataScheduler {

    final PoolOfflineDataStoringService poolOfflineDataStoringService;
    final PoolOfflineDataFetchingService poolOfflineDataFetchingService;
    final PoolOfflineDataProperties poolOfflineDataProperties;

    @Transactional
    @Scheduled(fixedDelayString = "#{poolOfflineDataProperties.getFixedDelay() * 1000}")
    public void fetchPoolOffline() {
        log.info("-----------Start job fetch pool offline data-----------");
        final var startTime = System.currentTimeMillis();
        List<PoolData> poolDataList = poolOfflineDataFetchingService.fetchPoolOfflineData();
        List<PoolData> successPools = poolDataList.stream().filter(PoolData::isValid).toList();
        List<PoolData> failPools = poolDataList.stream().filter(poolData -> !poolData.isValid()).toList();
        poolOfflineDataFetchingService.fetchPoolOfflineDataLogo(successPools);
        poolOfflineDataStoringService.saveSuccessPoolOfflineData(successPools);
        poolOfflineDataStoringService.saveFailOfflineData(failPools);
        log.info(
                "----------End job fetch pool offline data, time taken: {} ms----------",
                (System.currentTimeMillis() - startTime));
    }
}
