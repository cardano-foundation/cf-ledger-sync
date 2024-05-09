package org.cardanofoundation.ledgersync.verifier.data.app;

import org.cardanofoundation.ledgersync.verifier.data.app.model.AdaPotsComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AdaPotsComparisonKey;
import org.cardanofoundation.ledgersync.verifier.data.app.service.AdaPotsService;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.RepeatedTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
class AdaPotsTests {

  @Autowired
  @Qualifier("adaPotsLSServiceImpl")
  private AdaPotsService adaPotServiceLS;

  @Autowired
  @Qualifier("adaPotsDbSyncServiceImpl")
  private AdaPotsService adaPotServiceDbSync;

  @Value("${amount.ada_pots.amount_ada_pots}")
  private int amountAdaPots;

  @RepeatedTest(value = 20, name = RepeatedTest.LONG_DISPLAY_NAME)
  void compare_random_of_AdaPots() throws SQLException {
    List<Long> epochList = adaPotServiceLS.getRandomEpochNos(amountAdaPots);

    Map<AdaPotsComparisonKey, AdaPotsComparison> adaPotsComparisonMapLS =
        adaPotServiceLS.getMapAdaPotsFromEpochNos(epochList);
    Map<AdaPotsComparisonKey, AdaPotsComparison> adaPotsComparisonMapDbSync =
        adaPotServiceDbSync.getMapAdaPotsFromEpochNos(epochList);

    assertEquals(adaPotsComparisonMapLS.size(), adaPotsComparisonMapDbSync.size());

    for (Map.Entry<AdaPotsComparisonKey, AdaPotsComparison> entry :
        adaPotsComparisonMapLS.entrySet()) {
      AdaPotsComparisonKey key = entry.getKey();
      AdaPotsComparison value = entry.getValue();
      AdaPotsComparison dbSyncValue = adaPotsComparisonMapDbSync.get(key);

      assertAll(
          "Expected values do not match for key: " + key.getEpochNo(),
          () ->
              assertEquals(
                  dbSyncValue.getTreasury(),
                  value.getTreasury(),
                  String.format(
                      "Treasury assertion failed for key: %s, Expected Treasury: %s, Actual Treasury: %s",
                      key.getEpochNo(), value.getTreasury(), dbSyncValue.getTreasury())),
          () ->
              assertEquals(
                  dbSyncValue.getReserves(),
                  value.getReserves(),
                  String.format(
                      "Reserves assertion failed for key: %s, Expected Reserves: %s, Actual Reserves: %s",
                      key.getEpochNo(), value.getReserves(), dbSyncValue.getReserves())),
          () ->
              assertEquals(
                  dbSyncValue.getRewards(),
                  value.getRewards(),
                  String.format(
                      "Rewards assertion failed for key: %s, Expected Rewards: %s, Actual Rewards: %s",
                      key.getEpochNo(), value.getRewards(), dbSyncValue.getRewards())));
    }
  }
}
