package org.cardanofoundation.ledgersync.common.util;

import org.cardanofoundation.ledgersync.common.common.constant.Constant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MultiAssetTest {

  @Test
  void CheckEqualLoveLace() {
    String assetLoveLace = "LOVELACE";
    assertTrue(Constant.isLoveLace(assetLoveLace.getBytes()));
  }

}
