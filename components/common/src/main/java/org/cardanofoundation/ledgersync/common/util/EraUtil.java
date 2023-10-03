package org.cardanofoundation.ledgersync.common.util;


import org.cardanofoundation.ledgersync.common.common.Era;

public final class EraUtil {
  private EraUtil(){

  }
  public static Era getEra(int value) {
    switch (value) {
      case -1:
        return Era.ROLLBACK;
      case 0:
      case 1:
        return Era.BYRON;
      case 2:
        return Era.SHELLEY;
      case 3:
        return Era.ALLEGRA;
      case 4:
        return Era.MARY;
      case 5:
        return Era.ALONZO;
      case 6:
        return Era.BABBAGE;
      default:
        return null;
    }
  }
}
