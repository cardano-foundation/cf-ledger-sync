package org.cardanofoundation.ledgersync.converter;

import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.Map;
import co.nstant.in.cbor.model.UnsignedInteger;
import com.bloxbean.cardano.client.crypto.Blake2bUtil;
import lombok.experimental.UtilityClass;
import org.cardanofoundation.ledgersync.common.common.cost.mdl.PlutusV1Keys;
import org.cardanofoundation.ledgersync.common.common.cost.mdl.PlutusV2Keys;
import org.cardanofoundation.ledgersync.common.util.CborSerializationUtil;
import org.cardanofoundation.ledgersync.common.util.HexUtil;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.IntStream;

import static org.cardanofoundation.ledgersync.service.impl.genesis.GenesisDataServiceImpl.convertObjecToBigInteger;

@UtilityClass
public class CostModelConverter {

    public static final String PLUTUS_V1 = "PlutusV1";
    public static final String PLUTUS_V2 = "PlutusV2";

    public static String getCostModelHashFromGenesis(java.util.Map genesisMap) {
        Map mapItem = new Map();

        if (genesisMap.containsKey(PLUTUS_V1)) {
            mapItem.put(new UnsignedInteger(BigInteger.ZERO),
                    getPlutusArray((java.util.Map<String, Object>) genesisMap.get(PLUTUS_V1),
                            PlutusV1Keys.KEYS));
        }

        if (genesisMap.containsKey(PLUTUS_V2)) {
            mapItem.put(new UnsignedInteger(BigInteger.ONE),
                    getPlutusArray((java.util.Map<String, Object>) genesisMap.get(PLUTUS_V1),
                            PlutusV2Keys.KEYS));
        }

        var cbor = CborSerializationUtil.serialize(mapItem);
        return HexUtil.encodeHexString(Blake2bUtil.blake2bHash256(cbor));
    }

    private static Array getPlutusArray(java.util.Map genesisMap, List<String> plutusKey) {
        Array plutus = new Array(plutusKey.size());
        IntStream.range(BigInteger.ZERO.intValue(), plutusKey.size())
                .forEach(index -> {
                    plutus.add(
                            new UnsignedInteger(convertObjecToBigInteger(genesisMap.get(plutusKey.get(index)))));
                });
        return plutus;
    }
}
