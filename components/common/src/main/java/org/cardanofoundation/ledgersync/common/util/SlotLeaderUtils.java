package org.cardanofoundation.ledgersync.common.util;

import com.bloxbean.cardano.client.crypto.Blake2bUtil;
import org.cardanofoundation.ledgersync.common.common.constant.Constant;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

public final class SlotLeaderUtils {


  private SlotLeaderUtils() {

  }

  /**
   * Find slot leader hash from public key in cddl
   * <p>
   * Haskell code:
   * <p>
   * slotLeaderHash = BS.take 28
   * <p>
   * . Crypto.abstractHashToBytes . Crypto.hashRaw .LBS.fromStrict . Crypto.xpubPublicKey
   * <p>
   * . Crypto.unVerificationKey . Byron.headerGenesisKey . Byron.blockHeader
   * <p>
   * Read reference documents to dig into Haskell function
   * <p>
   * @see <a href="https://input-output-hk.github.io/ouroboros-network/cardano-crypto-wrapper/Cardano-Crypto-Hashing.html#g:1">Hashing</a>
   * @see <a href="https://input-output-hk.github.io/ouroboros-network/cardano-crypto-wrapper/Cardano-Crypto-Signing-VerificationKey.html#v:unVerificationKey">VerificationKey</a>
   * @see <a href="https://cips.cardano.org/cips/cip16/">Crypto Format</a>
   *
   * @param publicKey @see <a href="https://github.com/input-output-hk/cardano-ledger/blob/4dfa33c71dbca591ccee4b05824a7be5509368fd/eras/byron/cddl-spec/byron.cddl#L173">pubKey</a>
   * @return
   */
  public static String getByronSlotLeader(String publicKey) {
    var xpub = Arrays.copyOf(Hex.decode(publicKey.getBytes(StandardCharsets.UTF_8)),
        Constant.PUBLIC_KEY_LENGTH);
    return HexUtil.encodeHexString(
        Arrays.copyOf(Blake2bUtil.blake2bHash256(xpub), Constant.SLOT_LEADER_LENGTH));
  }


  /**
   * Find slot leader hash from shelley era
   * <p>
   * unKeyHashRaw :: Ledger.KeyHash d era -> ByteString
   * <p>
   * unKeyHashRaw (Ledger.KeyHash kh) = Crypto.hashToBytes kh
   *
   * @param issureVkey @see <a href="https://github.com/input-output-hk/cardano-ledger/blob/4dfa33c71dbca591ccee4b05824a7be5509368fd/eras/babbage/test-suite/cddl-files/babbage.cddl#L31">issureVkey</a>
   * @return
   */
  public static  String getAfterByronSlotLeader(String issureVkey){
     return HexUtil.encodeHexString(Blake2bUtil.blake2bHash224(Hex.decode(issureVkey)));
  }
}
