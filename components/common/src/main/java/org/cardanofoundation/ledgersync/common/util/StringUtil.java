package org.cardanofoundation.ledgersync.common.util;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

public final class StringUtil {

  private StringUtil() {

  }

  public static boolean isUtf8(byte[] input) {
    if (input == null) {
      return false;
    }
    CharsetDecoder decoder =
        StandardCharsets.UTF_8.newDecoder();
    try {
      decoder.decode(
          ByteBuffer.wrap(input));
    } catch (CharacterCodingException ex) {
      return false;
    }
    return true;
  }
}
