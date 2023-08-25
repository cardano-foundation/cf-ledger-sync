package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.nativescript;

import org.cardanofoundation.explorer.consumercommon.entity.Script;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.explorer.consumercommon.enumeration.ScriptType;
import org.cardanofoundation.ledgersync.common.common.nativescript.NativeScript;
import org.cardanofoundation.ledgersync.explorerconsumer.service.SyncServiceInstance;


public abstract class NativeScriptService<T extends NativeScript> implements
        SyncServiceInstance<T> {

    public abstract Script handle(T nativeScript, Tx tx);


    protected Script buildScript(ScriptType type, String hash, Tx tx, String json) {
        return Script.builder().tx(tx).json(json).type(type)
                .hash(hash).build();
    }


}
