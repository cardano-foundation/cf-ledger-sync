package org.cardanofoundation.ledgersync.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "store")
public class StoreProperties {
    private Assets assets = new Assets();
    private Metadata metadata = new Metadata();

    @Getter
    @Setter
    public static final class Assets {
        private boolean enabled = true;
    }

    @Getter
    @Setter
    public static final class Metadata {
        private boolean enabled = true;
    }

}

