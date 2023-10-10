package org.cardanofoundation.ledgersync.job.schedules;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.job.service.PoolOfflineDataFetchingService;
import org.cardanofoundation.ledgersync.job.service.PoolOfflineDataStoringService;
import org.cardanofoundation.ledgersync.schedulecommon.dto.PoolData;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConditionalOnProperty(
        value = "jobs.pool-offline-data.enabled",
        matchIfMissing = true,
        havingValue = "true")
public class PoolOfflineDataSchedule {

    final PoolOfflineDataStoringService poolOfflineDataStoringService;
    final PoolOfflineDataFetchingService poolOfflineDataFetchingService;

    public PoolOfflineDataSchedule(
            PoolOfflineDataStoringService poolOfflineDataStoringService,
            PoolOfflineDataFetchingService poolOfflineDataFetchingService) {
        this.poolOfflineDataStoringService = poolOfflineDataStoringService;
        this.poolOfflineDataFetchingService = poolOfflineDataFetchingService;
    }

    @Transactional
    @Scheduled(fixedDelayString = "${jobs.pool-offline-data.fetch.delay}")
    public void fetchPoolOffline() {
        log.info("-----------Start job fetch pool offline data-----------");
        final var startTime = System.currentTimeMillis();
        List<PoolData> poolDataList = poolOfflineDataFetchingService.fetchPoolOfflineData();
        List<PoolData> successPools = poolDataList.stream().filter(PoolData::isValid).toList();
        List<PoolData> failPools = new ArrayList<>(poolDataList.stream().filter(poolData -> !poolData.isValid()).toList());
        poolOfflineDataFetchingService.fetchPoolOfflineDataLogo(successPools);
        poolOfflineDataStoringService.saveSuccessPoolOfflineData(successPools);
        poolOfflineDataStoringService.saveFailOfflineData(failPools);
        log.info(
                "----------End job fetch pool offline data, time taken: {} ms----------",
                (System.currentTimeMillis() - startTime));
    }
}
