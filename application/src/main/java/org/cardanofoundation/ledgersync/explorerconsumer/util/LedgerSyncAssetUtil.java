package org.cardanofoundation.ledgersync.explorerconsumer.util;

import java.nio.charset.StandardCharsets;

public class LedgerSyncAssetUtil {
    public static String getFingerPrint(byte[] assetName, String policyId) {
        return org.cardanofoundation.ledgersync.common.util.AssetUtil.getFingerPrint(assetName, policyId);
    }

    public static byte[] assetNameToBytes(String assetName) {
        if (assetName != null && !assetName.isEmpty()) {
            return assetName.getBytes(StandardCharsets.UTF_8);
        }
        return new byte[0];
    }
}
