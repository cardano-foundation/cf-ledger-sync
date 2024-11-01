package org.cardanofoundation.ledgersync.scheduler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PreDestroy;

@Configuration
public class ExecutorConfig {
    
    private final ExecutorService executor;
    
    public ExecutorConfig() {
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }
    
    @Bean(name = "offChainExecutor")
    public ExecutorService virtualThreadExecutor() {
        return executor;
    }
    
    @PreDestroy
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
        }
    }
} 