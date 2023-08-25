package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.nativescript;

import com.bloxbean.cardano.client.exception.CborSerializationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.explorer.consumercommon.entity.Script;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.explorer.consumercommon.enumeration.ScriptType;
import org.cardanofoundation.ledgersync.common.common.nativescript.RequireTimeBefore;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.common.util.JsonUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ScriptInvalidBeforeServiceImpl extends NativeScriptService<RequireTimeBefore> {

    @Override
    public Script handle(RequireTimeBefore nativeScript, Tx tx) {
        try {
            return buildScript(ScriptType.TIMELOCK, HexUtil.encodeHexString(nativeScript.getScriptHash()), tx,
                    JsonUtil.getPrettyJson(nativeScript));
        } catch (CborSerializationException e) {
            log.error("Serialize native script hash error, tx {}", tx.getHash());
        }
        return null;
    }
}
