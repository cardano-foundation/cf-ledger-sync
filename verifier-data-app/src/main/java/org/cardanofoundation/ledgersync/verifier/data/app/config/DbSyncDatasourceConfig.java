package org.cardanofoundation.ledgersync.verifier.data.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "dbSyncEntityManagerFactory",
        transactionManagerRef = "dbSyncTransactionManager",
        basePackages = {"org.cardanofoundation.ledgersync.verifier.data.app.repository.dbsync"})
@RequiredArgsConstructor
public class DbSyncDatasourceConfig {

    private final MultiDataSourceProperties multiDataSourceProperties;

    @Bean(name = "dbSyncDataSource")
    public DataSource ledgerSyncDataSource() {
        return multiDataSourceProperties.buildDataSource(
                multiDataSourceProperties.getDatasourceDbSync());
    }

    @Bean(name = "dbSyncEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
            EntityManagerFactoryBuilder builder, @Qualifier("dbSyncDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages(
                        "org.cardanofoundation.ledgersync.verifier.data.app.entity.dbsync")
                .build();
    }

    @Bean(name = "dbSyncTransactionManager")
    public PlatformTransactionManager dbSyncTransactionManager(
            @Qualifier("dbSyncEntityManagerFactory")
            LocalContainerEntityManagerFactoryBean ledgerSyncEntityManagerFactory) {
        return new JpaTransactionManager(
                Objects.requireNonNull(ledgerSyncEntityManagerFactory.getObject()));
    }
}
