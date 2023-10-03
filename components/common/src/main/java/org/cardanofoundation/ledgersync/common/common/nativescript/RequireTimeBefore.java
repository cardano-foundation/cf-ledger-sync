package org.cardanofoundation.ledgersync.common.common.nativescript;

import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.UnsignedInteger;
import com.bloxbean.cardano.client.exception.CborDeserializationException;
import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigInteger;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This script class is for "RequireTimeBefore" expression
 */
@Data
@NoArgsConstructor
public class RequireTimeBefore implements NativeScript {

    private final ScriptType type = ScriptType.before;
    private BigInteger slot;

    public RequireTimeBefore(BigInteger slot) {
        this.slot = slot;
    }

    @Override
    public DataItem serializeAsDataItem() {
        Array array = new Array();
        array.add(new UnsignedInteger(5));
        array.add(new UnsignedInteger(slot));
        return array;
    }

    public static RequireTimeBefore deserialize(Array array) throws CborDeserializationException {
        BigInteger slot = ((UnsignedInteger)array.getDataItems().get(1)).getValue();
        return new RequireTimeBefore(slot);
    }

    public static RequireTimeBefore deserialize(JsonNode jsonNode) throws CborDeserializationException {
        BigInteger slot = jsonNode.get("slot").bigIntegerValue();
        return new RequireTimeBefore(slot);
    }
}
