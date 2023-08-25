package org.cardanofoundation.ledgersync.explorerconsumer.util;

import com.bloxbean.cardano.client.plutus.spec.PlutusData;
import com.bloxbean.cardano.client.util.HexUtil;
import com.bloxbean.cardano.yaci.core.model.Datum;

public class DatumUtil {
    public static String toDatumHash(Datum datum) {
        try {
            PlutusData plutusData = PlutusData.deserialize(HexUtil.decodeHexString(datum.getCbor()));
            return plutusData.getDatumHash();
        } catch (Exception e) {
            throw new IllegalStateException("Invalid datum cbor");
        }
    }
}
