package org.cardanofoundation.ledgersync.explorerconsumer.util;

import com.bloxbean.cardano.client.transaction.spec.Asset;

import java.math.BigInteger;

public class ConsumerAssetUtil {
    public static String getFingerPrint(byte[] assetName, String policyId) {
        return org.cardanofoundation.ledgersync.common.util.AssetUtil.getFingerPrint(assetName, policyId);
    }

    public static byte[] assetNameToBytes(String assetName) {
        return new Asset(assetName, BigInteger.ZERO).getNameAsBytes();
    }
}
