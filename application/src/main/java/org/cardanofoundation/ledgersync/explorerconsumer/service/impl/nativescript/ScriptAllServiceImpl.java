package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.nativescript;

import com.bloxbean.cardano.client.exception.CborSerializationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.explorer.consumercommon.entity.Script;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.explorer.consumercommon.enumeration.ScriptType;
import org.cardanofoundation.ledgersync.common.common.nativescript.ScriptAll;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.common.util.JsonUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ScriptAllServiceImpl extends NativeScriptService<ScriptAll> {

    @Override
    public Script handle(ScriptAll nativeScript, Tx tx) {
        try {
            return buildScript(ScriptType.TIMELOCK, HexUtil.encodeHexString(nativeScript.getScriptHash()), tx,
                    JsonUtil.getPrettyJson(nativeScript));
        } catch (CborSerializationException e) {
            log.error("Serialize native script hash error, tx {}", tx.getHash());
        }
        return null;
    }
}
