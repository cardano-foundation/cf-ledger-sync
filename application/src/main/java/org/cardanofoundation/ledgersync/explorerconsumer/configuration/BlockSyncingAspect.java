package org.cardanofoundation.ledgersync.explorerconsumer.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.cardanofoundation.ledgersync.explorerconsumer.service.BlockSyncingManager;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class BlockSyncingAspect {
    private final BlockSyncingManager blockSyncingManager;

    @Before("@annotation(org.cardanofoundation.ledgersync.explorerconsumer.configuration.BlockEventHandling)")
    public void beforeBlockEventHandling(JoinPoint joinPoint) {
        blockSyncingManager.setIsEventBeingProcessed(true);
    }

    @After("@annotation(org.cardanofoundation.ledgersync.explorerconsumer.configuration.BlockEventHandling)")
    public void afterBlockEventHandling(JoinPoint joinPoint) {
        blockSyncingManager.setLastEventProcessedTime(System.currentTimeMillis());
        blockSyncingManager.setIsEventBeingProcessed(false);
    }
}
