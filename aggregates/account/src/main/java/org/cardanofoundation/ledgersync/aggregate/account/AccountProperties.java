package org.cardanofoundation.ledgersync.aggregate.account;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "ledger-sync.aggregate.account", ignoreUnknownFields = true)
public class AccountProperties {
    private boolean enabled = true;
}
