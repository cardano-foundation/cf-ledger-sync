package org.cardanofoundation.ledgersync.util;

public class LedgerSyncAssetUtil {
    public static String getFingerPrint(byte[] assetName, String policyId) {
        return org.cardanofoundation.ledgersync.common.util.AssetUtil.getFingerPrint(assetName, policyId);
    }
}
