package org.cardanofoundation.ledgersync.service.impl.nativescript;

import com.bloxbean.cardano.client.exception.CborSerializationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.consumercommon.entity.Script;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.ScriptType;
import org.cardanofoundation.ledgersync.common.common.nativescript.ScriptAny;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.common.util.JsonUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ScriptAnyServiceImpl extends NativeScriptService<ScriptAny> {

    @Override
    public Script handle(ScriptAny nativeScript, Tx tx) {
        try {
            return buildScript(ScriptType.TIMELOCK, HexUtil.encodeHexString(nativeScript.getScriptHash()), tx,
                    JsonUtil.getPrettyJson(nativeScript));
        } catch (CborSerializationException e) {
            log.error("Serialize native script hash error, tx {}", tx.getHash());
        }
        return null;
    }

}
