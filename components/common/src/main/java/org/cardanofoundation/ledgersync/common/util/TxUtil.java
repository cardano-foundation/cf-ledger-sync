package org.cardanofoundation.ledgersync.common.util;

import com.bloxbean.cardano.client.crypto.Blake2bUtil;

public final class TxUtil {

  private TxUtil() {

  }

  public static String calculateTxHash(byte[] bytes) {
    try {
      return HexUtil.encodeHexString(
          Blake2bUtil.blake2bHash256(bytes));
    } catch (Exception e) {
      return null;
    }
  }
}
