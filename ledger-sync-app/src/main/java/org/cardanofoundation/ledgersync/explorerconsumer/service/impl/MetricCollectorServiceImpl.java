package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.ledgersync.common.common.Era;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.constant.ConsumerConstant;
import org.cardanofoundation.ledgersync.explorerconsumer.service.MetricCollectorService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class MetricCollectorServiceImpl implements MetricCollectorService {

    final MeterRegistry meterRegistry;
    AtomicLong currentBlockNumber;
    AtomicInteger currentEra;

    final Map<Era, Timer> eraProcessingTimerMap = new HashMap<>();

    Counter rollbackCounter;
    Counter blockProcessingCounter;
    AtomicReference<Float> networkSyncPercent;
    Long timeStartEra;

    @PostConstruct
    private void init() {
        setupMeterMetrics();
    }

    private void setupMeterMetrics() {

        this.currentBlockNumber = new AtomicLong(0);
        this.currentEra = new AtomicInteger(0);
        this.networkSyncPercent = new AtomicReference<>(0f);

        Gauge.builder("consumer.current.block.number", currentBlockNumber, AtomicLong::get)
                .description("Current block number").register(this.meterRegistry);

        Gauge.builder("consumer.current.era", currentEra, AtomicInteger::get).description("Current era")
                .register(this.meterRegistry);

        Gauge.builder("consumer.network.sync.percent", networkSyncPercent, AtomicReference<Float>::get)
                .description("Network sync percent").register(this.meterRegistry);

        this.rollbackCounter = Counter.builder("consumer.count.rollback")
                .description("Count rollback block").register(this.meterRegistry);
        this.blockProcessingCounter = Counter.builder("consumer.block.processing.count")
                .description("Count block processing").register(this.meterRegistry);

        this.timeStartEra = System.currentTimeMillis();
    }

    public void collectRollbackMetric() {
        this.rollbackCounter.increment(1);
    }


    public void collectEraAndEpochProcessingMetric(AggregatedBlock block) {
        Era currentBlockEra = block.getEra();
        if (Objects.isNull(currentBlockEra)) {
            return;
        }
        Long now = System.currentTimeMillis();

        Timer.builder("consumer.era.and.epoch.processing.timer")
                .description("Processing era and epoch timer")
                .tag("era", currentBlockEra.toString())
                .tag("epoch", String.valueOf(block.getEpochNo()))
                .register(this.meterRegistry).record(now - timeStartEra, TimeUnit.MILLISECONDS);
        timeStartEra = now;
    }

    public void collectCurrentBlockMetric(AggregatedBlock block) {
        Long blockNo = block.getBlockNo();
        if (Objects.isNull(blockNo)) {
            return;
        }

        this.currentBlockNumber.set(blockNo);
    }

    public void collectCurrentEraMetric(AggregatedBlock block) {
        Era era = block.getEra();
        if (Objects.isNull(era)) {
            return;
        }
        this.currentEra.set(era.getValue());
    }


    public void collectNetworkSyncPercentMetric(AggregatedBlock block) {
        Long startDate = ConsumerConstant.getByronKnownTime(block.getNetwork());
        if (Objects.isNull(startDate)) {
            return;
        }
        long now = System.currentTimeMillis() / 1000;

        long blockTime = block.getBlockTime().getTime() / 1000;

        float percent = (float) (blockTime - startDate) / (now - startDate);
        this.networkSyncPercent.set(percent * 100);
    }

    public void collectCountBlockProcessingMetric(AggregatedBlock firstBlock, AggregatedBlock lastBlock) {

        if (Objects.isNull(firstBlock) || Objects.isNull(lastBlock)) {
            return;
        }

        Long firstBlockNo = firstBlock.getBlockNo();
        Long lastBlockNo = lastBlock.getBlockNo();

        if (Objects.isNull(firstBlockNo) || Objects.isNull(lastBlockNo)) {
            return;
        }

        long count = lastBlockNo - firstBlockNo + 1;
        if (count <= 0) {
            return;
        }
        this.blockProcessingCounter.increment(count);
    }
}
