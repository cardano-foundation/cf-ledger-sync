package org.cardanofoundation.ledgersync.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxChart;
import org.cardanofoundation.ledgersync.projection.TxTimeProjection;
import org.cardanofoundation.ledgersync.repository.TxChartRepository;
import org.cardanofoundation.ledgersync.repository.TxRepository;
import org.cardanofoundation.ledgersync.service.TxChartService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class TxChartServiceImpl implements TxChartService {

    TxRepository txRepository;
    TxChartRepository txChartRepository;

    @Override
    public void handleTxChart(Tx latestSavedTx) {
        List<TxTimeProjection> txListWithTime = Objects.isNull(latestSavedTx)
                ? txRepository.findTxWithTimeByIdGreaterThanOrNull(null)
                : txRepository.findTxWithTimeByIdGreaterThanOrNull(
                latestSavedTx.getId());

        if (CollectionUtils.isEmpty(txListWithTime)) {
            return;
        }

        updateTxChart(txListWithTime, false);
    }

    private void updateTxChart(List<TxTimeProjection> txListWithTime, boolean subtract) {
        // Initialize variables to bypass warnings
        int txListSize = txListWithTime.size();
        Timestamp minTxTimestamp = txListWithTime.get(0).getTxTime();
        Timestamp maxTxTimestamp = txListWithTime.get(txListSize - 1).getTxTime();

        for (TxTimeProjection txWithTime : txListWithTime) {
            Timestamp txTime = txWithTime.getTxTime();
            if (Objects.isNull(minTxTimestamp) || txTime.compareTo(minTxTimestamp) < 0) {
                minTxTimestamp = txTime;
            }

            if (Objects.isNull(maxTxTimestamp) || txTime.compareTo(maxTxTimestamp) > 0) {
                maxTxTimestamp = txTime;
            }
        }

        long minTxTime = timestampToEpochTrimSecond(minTxTimestamp);
        long maxTxTime = timestampToEpochTrimSecond(maxTxTimestamp);
        BigInteger minTxTimeInMinute = BigInteger.valueOf(minTxTime);
        BigInteger maxTxTimeInMinute = BigInteger.valueOf(maxTxTime);
        getAndUpdateTxCharts(minTxTimeInMinute, maxTxTimeInMinute, txListWithTime, subtract);
    }

    private void getAndUpdateTxCharts(
            BigInteger minTxTimeInMinute, BigInteger maxTxTimeInMinute,
            List<TxTimeProjection> txListWithTime, boolean subtract) {

        Map<BigInteger, TxChart> txChartInMinute = txChartRepository
                .findAllByMinuteBetween(minTxTimeInMinute, maxTxTimeInMinute)
                .stream()
                .collect(Collectors.toMap(
                        TxChart::getMinute,
                        Function.identity(),
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
        List<TxChart> txChartsForDeletion = new ArrayList<>();

        txListWithTime.forEach(
                txWithTime -> updateTxChart(subtract, txChartInMinute, txChartsForDeletion, txWithTime));

        if (!CollectionUtils.isEmpty(txChartInMinute)) {
            txChartRepository.saveAll(txChartInMinute.values());
        }

        if (!CollectionUtils.isEmpty(txChartsForDeletion)) {
            txChartRepository.deleteAll(txChartsForDeletion);
        }
    }

    private void updateTxChart(boolean subtract, Map<BigInteger, TxChart> txChartInMinute,
                               List<TxChart> txChartsForDeletion, TxTimeProjection txWithTime) {
        Timestamp txTime = txWithTime.getTxTime();
        LocalDateTime txTimeLdt = txTime.toLocalDateTime();
        int minutePart = txTimeLdt.getMinute();
        int hourPart = txTimeLdt.getHour();
        int dayOfMonthPart = txTimeLdt.getDayOfMonth();
        int dayOfYearPart = txTimeLdt.getDayOfYear();

        // Divide this with 1000 to trim milliseconds part
        long txTimestampInMinute = timestampToEpochTrimSecond(txTime);
        long txTimestampInHour = txTimestampInMinute - (minutePart * 60);
        long txTimestampInDay = txTimestampInHour - (hourPart * 60 * 60);
        long txTimestampInMonth = txTimestampInDay - ((dayOfMonthPart - 1) * 24 * 60 * 60);
        long txTimestampInYear = txTimestampInDay - ((dayOfYearPart - 1) * 24 * 60 * 60);

        BigInteger txTimestampInMinuteBigInt = BigInteger.valueOf(txTimestampInMinute);
        boolean txChartExist = txChartInMinute.containsKey(txTimestampInMinuteBigInt);
        TxChart txChart = txChartExist
                ? txChartInMinute.get(txTimestampInMinuteBigInt)
                : buildTxChart(txTimestampInMinute, txTimestampInHour,
                txTimestampInDay, txTimestampInMonth, txTimestampInYear);

        int addCount = subtract ? -1 : 1;
        if (Boolean.TRUE.equals(txWithTime.getSimpleTx())) {
            txChart.setTxSimple(txChart.getTxSimple() + addCount);
        }

        if (Boolean.TRUE.equals(txWithTime.getTxWithMetadataWithoutSc())) {
            txChart.setTxWithMetadataWithoutSc(txChart.getTxWithMetadataWithoutSc() + addCount);
        }

        if (Boolean.TRUE.equals(txWithTime.getTxWithSc())) {
            txChart.setTxWithSc(txChart.getTxWithSc() + addCount);
        }

        txChart.setTxCount(txChart.getTxCount() + addCount);
        if (txChart.getTxCount() == 0) {
            txChartInMinute.remove(txTimestampInMinuteBigInt);
            txChartsForDeletion.add(txChart);
            return;
        }

        if (!txChartExist) {
            txChartInMinute.put(txTimestampInMinuteBigInt, txChart);
        }
    }

    private long timestampToEpochTrimSecond(Timestamp timestamp) {
        LocalDateTime timestampLdt = timestamp.toLocalDateTime();
        long timeInEpoch = timestampLdt.toEpochSecond(ZoneOffset.UTC);
        int secondPart = timestampLdt.getSecond();
        return timeInEpoch - secondPart;
    }

    private static TxChart buildTxChart(long txTimestampInMinute, long txTimestampInHour,
                                        long txTimestampInDay, long txTimestampInMonth,
                                        long txTimestampInYear) {
        return TxChart.builder()
                .minute(BigInteger.valueOf(txTimestampInMinute))
                .hour(BigInteger.valueOf(txTimestampInHour))
                .day(BigInteger.valueOf(txTimestampInDay))
                .month(BigInteger.valueOf(txTimestampInMonth))
                .year(BigInteger.valueOf(txTimestampInYear))
                .txCount(BigInteger.ZERO.longValue())
                .txWithMetadataWithoutSc(BigInteger.ZERO.longValue())
                .txWithSc(BigInteger.ZERO.longValue())
                .txSimple(BigInteger.ZERO.longValue())
                .build();
    }

    @Override
    public void rollbackTxChart(Collection<Tx> txs) {
        if (CollectionUtils.isEmpty(txs)) {
            log.info("No txs were supplied, skipping tx chart rollback");
            return;
        }

        List<TxTimeProjection> txListWithTime = txRepository.findTxWithTimeByTxIn(txs);
        if (CollectionUtils.isEmpty(txListWithTime)) {
            log.info("No txs with time records found, skipping tx chart rollback");
            return;
        }

        updateTxChart(txListWithTime, true);
        log.info("Tx chart rollback finished");
    }
}
