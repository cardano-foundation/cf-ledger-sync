package org.cardanofoundation.ledgersync.common.util;

import java.util.Objects;

public final class HexUtil {

  private static final String HEX_PREFIX = "0x";
  private static final int HEXA = 16;

  private static final int NUMBER_255 = 0xF;

  private HexUtil() {

  }

  public static String encodeHexString(byte[] byteArray) {
    if (byteArray == null) {
      return null;
    }

    return encodeHexString(byteArray, false);
  }

  public static String encodeHexString(byte[] byteArray, boolean withPrefix) {
    if (Objects.isNull(byteArray)) {
      return null;
    }

    StringBuilder hexStringBuffer = new StringBuilder();

    for (byte b : byteArray) {
      hexStringBuffer.append(byteToHex(b));
    }
    String hexString = hexStringBuffer.toString();

    if (withPrefix) {
      return HEX_PREFIX + hexString;
    } else {
      return hexString;
    }
  }

  public static byte[] decodeHexString(String hexString) {

    if (Objects.isNull(hexString)) {
      return new byte[0];
    }

    if (hexString.startsWith(HEX_PREFIX)) {
      hexString = hexString.substring(2);
    }

    if (hexString.length() % 2 == 1) {
      throw new IllegalArgumentException("Invalid hexadecimal String supplied. " + hexString);
    }

    byte[] bytes = new byte[hexString.length() / 2];
    for (int i = 0; i < hexString.length(); i += 2) {
      bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
    }
    return bytes;
  }

  public static byte hexToByte(String hexString) {
    int firstDigit = toDigit(hexString.charAt(0));
    int secondDigit = toDigit(hexString.charAt(1));
    return (byte) ((firstDigit << 4) + secondDigit);
  }

  public static String byteToHex(byte num) {
    char[] hexDigits = new char[2];
    hexDigits[0] = Character.forDigit((num >> 4) & NUMBER_255, HEXA);
    hexDigits[1] = Character.forDigit((num & NUMBER_255), HEXA);
    return new String(hexDigits);
  }

  public static String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  private static int toDigit(char hexChar) {
    int digit = Character.digit(hexChar, HEXA);
    if (digit == -1) {
      throw new IllegalArgumentException("Invalid Hexadecimal Character: " + hexChar);
    }
    return digit;
  }

}

