package org.cardanofoundation.ledgersync.explorerconsumer.configuration;

import org.jooq.conf.MappedSchema;
import org.jooq.conf.RenderMapping;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.stereotype.Component;

@Component
public class JooqConfigurationCustomizer implements DefaultConfigurationCustomizer {

    @Value("${spring.jpa.properties.hibernate.default_schema}")
    String schema;

    @Override
    public void customize(DefaultConfiguration configuration) {
        configuration.settings()
                .withRenderSchema(true)
                .withRenderMapping(
                        new RenderMapping()
                                .withSchemata(new MappedSchema().withInput("")
                                        .withOutput(schema))
                );
    }
}
