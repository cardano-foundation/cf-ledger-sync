package org.cardanofoundation.ledgersync.util;

import java.util.Random;

public final class TestStringUtils {

  private TestStringUtils() {}

  public static String generateRandomHash(int length) {
    Random random = new Random();
    StringBuilder sb = new StringBuilder();

    while (sb.length() < length) {
      sb.append(Integer.toHexString(random.nextInt()));
    }

    return sb.toString();
  }
}
