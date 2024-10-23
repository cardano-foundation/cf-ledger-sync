package org.cardanofoundation.ledgersync.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.scheduler.jobs.OffChainDataScheduler;
import org.cardanofoundation.ledgersync.scheduler.jobs.PoolOfflineDataScheduler;
import org.cardanofoundation.ledgersync.scheduler.service.OffChainDataStoringService;
import org.cardanofoundation.ledgersync.scheduler.service.OffChainRetryDataErrorService;
import org.cardanofoundation.ledgersync.scheduler.service.PoolOfflineDataFetchingService;
import org.cardanofoundation.ledgersync.scheduler.service.PoolOfflineDataStoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ConditionalOnProperty(
        prefix = "ledger-sync.scheduler",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@Configuration
@EnableConfigurationProperties(SchedulerProperties.class)
@ComponentScan(basePackages = {"org.cardanofoundation.ledgersync.scheduler"})
@EnableJpaRepositories(basePackages = {"org.cardanofoundation.ledgersync.scheduler"})
@EntityScan(basePackages = {"org.cardanofoundation.ledgersync.scheduler"})
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@Slf4j
public class SchedulerConfiguration {

    @Autowired
    SchedulerProperties properties;

    @Bean
    public PoolOfflineDataScheduler poolOfflineDataScheduler(PoolOfflineDataStoringService poolOfflineDataStoringService,
                                                             PoolOfflineDataFetchingService poolOfflineDataFetchingService,
                                                             PoolOfflineDataProperties poolOfflineDataProperties) {
        log.info("<<< Enable PoolOfflineDataScheduler >>>");
        log.info("PoolOfflineDataScheduler: fixed delay time {} sec", poolOfflineDataProperties.getFixedDelay());
        return new PoolOfflineDataScheduler(poolOfflineDataStoringService, poolOfflineDataFetchingService, poolOfflineDataProperties);
    }

    @Bean
    public OffChainDataScheduler offChainVotingDataScheduler(
        OffChainDataStoringService offChainDataStoringService,
        OffChainRetryDataErrorService offChainDataFetchingErrorService,
        OffChainDataProperties offChainDataProperties) {
        log.info("<<< Enable OffChainDataScheduler >>>");
        log.info("OffChainDataScheduler: fixed delay time {} sec", offChainDataProperties.getFixedDelay());
        return new OffChainDataScheduler(offChainDataStoringService, offChainDataFetchingErrorService, offChainDataProperties);
    }

    @Bean
    PoolOfflineDataProperties poolOfflineDataProperties() {
        PoolOfflineDataProperties poolOfflineDataProperties = new PoolOfflineDataProperties();
        poolOfflineDataProperties.setFixedDelay(properties.getPoolOfflineData().getFixedDelay());
        poolOfflineDataProperties.setInitialDelay(properties.getPoolOfflineData().getInitialDelay());
        return poolOfflineDataProperties;
    }

    @Bean
    OffChainDataProperties offChainDataProperties() {
        OffChainDataProperties offChainDataProperties = new OffChainDataProperties();
        offChainDataProperties.setFixedDelay(properties.getOffChainData().getFixedDelay());
        offChainDataProperties.setInitialDelay(properties.getOffChainData().getInitialDelay());
        offChainDataProperties.setFixedDelayFetchError(properties.getOffChainData().getFixedDelayFetchError());
        offChainDataProperties.setInitialDelayFetchError(properties.getOffChainData().getInitialDelayFetchError());
        return offChainDataProperties;
    }

}
