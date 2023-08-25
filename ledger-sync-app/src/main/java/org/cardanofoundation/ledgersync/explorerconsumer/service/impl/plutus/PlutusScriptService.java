package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.plutus;

import com.bloxbean.cardano.client.plutus.spec.PlutusScript;
import org.cardanofoundation.explorer.consumercommon.entity.Script;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.explorer.consumercommon.enumeration.ScriptType;
import org.cardanofoundation.ledgersync.explorerconsumer.service.SyncServiceInstance;


public abstract class PlutusScriptService<T extends PlutusScript> implements
        SyncServiceInstance<T> {


    public abstract Script handle(T plutusScript, Tx tx);

    protected abstract byte[] getTxScriptBytes(T plutusScript, Tx tx);

    protected Script buildScript(byte[] data, ScriptType type, Tx tx, String hash) {
        tx.addScriptSize(data.length);
        return Script.builder().tx(tx).serialisedSize(data.length).bytes(data)
                .type(type).hash(hash).build();
    }
}
