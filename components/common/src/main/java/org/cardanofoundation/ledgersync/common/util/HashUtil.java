package org.cardanofoundation.ledgersync.common.util;

import com.bloxbean.cardano.client.crypto.Blake2bUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class HashUtil {

  private HashUtil() {
    
  }

  public static String generateByronHeaderHash(byte[] headerBytes, int eraVariant){
    byte[] prefix = new byte[]{(byte) 0x82, (byte) eraVariant};
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    try {
      outputStream.write(prefix);
      outputStream.write(headerBytes);
    } catch (IOException e) {
      return "";
    }

    return HexUtil.encodeHexString(Blake2bUtil.blake2bHash256(outputStream.toByteArray()));
  }
}
