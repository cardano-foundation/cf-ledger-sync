package org.cardanofoundation.ledgersync.scheduler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "ledger-sync.scheduler", ignoreUnknownFields = true)
public class SchedulerProperties {
    private boolean enabled = true;
    private PoolOfflineData poolOfflineData = new PoolOfflineData();
    private AsyncConfig asyncConfig = new AsyncConfig();

    @Getter
    @Setter
    public static final class PoolOfflineData {
        private long fixedDelay = 172800L;
        private long initialDelay = 20000L;
    }

    @Getter
    @Setter
    public static final class AsyncConfig {
        private int core = 10;
        private int max = 12;
        private String name = "Scheduler-Executorxx-";
    }

}