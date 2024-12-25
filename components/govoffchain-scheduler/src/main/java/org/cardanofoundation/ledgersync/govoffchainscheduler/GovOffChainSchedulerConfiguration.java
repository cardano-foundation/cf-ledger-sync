package org.cardanofoundation.ledgersync.govoffchainscheduler;

import org.cardanofoundation.ledgersync.govoffchainscheduler.jobs.OffChainDataScheduler;

import lombok.extern.slf4j.Slf4j;

import org.cardanofoundation.ledgersync.govoffchainscheduler.service.OffChainPersistService;
import org.cardanofoundation.ledgersync.govoffchainscheduler.service.OffChainRetryDataErrorService;
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
@EnableConfigurationProperties(GovOffChainSchedulerProperties.class)
@ComponentScan(basePackages = {"org.cardanofoundation.ledgersync.govoffchainscheduler"})
@EnableJpaRepositories(basePackages = {"org.cardanofoundation.ledgersync.govoffchainscheduler"})
@EntityScan(basePackages = {"org.cardanofoundation.ledgersync.govoffchainscheduler",
                            "com.bloxbean.cardano.yaci.store.core",
                            "com.bloxbean.cardano.yaci.store.governance"})
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@Slf4j
public class GovOffChainSchedulerConfiguration {

    @Autowired
    GovOffChainSchedulerProperties properties;

    @Bean
    public OffChainDataScheduler offChainVotingDataScheduler(
        OffChainPersistService offChainPersistService,
        OffChainRetryDataErrorService offChainDataFetchingErrorService,
        OffChainDataProperties offChainDataProperties) {
        log.info("<<< Enable OffChainDataScheduler >>>");
        log.info("OffChainDataScheduler: fixed delay time {} sec", offChainDataProperties.getFixedDelay());
        return new OffChainDataScheduler(offChainPersistService, offChainDataFetchingErrorService, offChainDataProperties);
    }

    @Bean
    OffChainDataProperties offChainDataProperties() {
        OffChainDataProperties offChainDataProperties = new OffChainDataProperties();
        offChainDataProperties.setFixedDelay(properties.getOffChainData().getFixedDelay());
        offChainDataProperties.setInitialDelay(properties.getOffChainData().getInitialDelay());
        offChainDataProperties.setFixedDelayFetchError(properties.getOffChainData().getFixedDelayFetchError());
        offChainDataProperties.setInitialDelayFetchError(properties.getOffChainData().getInitialDelayFetchError());
        offChainDataProperties.setRetryCount(properties.getOffChainData().getRetryCount());
        return offChainDataProperties;
    }

}
