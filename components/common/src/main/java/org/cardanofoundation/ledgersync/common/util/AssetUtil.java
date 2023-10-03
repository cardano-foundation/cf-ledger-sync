package org.cardanofoundation.ledgersync.common.util;

import com.bloxbean.cardano.client.util.HexUtil;

public final class AssetUtil {

  private AssetUtil() {
  }

  public static String getFingerPrint(byte[] name, String policy) {
    String hexName = HexUtil.encodeHexString(name, false);
    return com.bloxbean.cardano.client.util.AssetUtil.calculateFingerPrint(policy, hexName);
  }
}
