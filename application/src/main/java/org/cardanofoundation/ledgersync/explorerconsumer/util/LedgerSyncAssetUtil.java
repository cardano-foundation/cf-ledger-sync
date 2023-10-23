package org.cardanofoundation.ledgersync.explorerconsumer.util;

import com.bloxbean.cardano.client.transaction.spec.Asset;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class LedgerSyncAssetUtil {
    public static String getFingerPrint(byte[] assetName, String policyId) {
        return org.cardanofoundation.ledgersync.common.util.AssetUtil.getFingerPrint(assetName, policyId);
    }
    public static byte[] assetNameToBytes(String assetName) {
        try {
            return new Asset(assetName, BigInteger.ZERO).getNameAsBytes();
        } catch (Exception e) {
            return assetName.getBytes(StandardCharsets.UTF_8);
        }
    }
}
