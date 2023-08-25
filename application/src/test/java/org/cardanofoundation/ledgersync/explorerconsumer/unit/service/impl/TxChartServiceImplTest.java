package org.cardanofoundation.ledgersync.explorerconsumer.unit.service.impl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.explorer.consumercommon.entity.TxChart;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.TxTimeProjection;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxChartRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.TxChartServiceImpl;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class TxChartServiceImplTest {

  @Mock
  TxRepository txRepository;

  @Mock
  TxChartRepository txChartRepository;

  @Captor
  ArgumentCaptor<Collection<TxChart>> txChartsCaptor;

  TxChartServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new TxChartServiceImpl(txRepository, txChartRepository);
  }

  @Test
  @DisplayName("Should skip tx chart handling if no tx list were found")
  void shouldSkipTxChartHandlingTest() {
    victim.handleTxChart(null);
    Mockito.verify(txRepository, Mockito.times(1))
        .findTxWithTimeByIdGreaterThanOrNull(Mockito.nullable(Long.class));
    Mockito.verifyNoInteractions(txChartRepository);

    Tx tx = Mockito.mock(Tx.class);
    victim.handleTxChart(tx);
    Mockito.verify(txRepository, Mockito.times(1))
        .findTxWithTimeByIdGreaterThanOrNull(Mockito.notNull());
    Mockito.verifyNoInteractions(txChartRepository);
  }

  @Test
  @DisplayName("Should handle new tx chart successfully")
  void shouldHandleNewTxChartSuccessfullyTest() {
    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
    Timestamp timestamp = Timestamp.valueOf(now);
    Timestamp timestamp2 = Timestamp.valueOf(now.plusMinutes(1));
    TxTimeProjection txTimeProjection = givenTxTimeProjection(timestamp, true, false, false);
    TxTimeProjection txTimeProjection2 = givenTxTimeProjection(timestamp, false, false, true);
    TxTimeProjection txTimeProjection3 = givenTxTimeProjection(timestamp2, false, true, false);

    Mockito.when(txRepository.findTxWithTimeByIdGreaterThanOrNull(Mockito.any()))
        .thenReturn(List.of(txTimeProjection, txTimeProjection2, txTimeProjection3));

    victim.handleTxChart(Mockito.mock(Tx.class));

    Mockito.verify(txChartRepository, Mockito.times(1))
        .findAllByMinuteBetween(Mockito.any(), Mockito.any());
    Mockito.verify(txChartRepository, Mockito.times(1))
        .saveAll(txChartsCaptor.capture());
    Mockito.verifyNoMoreInteractions(txChartRepository);
    Mockito.verify(txRepository, Mockito.times(1))
        .findTxWithTimeByIdGreaterThanOrNull(Mockito.any());
    Mockito.verifyNoMoreInteractions(txRepository);

    List<TxChart> txCharts = new ArrayList<>(txChartsCaptor.getValue());
    TxChart txChart = txCharts.get(0);
    verifyTxChartTime(txChart, timestamp);
    Assertions.assertEquals(1L, txChart.getTxWithSc());
    Assertions.assertEquals(0L, txChart.getTxWithMetadataWithoutSc());
    Assertions.assertEquals(1L, txChart.getTxSimple());
    Assertions.assertEquals(2L, txChart.getTxCount());

    txChart = txCharts.get(1);
    verifyTxChartTime(txChart, timestamp2);
    Assertions.assertEquals(0L, txChart.getTxWithSc());
    Assertions.assertEquals(1L, txChart.getTxWithMetadataWithoutSc());
    Assertions.assertEquals(0L, txChart.getTxSimple());
    Assertions.assertEquals(1L, txChart.getTxCount());
  }

  @Test
  @DisplayName("Should handle tx chart with existing tx chart data successfully")
  void shouldHandleTxChartWithExistingDataSuccessfullyTest() {
    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
    Timestamp timestamp = Timestamp.valueOf(now);
    Timestamp timestamp2 = Timestamp.valueOf(now.plusMinutes(1));
    TxChart givenTxChart = givenTxChart(timestamp, 2L, 1L, 0L, 1L);
    TxChart givenTxChart2 = givenTxChart(timestamp2, 3L, 0L, 1L, 2L);
    TxTimeProjection txTimeProjection = givenTxTimeProjection(timestamp, true, false, false);
    TxTimeProjection txTimeProjection2 = givenTxTimeProjection(timestamp, false, false, true);
    TxTimeProjection txTimeProjection3 = givenTxTimeProjection(timestamp2, false, true, false);

    Mockito.when(txChartRepository.findAllByMinuteBetween(Mockito.any(), Mockito.any()))
        .thenReturn(List.of(givenTxChart, givenTxChart2));
    Mockito.when(txRepository.findTxWithTimeByIdGreaterThanOrNull(Mockito.any()))
        .thenReturn(List.of(txTimeProjection, txTimeProjection2, txTimeProjection3));

    victim.handleTxChart(Mockito.mock(Tx.class));

    Mockito.verify(txChartRepository, Mockito.times(1))
        .findAllByMinuteBetween(Mockito.any(), Mockito.any());
    Mockito.verify(txChartRepository, Mockito.times(1))
        .saveAll(txChartsCaptor.capture());
    Mockito.verifyNoMoreInteractions(txChartRepository);
    Mockito.verify(txRepository, Mockito.times(1))
        .findTxWithTimeByIdGreaterThanOrNull(Mockito.any());
    Mockito.verifyNoMoreInteractions(txRepository);

    List<TxChart> txCharts = new ArrayList<>(txChartsCaptor.getValue());
    TxChart txChart = txCharts.get(0);
    verifyTxChartTime(txChart, timestamp);
    Assertions.assertEquals(2L, txChart.getTxWithSc());
    Assertions.assertEquals(0L, txChart.getTxWithMetadataWithoutSc());
    Assertions.assertEquals(2L, txChart.getTxSimple());
    Assertions.assertEquals(4L, txChart.getTxCount());

    txChart = txCharts.get(1);
    verifyTxChartTime(txChart, timestamp2);
    Assertions.assertEquals(0L, txChart.getTxWithSc());
    Assertions.assertEquals(2L, txChart.getTxWithMetadataWithoutSc());
    Assertions.assertEquals(2L, txChart.getTxSimple());
    Assertions.assertEquals(4L, txChart.getTxCount());
  }

  @Test
  @DisplayName("Should skip tx chart rollback if no txs were supplied")
  void shouldSkipTxChartRollbackTest() {
    Collection<Tx> txs = Collections.emptyList();
    victim.rollbackTxChart(txs);
    Mockito.verifyNoInteractions(txRepository);
    Mockito.verifyNoInteractions(txChartRepository);

    txs = List.of(Mockito.mock(Tx.class));
    victim.rollbackTxChart(txs);
    Mockito.verify(txRepository, Mockito.times(1))
        .findTxWithTimeByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txRepository);
    Mockito.verifyNoInteractions(txChartRepository);
  }

  @Test
  @DisplayName("Should rollback tx charts successfully")
  void shouldRollbackTxChartSuccessfullyTest() {
    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
    Timestamp timestamp = Timestamp.valueOf(now);
    Timestamp timestamp2 = Timestamp.valueOf(now.plusMinutes(1));
    TxChart givenTxChart = givenTxChart(timestamp, 3L, 1L, 1L, 1L);
    TxChart givenTxChart2 = givenTxChart(timestamp2, 3L, 0L, 1L, 2L);
    TxTimeProjection txTimeProjection = givenTxTimeProjection(timestamp, true, false, false);
    TxTimeProjection txTimeProjection2 = givenTxTimeProjection(timestamp, false, false, true);
    TxTimeProjection txTimeProjection3 = givenTxTimeProjection(timestamp2, false, true, false);

    Mockito.when(txChartRepository.findAllByMinuteBetween(Mockito.any(), Mockito.any()))
        .thenReturn(List.of(givenTxChart, givenTxChart2));
    Mockito.when(txRepository.findTxWithTimeByTxIn(Mockito.anyCollection()))
        .thenReturn(List.of(txTimeProjection, txTimeProjection2, txTimeProjection3));

    Tx tx = Mockito.mock(Tx.class);
    victim.rollbackTxChart(List.of(tx));

    Mockito.verify(txChartRepository, Mockito.times(1))
        .findAllByMinuteBetween(Mockito.any(), Mockito.any());
    Mockito.verify(txChartRepository, Mockito.times(1))
        .saveAll(txChartsCaptor.capture());
    Mockito.verifyNoMoreInteractions(txChartRepository);
    Mockito.verify(txRepository, Mockito.times(1))
        .findTxWithTimeByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txRepository);

    List<TxChart> txCharts = new ArrayList<>(txChartsCaptor.getValue());
    TxChart txChart = txCharts.get(0);
    verifyTxChartTime(txChart, timestamp);
    Assertions.assertEquals(0L, txChart.getTxWithSc());
    Assertions.assertEquals(1L, txChart.getTxWithMetadataWithoutSc());
    Assertions.assertEquals(0L, txChart.getTxSimple());
    Assertions.assertEquals(1L, txChart.getTxCount());

    txChart = txCharts.get(1);
    verifyTxChartTime(txChart, timestamp2);
    Assertions.assertEquals(0L, txChart.getTxWithSc());
    Assertions.assertEquals(0L, txChart.getTxWithMetadataWithoutSc());
    Assertions.assertEquals(2L, txChart.getTxSimple());
    Assertions.assertEquals(2L, txChart.getTxCount());
  }

  @Test
  @DisplayName("Should rollback and delete tx charts successfully")
  void shouldRollbackAndDeleteTxChartSuccessfullyTest() {
    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
    Timestamp timestamp = Timestamp.valueOf(now);
    Timestamp timestamp2 = Timestamp.valueOf(now.plusMinutes(1));
    TxChart givenTxChart = givenTxChart(timestamp, 2L, 1L, 0L, 1L);
    TxChart givenTxChart2 = givenTxChart(timestamp2, 3L, 0L, 1L, 2L);
    TxTimeProjection txTimeProjection = givenTxTimeProjection(timestamp, true, false, false);
    TxTimeProjection txTimeProjection2 = givenTxTimeProjection(timestamp, false, false, true);
    TxTimeProjection txTimeProjection3 = givenTxTimeProjection(timestamp2, false, true, false);

    Mockito.when(txChartRepository.findAllByMinuteBetween(Mockito.any(), Mockito.any()))
        .thenReturn(List.of(givenTxChart, givenTxChart2));
    Mockito.when(txRepository.findTxWithTimeByTxIn(Mockito.anyCollection()))
        .thenReturn(List.of(txTimeProjection, txTimeProjection2, txTimeProjection3));

    Tx tx = Mockito.mock(Tx.class);
    victim.rollbackTxChart(List.of(tx));

    Mockito.verify(txChartRepository, Mockito.times(1))
        .findAllByMinuteBetween(Mockito.any(), Mockito.any());
    Mockito.verify(txChartRepository, Mockito.times(1))
        .saveAll(txChartsCaptor.capture());
    Mockito.verify(txChartRepository, Mockito.times(1))
        .deleteAll(txChartsCaptor.capture());
    Mockito.verifyNoMoreInteractions(txChartRepository);
    Mockito.verify(txRepository, Mockito.times(1))
        .findTxWithTimeByTxIn(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(txRepository);

    List<TxChart> txCharts = txChartsCaptor.getAllValues()
        .stream()
        .flatMap(Collection::stream)
        .toList();
    TxChart txChart = txCharts.get(1);
    verifyTxChartTime(txChart, timestamp);
    Assertions.assertEquals(0L, txChart.getTxWithSc());
    Assertions.assertEquals(0L, txChart.getTxWithMetadataWithoutSc());
    Assertions.assertEquals(0L, txChart.getTxSimple());
    Assertions.assertEquals(0L, txChart.getTxCount());

    txChart = txCharts.get(0);
    verifyTxChartTime(txChart, timestamp2);
    Assertions.assertEquals(0L, txChart.getTxWithSc());
    Assertions.assertEquals(0L, txChart.getTxWithMetadataWithoutSc());
    Assertions.assertEquals(2L, txChart.getTxSimple());
    Assertions.assertEquals(2L, txChart.getTxCount());
  }

  private void verifyTxChartTime(TxChart target, Timestamp timeForComparing) {
    LocalDateTime txTimeLdt = timeForComparing.toLocalDateTime();
    int minutePart = txTimeLdt.getMinute();
    int hourPart = txTimeLdt.getHour();
    int dayOfMonthPart = txTimeLdt.getDayOfMonth();
    int dayOfYearPart = txTimeLdt.getDayOfYear();
    long txTimestampInMinute = timestampToEpochTrimSecond(timeForComparing);
    long txTimestampInHour = txTimestampInMinute - (minutePart * 60);
    long txTimestampInDay = txTimestampInHour - (hourPart * 60 * 60);
    long txTimestampInMonth = txTimestampInDay - ((dayOfMonthPart - 1) * 24 * 60 * 60);
    long txTimestampInYear = txTimestampInDay - ((dayOfYearPart - 1) * 24 * 60 * 60);

    Assertions.assertEquals(txTimestampInMinute, target.getMinute().longValue());
    Assertions.assertEquals(txTimestampInHour, target.getHour().longValue());
    Assertions.assertEquals(txTimestampInDay, target.getDay().longValue());
    Assertions.assertEquals(txTimestampInMonth, target.getMonth().longValue());
    Assertions.assertEquals(txTimestampInYear, target.getYear().longValue());
  }

  private static TxTimeProjection givenTxTimeProjection(Timestamp txTime, Boolean txWithSc,
                                                        Boolean txWithMetadataWithoutSc,
                                                        Boolean simpleTx) {
    TxTimeProjection txTimeProjection = Mockito.mock(TxTimeProjection.class);
    Mockito.when(txTimeProjection.getTxTime()).thenReturn(txTime);
    Mockito.when(txTimeProjection.getTxWithSc()).thenReturn(txWithSc);
    Mockito.when(txTimeProjection.getTxWithMetadataWithoutSc()).thenReturn(txWithMetadataWithoutSc);
    Mockito.when(txTimeProjection.getSimpleTx()).thenReturn(simpleTx);

    return txTimeProjection;
  }

  private static long timestampToEpochTrimSecond(Timestamp timestamp) {
    LocalDateTime timestampLdt = timestamp.toLocalDateTime();
    long timeInEpoch = timestampLdt.toEpochSecond(ZoneOffset.UTC);
    int secondPart = timestampLdt.getSecond();
    return timeInEpoch - secondPart;
  }

  private static TxChart givenTxChart(Timestamp timestamp, long txCount, long txWithSc,
                                      long txWithMetadataWithoutSc, long txSimple) {
    LocalDateTime txTimeLdt = timestamp.toLocalDateTime();
    int minutePart = txTimeLdt.getMinute();
    int hourPart = txTimeLdt.getHour();
    int dayOfMonthPart = txTimeLdt.getDayOfMonth();
    int dayOfYearPart = txTimeLdt.getDayOfYear();
    long txTimestampInMinute = timestampToEpochTrimSecond(timestamp);
    long txTimestampInHour = txTimestampInMinute - (minutePart * 60);
    long txTimestampInDay = txTimestampInHour - (hourPart * 60 * 60);
    long txTimestampInMonth = txTimestampInDay - ((dayOfMonthPart - 1) * 24 * 60 * 60);
    long txTimestampInYear = txTimestampInDay - ((dayOfYearPart - 1) * 24 * 60 * 60);

    return TxChart.builder()
        .minute(BigInteger.valueOf(txTimestampInMinute))
        .hour(BigInteger.valueOf(txTimestampInHour))
        .day(BigInteger.valueOf(txTimestampInDay))
        .month(BigInteger.valueOf(txTimestampInMonth))
        .year(BigInteger.valueOf(txTimestampInYear))
        .txCount(txCount)
        .txWithMetadataWithoutSc(txWithMetadataWithoutSc)
        .txWithSc(txWithSc)
        .txSimple(txSimple)
        .build();
  }
}
