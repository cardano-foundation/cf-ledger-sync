package org.cardanofoundation.ledgersync.verifier.data.app.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * Configuration class for setting up the datasource, entity manager factory, and transaction manager
 * for the LedgerSync application.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "ledgerSyncEntityManagerFactory",
        transactionManagerRef = "ledgerSyncTransactionManager",
        basePackages = {"org.cardanofoundation.ledgersync.verifier.data.app.repository.ledgersync"})
public class LedgerSyncDatasourceConfig {

    private final MultiDataSourceProperties multiDataSourceProperties;

    /**
     * Constructor for LedgerSyncDatasourceConfig.
     *
     * @param multiDataSourceProperties The properties for configuring multiple datasources.
     */
    public LedgerSyncDatasourceConfig(MultiDataSourceProperties multiDataSourceProperties) {
        this.multiDataSourceProperties = multiDataSourceProperties;
    }

    /**
     * Creates the primary datasource bean for the LedgerSync application.
     *
     * @return The primary datasource for LedgerSync.
     */
    @Primary
    @Bean(name = "ledgerSyncDataSource")
    public DataSource ledgerSyncDataSource() {
        return multiDataSourceProperties.buildDataSource(
                multiDataSourceProperties.getDatasourceLedgerSync());
    }

    /**
     * Creates the entity manager factory bean for the LedgerSync application.
     *
     * @param builder   The EntityManagerFactoryBuilder.
     * @param dataSource The primary datasource for LedgerSync.
     * @return The LocalContainerEntityManagerFactoryBean for LedgerSync.
     */
    @Primary
    @Bean(name = "ledgerSyncEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
            EntityManagerFactoryBuilder builder,
            @Qualifier("ledgerSyncDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages(
                        "org.cardanofoundation.ledgersync.consumercommon.entity",
                        "org.cardanofoundation.ledgersync.verifier.data.app.entity.ledgersync",
                        "org.cardanofoundation.ledgersync.consumercommon.enumeration",
                        "org.cardanofoundation.ledgersync.consumercommon.validation")
                .build();
    }

    /**
     * Creates the transaction manager bean for the LedgerSync application.
     *
     * @param ledgerSyncEntityManagerFactory The entity manager factory for LedgerSync.
     * @return The transaction manager for LedgerSync.
     */
    @Primary
    @Bean(name = "ledgerSyncTransactionManager")
    public PlatformTransactionManager ledgerSyncTransactionManager(
            @Qualifier("ledgerSyncEntityManagerFactory")
            LocalContainerEntityManagerFactoryBean ledgerSyncEntityManagerFactory) {
        return new JpaTransactionManager(
                Objects.requireNonNull(ledgerSyncEntityManagerFactory.getObject()));
    }
}
