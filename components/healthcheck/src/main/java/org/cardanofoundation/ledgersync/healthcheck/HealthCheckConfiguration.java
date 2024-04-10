package org.cardanofoundation.ledgersync.healthcheck;

import org.cardanofoundation.ledgersync.healthcheck.service.HealthCheckCachingService;
import org.cardanofoundation.ledgersync.healthcheck.service.HealthStatusService;
import org.cardanofoundation.ledgersync.healthcheck.service.impl.HealthStatusServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = {"ledgersync.healthcheck.enabled"},
        havingValue = "true"
)
@EnableConfigurationProperties(HealthCheckProperties.class)
@ComponentScan(basePackages = {"org.cardanofoundation.ledgersync.healthcheck"})
public class HealthCheckConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HealthStatusService healthStatusService(HealthCheckCachingService healthCheckCachingService,
                                                   HealthCheckProperties healthCheckProperties) {
        return new HealthStatusServiceImpl(healthCheckCachingService, healthCheckProperties);
    }

}
