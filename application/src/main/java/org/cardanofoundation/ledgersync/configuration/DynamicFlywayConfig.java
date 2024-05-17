package org.cardanofoundation.ledgersync.configuration;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;


@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({StoreProperties.class, FlywayConfigurationProperties.class})
public class DynamicFlywayConfig {
    private final DataSource dataSource;
    private final StoreProperties storeProperties;
    private final FlywayConfigurationProperties flywayConfigurationProperties;

    @Bean
    public Flyway flywayConfig() throws SQLException {
        FlywayProperties flywayProperties = flywayConfigurationProperties.getFlyway();
        List<String> locations = flywayProperties.getLocations();

        String vendor = dataSource.getConnection().getMetaData().getDatabaseProductName().toLowerCase();
        locations.add("classpath:db/store/" + vendor);

        if (!storeProperties.getAssets().isEnabled()) {
            locations.add("classpath:db/migration/ledgersync/assets");
        }

        if (!storeProperties.getMetadata().isEnabled()) {
            locations.add("classpath:db/migration/ledgersync/metadata");
        }

        if (!storeProperties.getBlocks().isEnabled()) {
            locations.add("classpath:db/migration/ledgersync/blocks");
        }

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .sqlMigrationPrefix(flywayProperties.getSqlMigrationPrefix())
                .sqlMigrationSeparator(flywayProperties.getSqlMigrationSeparator())
                .sqlMigrationSuffixes(flywayProperties.getSqlMigrationSuffixes().toArray(new String[0]))
                .group(flywayProperties.isGroup())
                .validateOnMigrate(flywayProperties.isValidateOnMigrate())
                .schemas(flywayProperties.getSchemas().toArray(new String[0]))
                .defaultSchema(flywayProperties.getDefaultSchema())
                .locations(locations.toArray(new String[0]))
                .createSchemas(flywayProperties.isCreateSchemas())
                .outOfOrder(flywayProperties.isOutOfOrder())
                .load();
        flyway.migrate();
        return flyway;
    }

}


