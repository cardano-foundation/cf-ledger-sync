package org.cardanofoundation.ledgersync.explorerconsumer.multiasset;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.cardanofoundation.ledgersync.common.common.constant.Constant;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

@Profile("test")
 class MultiAssetTest {

  @Test
   void CheckEqualLoveLace() {
    String assetLoveLace = "LOVELACE";
    assertEquals(true, Constant.isLoveLace(assetLoveLace.getBytes()));
  }

}
