package org.cardanofoundation.ledgersync.scheduler.config;

import java.util.concurrent.Executor;

import org.cardanofoundation.ledgersync.scheduler.SchedulerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@ConditionalOnProperty(
        prefix = "ledger-sync.scheduler",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@Configuration
public class AsyncConfiguration implements AsyncConfigurer, SchedulingConfigurer {

    @Autowired
    SchedulerProperties properties;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getAsyncConfig().getCore());
        executor.setMaxPoolSize(properties.getAsyncConfig().getMax());
        executor.setThreadNamePrefix(properties.getAsyncConfig().getName());
        executor.initialize();
        return executor;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(properties.getAsyncConfig().getCore());
        taskScheduler.initialize();
        taskRegistrar.setTaskScheduler(taskScheduler);
    }
}
