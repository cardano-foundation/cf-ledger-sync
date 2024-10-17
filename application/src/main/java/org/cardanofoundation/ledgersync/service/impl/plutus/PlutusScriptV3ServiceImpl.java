package org.cardanofoundation.ledgersync.service.impl.plutus;

import com.bloxbean.cardano.client.exception.CborSerializationException;

import com.bloxbean.cardano.client.plutus.spec.PlutusV3Script;
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
public class PlutusScriptV3ServiceImpl extends PlutusScriptService<PlutusV3Script> {

    @Override
    public Script handle(PlutusV3Script plutusScript, Tx tx) {
        try {
            return buildScript(getTxScriptBytes(plutusScript, tx), ScriptType.PLUTUSV3, tx,
                    plutusScript.getPolicyId());
        } catch (CborSerializationException e) {
            log.error("Get Policy Id of plutusV2, tx {}", tx.getHash());
        }
        return null;
    }

    @Override
    protected byte[] getTxScriptBytes(PlutusV3Script plutusScript, Tx tx) {
        byte[] data = new byte[]{};
        try {
            data = plutusScript.serializeScriptBody();
        } catch (Exception e) {
            log.warn("Exception serialize plutusV2 script tx_hash: {}, ", tx.getHash());
        }
        return data;
    }
}
