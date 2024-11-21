package org.cardanofoundation.ledgersync.scheduler.config;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PreDestroy;

@Configuration
public class ExecutorConfig {
    
    private final ExecutorService executor;
    private final Lock lock;
    
    public ExecutorConfig() {
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
        this.lock = new ReentrantLock();
    }
    
    @Bean(name = "offChainExecutor")
    public ExecutorService virtualThreadExecutor() {
        return executor;
    }

    @Bean(name = "virtualThreadLock")
    public Lock executorLock() {
        return lock;
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