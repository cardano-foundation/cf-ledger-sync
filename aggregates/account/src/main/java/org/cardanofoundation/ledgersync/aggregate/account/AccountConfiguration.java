package org.cardanofoundation.ledgersync.aggregate.account;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ConditionalOnProperty(
        prefix = "ledger-sync.aggregate.account",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@Configuration
@EnableConfigurationProperties(AccountProperties.class)
@ComponentScan(basePackages = {"org.cardanofoundation.ledgersync.aggregate.account"})
@EnableJpaRepositories(basePackages = {"org.cardanofoundation.ledgersync.aggregate.account"})
@EntityScan(basePackages = {"org.cardanofoundation.ledgersync.aggregate.account"})
@EnableTransactionManagement
@Slf4j
public class AccountConfiguration {
    @Autowired
    AccountProperties accountProperties;
}
