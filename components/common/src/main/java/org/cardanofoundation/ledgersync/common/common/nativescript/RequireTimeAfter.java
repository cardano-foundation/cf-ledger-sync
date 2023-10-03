package org.cardanofoundation.ledgersync.common.common.nativescript;

import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.UnsignedInteger;
import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigInteger;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This script class is for "RequireTimeAfter" expression
 */
@Data
@NoArgsConstructor
public class RequireTimeAfter implements NativeScript {

    private final ScriptType type = ScriptType.after;
    private BigInteger slot;

    public RequireTimeAfter(BigInteger slot) {
        this.slot = slot;
    }

    @Override
    public DataItem serializeAsDataItem() {
        Array array = new Array();
        array.add(new UnsignedInteger(4));
        array.add(new UnsignedInteger(slot));
        return array;
    }

    public static RequireTimeAfter deserialize(Array array) {
        BigInteger slot = ((UnsignedInteger)array.getDataItems().get(1)).getValue();
        return new RequireTimeAfter(slot);
    }

    public static RequireTimeAfter deserialize(JsonNode jsonNode) {
        BigInteger slot = jsonNode.get("slot").bigIntegerValue();
        return new RequireTimeAfter(slot);
    }
}
