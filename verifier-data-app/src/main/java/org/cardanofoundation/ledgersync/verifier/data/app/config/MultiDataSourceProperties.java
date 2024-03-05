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

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
@ConfigurationProperties(prefix = "multi-datasource")
public class MultiDataSourceProperties {
    DataSourceConfig datasourceLedgerSync;
    DataSourceConfig datasourceDbSync;
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class DataSourceConfig {
        HikariConfig hikariConfig;
    }

    public DataSource buildDataSource(DataSourceConfig dataSourceConfig) {
        return new HikariDataSource(dataSourceConfig.getHikariConfig());
    }
}
