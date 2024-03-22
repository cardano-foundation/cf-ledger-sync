package org.cardanofoundation.ledgersync.verifier.data.app.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Configuration class for setting up multiple datasources using HikariCP.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
@ConfigurationProperties(prefix = "multi-datasource")
public class MultiDataSourceProperties {
    DataSourceConfig datasourceLedgerSync;
    DataSourceConfig datasourceDbSync;

    /**
     * Inner class representing the configuration for a single datasource.
     */
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class DataSourceConfig {
        HikariConfig hikariConfig;
    }

    /**
     * Build and return a datasource using the provided configuration.
     *
     * @param dataSourceConfig The configuration for the datasource.
     * @return The configured datasource.
     */
    public DataSource buildDataSource(DataSourceConfig dataSourceConfig) {
        return new HikariDataSource(dataSourceConfig.getHikariConfig());
    }
}
