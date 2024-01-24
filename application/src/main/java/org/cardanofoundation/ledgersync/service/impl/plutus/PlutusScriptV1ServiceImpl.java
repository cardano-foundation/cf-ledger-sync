package org.cardanofoundation.ledgersync.service.impl.plutus;

import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.plutus.spec.PlutusV1Script;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.consumercommon.entity.Script;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.ScriptType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PlutusScriptV1ServiceImpl extends PlutusScriptService<PlutusV1Script> {

    @Override
    public Script handle(PlutusV1Script plutusScript, Tx tx) {
        try {
            return buildScript(getTxScriptBytes(plutusScript, tx), ScriptType.PLUTUSV1, tx,
                    plutusScript.getPolicyId());
        } catch (CborSerializationException e) {
            log.error("Get Policy Id of plutusV1, tx {}", tx.getHash());
        }
        return null;
    }

    @Override
    protected byte[] getTxScriptBytes(PlutusV1Script plutusScript, Tx tx) {
        byte[] data = new byte[]{};
        try {
            data = plutusScript.serializeScriptBody();
        } catch (Exception e) {
            log.warn("Exception serialize plutus V1 script tx_hash: {}, ", tx.getHash());
        }
        return data;
    }
}
