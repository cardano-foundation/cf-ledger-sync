package org.cardanofoundation.ledgersync.service.impl.nativescript;

import org.cardanofoundation.ledgersync.consumercommon.entity.Script;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.ScriptType;
import org.cardanofoundation.ledgersync.common.common.nativescript.NativeScript;
import org.cardanofoundation.ledgersync.service.SyncServiceInstance;


public abstract class NativeScriptService<T extends NativeScript> implements
        SyncServiceInstance<T> {

    public abstract Script handle(T nativeScript, Tx tx);


    protected Script buildScript(ScriptType type, String hash, Tx tx, String json) {
        return Script.builder().tx(tx).json(json).type(type)
                .hash(hash).build();
    }


}
