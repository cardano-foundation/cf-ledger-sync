package org.cardanofoundation.ledgersync.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring")
public class FlywayConfigurationProperties {
    private FlywayProperties flyway;
}
