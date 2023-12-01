package org.cardanofoundation.ledgersync.common.common.constant;

import java.util.Arrays;

public final class Constant {

  private Constant() {

  }

  //Network magic
  public static final int TESTNET = 1097911063;
  public static final int PREPROD_TESTNET = 1;
  public static final int PREVIEW_TESTNET = 2;
  public static final int SANCHONET = 4;
  public static final int MAINNET = 764824073;

  public static final String LOVELACE = "LOVELACE";
  public static final byte[] LOVELACE_BYTE = LOVELACE.getBytes();

  public static boolean isLoveLace (byte[] data){
    return Arrays.equals(LOVELACE_BYTE,data);
  }

  public static final int PUBLIC_KEY_LENGTH = 32;
  public static final int SLOT_LEADER_LENGTH = 28;

  public static boolean isTestnet(int networkMagic) {
    return networkMagic == TESTNET
        || networkMagic == PREPROD_TESTNET
        || networkMagic == PREVIEW_TESTNET
        || networkMagic == SANCHONET;
  }
}
