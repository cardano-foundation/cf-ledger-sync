package org.cardanofoundation.ledgersync.explorerconsumer.util;

import co.nstant.in.cbor.model.Array;
import com.bloxbean.cardano.client.common.cbor.CborSerializationUtil;
import com.bloxbean.cardano.client.exception.CborDeserializationException;
import com.bloxbean.cardano.client.util.HexUtil;
import com.bloxbean.cardano.yaci.core.model.Redeemer;
import org.cardanofoundation.ledgersync.common.common.Datum;
import org.cardanofoundation.ledgersync.common.util.JsonUtil;

public class RedeemerWrapper {
    private Redeemer yaciRedeemer;
    private com.bloxbean.cardano.client.plutus.spec.Redeemer redeemer;

    public RedeemerWrapper(Redeemer yaciRedeemer) {
        this.yaciRedeemer = yaciRedeemer;
        try {
            this.redeemer = com.bloxbean.cardano.client.plutus.spec.Redeemer.deserialize((Array) CborSerializationUtil.deserialize(HexUtil.decodeHexString(yaciRedeemer.getCbor())));
        } catch (CborDeserializationException e) {
            throw new IllegalStateException("Failed to deserialize redeemer", e);
        }
    }

    public String getDataHash() {
        try {
            return redeemer.getData().getDatumHash();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to serialize redeemer data", e);
        }
    }

    public String getCbor() {
        return yaciRedeemer.getCbor();
    }

    public com.bloxbean.cardano.client.plutus.spec.Redeemer getRedeemer() {
        return redeemer;
    }

    public Datum getDatum() {
        return Datum.builder()
                .hash(getDataHash())
                .cbor(redeemer.getData().serializeToHex())
                .json(JsonUtil.getPrettyJson(redeemer.getData()))
                .build();
    }
}
