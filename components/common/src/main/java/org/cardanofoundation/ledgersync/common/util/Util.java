package org.cardanofoundation.ledgersync.common.util;

import org.cardanofoundation.ledgersync.common.common.constant.Constant;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;


public final class Util {

  public static final long MAINNET_START_TIME = 1_506_203_091;
  public static final long MAINNET_BYRON_BLOCK = 4_490_511;
  public static final long TESTNET_START_TIME = 1_564_020_236;
  public static final long TESTNET_BYRON_BLOCK = 1_597_133;
  public static final int BYRON_PROCESS_TIME = 20;
  public static final int SHELLY_PROCESS_TIME = 1;

  private static final Map<Integer,Long> mStartTime = Map.ofEntries(
    Map.entry(Constant.TESTNET, TESTNET_START_TIME),
    Map.entry(Constant.MAINNET, MAINNET_START_TIME)
  );

  private static final Map<Integer,Long> mBlock = Map.ofEntries(
      Map.entry(Constant.TESTNET, TESTNET_BYRON_BLOCK),
      Map.entry(Constant.MAINNET, MAINNET_BYRON_BLOCK)
  );

  private Util() {

  }

  public static int getIntegerFromBytes(byte[] data) {
    return Integer.parseUnsignedInt(HexUtil.encodeHexString(data), 16);
  }

  public static long getLongFromBytes(byte[] data) {
    return Long.parseLong(HexUtil.encodeHexString(data), 16);
  }

  public static byte[] putNumberToBytes(long num, int size) {
    byte[] result = new byte[size];
    int j = 0;
    for (int i = size - 1; i >= 0; i--) {
      result[i] = (byte) ((int) num >>> (j * 8));
      j++;
    }
    return result;
  }
  // Concat 2 byte array with order a -> b

  public static byte[] concat(byte[] a, byte[] b) {
    byte[] c = new byte[a.length + b.length];
    System.arraycopy(a, 0, c, 0, a.length);
    System.arraycopy(b, 0, c, a.length, b.length);
    return c;
  }

  /**
   * @param blockNumber     block number in network
   * @param epochSlot       slot in epoch not include EBB block
   * @param byronActualSlot actual end slot in network not include EBB block
   * @param network         network magic of testnet: 1097911063 or mainnet: 764824073
   * @return block start time in Timestamp for test
   */
  public static Timestamp calculateBlockTime(long blockNumber, long epochSlot, long byronActualSlot,
      int network) {

    return Timestamp.valueOf(
        LocalDateTime.ofEpochSecond(getBlockTime(blockNumber, epochSlot, byronActualSlot, network),
            0, ZoneOffset.ofHours(0))
    );
  }

  /**
   * This method calculate block start time from network reference to
   * <a href="https://github.com/CardanoSolutions/ogmios/issues/3">GitHub Issue</a>
   * POSIX time for main-net is 1_506_203_091, and testnet is 1_564_020_236* In order to find to
   * <b>actualSlot</b>.
   * <p>Call N is number of blocks per epoch (not include EBB block), k is current epoch number.
   * <p>The actual slot of block:
   * <p>actualSlot = sum(N(0), N(1), N(2) ... N(k - 1) + slotInEpoch
   * <p>If actualSlot smaller than or equals to byron end block in network
   * <b>blockTime = actualSlot *  byronProcessTime</b>
   * <p>If actualSlot greater than to byron end block in network.
   * <p><b>otherBlocks = actualSlot -  byronEndBlock.</b>
   * <p><b>blockTime = (byronEndBlock *  byronProcessTime) + (otherBlocks *
   * ShellyProcessTime)</b>
   * <p>otherBlocks: number of blocks Shelly, Babbage ...
   * <p>blockTime: block start time<p>
   *
   * @param blockNumber     block number in network
   * @param slot            actual slot in network not include EBB block
   * @param network         network magic of test-net: 1097911063 or main-net: 764824073
   * @param byronActualSlot actual end slot in network not include EBB block
   * @return block start time in seconds unit
   */
  public static long getBlockTime(long blockNumber, long slot, long byronActualSlot, int network) {
    long time = getStartTime(network);
    // Shelly above
    long lastByronBlock = getLastByronBlock(network);

    if (slot < 0) {
      throw new IllegalArgumentException("epochSlot number must be positive ");
    }


    final long actualSlot = slot - 1;

    if (blockNumber > lastByronBlock) {
      final long otherBlocks = actualSlot - byronActualSlot;
      time = time + (byronActualSlot * BYRON_PROCESS_TIME);
      time = time + (otherBlocks * SHELLY_PROCESS_TIME);
    } else {
      time = time + (actualSlot * BYRON_PROCESS_TIME);
    }
    return time;
  }

  public static byte[] getBytes(String string) {
    return string.getBytes(StandardCharsets.UTF_8);
  }


  public static long getStartTime(int networkMagic){
    if(mStartTime.containsKey(networkMagic))
      return mStartTime.get(networkMagic);
    return 0;
  }

  public static long getLastByronBlock(int networkMagic){
    if(mBlock.containsKey(networkMagic))
      return mBlock.get(networkMagic);
    return 0;
  }
  
}
