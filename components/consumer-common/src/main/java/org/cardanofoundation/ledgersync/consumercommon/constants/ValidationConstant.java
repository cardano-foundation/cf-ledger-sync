package org.cardanofoundation.ledgersync.consumercommon.constants;

import java.math.BigInteger;

public class ValidationConstant {

  private ValidationConstant() {

  }

  public static final long MINUS_ONE = -1L;
  public static final double TWO = 2D;
  public static final double INT_65_BYTES = 65D;
  public static final double LOVE_LACE_BYTES = 64D;
  public static final double WORD_128_BYTES = 128D;
  public static final int ADDRESS_MAX_BYTES = 58;
  public static final int ASSET_MAX_BYTES = 32;
  public static final int HASH_28 = 56;
  public static final int HASH_32 = 64;

  public static BigInteger getMaxInt65() {
    return BigInteger.valueOf((long) Math.pow(TWO, INT_65_BYTES));
  }

  public static BigInteger getMinInt65() {
    return BigInteger.valueOf(MINUS_ONE).multiply(getMaxInt65());
  }

  public static BigInteger getMaxLoveLace() {
    return BigInteger.valueOf((long) Math.pow(TWO, LOVE_LACE_BYTES));
  }

  public static BigInteger getMaxWord128() {
    return BigInteger.valueOf((long) Math.pow(TWO, WORD_128_BYTES));
  }
}
