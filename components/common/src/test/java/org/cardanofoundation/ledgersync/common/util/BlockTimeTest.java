package org.cardanofoundation.ledgersync.common.util;

import org.cardanofoundation.ledgersync.common.common.constant.Constant;
import org.cardanofoundation.ledgersync.common.util.Util;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BlockTimeTest {

  @Test
  void calculateTestNetTime_1_031() {
    compareTimeStamp(1, 1L
        , LocalDateTime.of(2019, 7, 25, 2, 3, 56)
        , Constant.TESTNET);
  }

  private static void compareTimeStamp(long blockNumber,long actualSlot, LocalDateTime expectedDate, int network) {
    Timestamp calculateBlockTime = Util.calculateBlockTime(blockNumber,actualSlot, 0, network);

    expectedDate.atZone(ZoneId.of("UTC"));
    Timestamp expectedUTC = Timestamp.valueOf(expectedDate);

    Assertions.assertEquals(expectedUTC, calculateBlockTime);
  }

  @Test
  void calculateTestNetTimeSlot_1_032() {
    compareTimeStamp(2,2L,
        LocalDateTime.of(2019, 7, 25, 2, 4, 16)
        , Constant.TESTNET);
  }

  @Test
  void calculateTestNetTimeSlot_1_037() {
    compareTimeStamp(7, 7L,
        LocalDateTime.of(2019, 7, 25, 2, 5, 56),
        Constant.TESTNET);
  }

  @Test
  void calculateTestNetTimeSlot_21_599() {
    LocalDateTime expectedDate = LocalDateTime.of(2019,7, 29, 20, 19, 56);

    compareTimeStamp(19539, 20569,
        expectedDate,
        Constant.TESTNET);
  }

  @Test
  void calculateTestNetTimeBlock78_9_19_Slot_79_949() {

    LocalDateTime expectedDate = LocalDateTime.of(2019, 8, 12, 8, 29, 56);

    compareTimeStamp(78_9_19,78_919,
        expectedDate,
        Constant.TESTNET);
  }

  @Test
  void calculateTestNetTimeBlock_125_610_Slot_126_640() {

    LocalDateTime expectedDate = LocalDateTime.of(2019, 8, 23,3, 53, 36);

    compareTimeStamp(125_610,125_610,
        expectedDate,
        Constant.TESTNET);
  }

  @Test
  void calculateTestNetTimeBlock_125_611_Slot_126_642() {

    LocalDateTime expectedDate = LocalDateTime.of(2019, 8, 23, 3, 54, 16);

    compareTimeStamp(125_611,125_612,
        expectedDate,
        Constant.TESTNET);
  }

}
