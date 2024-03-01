package org.cardanofoundation.ledgersync.service.impl;

import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.UnsignedInteger;
import com.bloxbean.cardano.client.plutus.spec.PlutusScript;
import com.bloxbean.cardano.client.plutus.spec.PlutusV1Script;
import com.bloxbean.cardano.client.plutus.spec.PlutusV2Script;
import com.bloxbean.cardano.yaci.core.model.PlutusScriptType;
import com.bloxbean.cardano.yaci.core.model.Witnesses;
import com.bloxbean.cardano.yaci.store.common.util.PlutusV3Script;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.consumercommon.entity.Script;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.common.common.nativescript.NativeScript;
import org.cardanofoundation.ledgersync.common.util.CborSerializationUtil;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTxOut;
import org.cardanofoundation.ledgersync.exception.HashScriptException;
import org.cardanofoundation.ledgersync.factory.NativeScriptFactory;
import org.cardanofoundation.ledgersync.factory.PlutusScriptFactory;
import org.cardanofoundation.ledgersync.projection.ScriptProjection;
import org.cardanofoundation.ledgersync.repository.ScriptRepository;
import org.cardanofoundation.ledgersync.service.ScriptService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScriptServiceImpl implements ScriptService {

    NativeScriptFactory nativeScriptFactory;

    PlutusScriptFactory plutusScriptFactory;

    ScriptRepository scriptRepository;

    @Override
    public void handleScripts(Collection<AggregatedTx> aggregatedTxs, Map<String, Tx> txMap) {
        Map<String, Script> scriptMap = new HashMap<>();

        aggregatedTxs
                .stream()
                .filter(aggregatedTx -> Objects.nonNull(aggregatedTx.getWitnesses()))
                .forEach(aggregatedTx -> {
                    Witnesses txWitnesses = aggregatedTx.getWitnesses();
                    getAllScript(txWitnesses,
                            txMap.get(aggregatedTx.getHash()))
                            .forEach(scriptMap::putIfAbsent);
                    AggregatedTxOut collateralReturn = aggregatedTx.getCollateralReturn();
                    if (Objects.isNull(collateralReturn)) {
                        return;
                    }

                    if (StringUtils.hasText(collateralReturn.getScriptRef())) {
                        log.info("Handle collateral script reference when tx {} failed!",
                                aggregatedTx.getHash());
                        Script script = getScriptFromScriptRef(
                                collateralReturn.getScriptRef(),
                                txMap.get(aggregatedTx.getHash()));
                        updateTxForScript(script, scriptMap);
                    }
                });
        handleScriptFromTxOut(aggregatedTxs, txMap, scriptMap);
        saveNonExistsScripts(scriptMap.values());
    }

    @Override
    public Map<String, Script> getAllScript(Witnesses txWitnesses, Tx tx) {

        Map<String, Script> mScripts = new HashMap<>();
        //Native script
        mScripts.putAll(txWitnesses.getNativeScripts().stream()
                .map(nScript -> {
                    try {
                        return nativeScriptFactory.handle(NativeScript.deserializeJson(nScript.getContent()), tx);
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .collect(Collectors.toMap(Script::getHash,
                        Function.identity(), (a, b) -> a)));

        //Plutus script v1
        //TODO ?? yaci check
        mScripts.putAll(txWitnesses.getPlutusV1Scripts().stream()
                .map(plutusScript -> plutusScriptFactory.handle(convertToPlutusScript(plutusScript), tx))
                .collect(Collectors.toMap(Script::getHash,
                        Function.identity(), (a, b) -> a)));

        //Plutus script v2
        //TODO ?? yaci check
        mScripts.putAll(txWitnesses.getPlutusV2Scripts().stream()
                .map(plutusScript -> plutusScriptFactory.handle(convertToPlutusScript(plutusScript), tx))
                .collect(Collectors.toMap(Script::getHash,
                        Function.identity(), (a, b) -> a)));

        mScripts.putAll(txWitnesses.getPlutusV3Scripts().stream()
                .map(plutusScript -> plutusScriptFactory.handle(convertToPlutusScript(plutusScript), tx))
                .collect(Collectors.toMap(Script::getHash,
                        Function.identity(), (a, b) -> a)));

        return mScripts;
    }

    private PlutusScript convertToPlutusScript(com.bloxbean.cardano.yaci.core.model.PlutusScript plutusScript) {
        if (plutusScript.getType() == PlutusScriptType.PlutusScriptV1)
            return PlutusV1Script.builder()
                    .cborHex(plutusScript.getContent())
                    .build();
        else if (plutusScript.getType() == PlutusScriptType.PlutusScriptV2)
            return PlutusV2Script.builder()
                    .cborHex(plutusScript.getContent())
                    .build();
        else if (plutusScript.getType() == PlutusScriptType.PlutusScriptV3)
            return PlutusV3Script.builder()
                    .cborHex(plutusScript.getContent())
                    .build();
        else
            throw new IllegalStateException("Invalid plutus script type : " + plutusScript.getType());
    }

    @Override
    public List<Script> saveNonExistsScripts(Collection<Script> scripts) {
        if (CollectionUtils.isEmpty(scripts)) {
            return Collections.emptyList();
        }

        Set<String> hashes = scripts.stream()
                .filter(Objects::nonNull)
                .map(Script::getHash)
                .collect(Collectors.toSet());
        Map<String, Script> scriptMap = getScriptsByHashes(hashes);

        Map<String, Script> scriptNeedSave = new HashMap<>();
        scripts.forEach(script -> {
            if (!scriptMap.containsKey(script.getHash())) {
                scriptNeedSave.put(script.getHash(), script);
            }
        });

        if (CollectionUtils.isEmpty(scriptNeedSave)) {
            return Collections.emptyList();
        }

        // Script records need to be saved in sequential order to ease out future queries
        return scriptRepository.saveAll(scriptNeedSave.values()
                .stream()
                .sorted((Comparator.comparing(script -> script.getTx().getId())))
                .toList());
    }

    @Override
    public String getHashOfReferenceScript(String hexReferScript) throws HashScriptException {
        try {
            Array scriptArray = (Array) CborSerializationUtil.deserialize(
                    HexUtil.decodeHexString(hexReferScript));
            int type = ((UnsignedInteger) scriptArray.getDataItems().get(0)).getValue().intValue();
            switch (type) {
                case 0:
                    NativeScript script = NativeScript.deserialize((Array) scriptArray.getDataItems().get(1));
                    return script.getPolicyId();
                case 1:
                    PlutusV1Script plutusV1Script = PlutusV1Script.deserialize(
                            (ByteString) scriptArray.getDataItems().get(1));
                    return plutusV1Script.getPolicyId();
                case 2:
                    PlutusV2Script plutusV2Script = PlutusV2Script.deserialize(
                            (ByteString) scriptArray.getDataItems().get(1));
                    return plutusV2Script.getPolicyId();
                case 3:
                    PlutusV3Script plutusV3Script = PlutusV3Script.deserialize(
                            (ByteString) scriptArray.getDataItems().get(1));
                    return plutusV3Script.getPolicyId();
                default:
                    log.error("Invalid script type {}, hex {}", type, hexReferScript);
                    throw new HashScriptException("Script type invalid " + type);
            }
        } catch (Exception e) {
            log.error("Get hash of script {}, error: {}", hexReferScript, e.getMessage());
            throw new HashScriptException(e);
        }
    }

    public void handleScriptFromTxOut(Collection<AggregatedTx> aggregatedTxs, Map<String, Tx> txMap,
                                      Map<String, Script> scriptMap) {
        var mTxHashAndScript = aggregatedTxs.stream()
                .filter(aggregatedTx -> {
                    if (aggregatedTx.isValidContract()) {
                        for (AggregatedTxOut aggregatedTxOut : aggregatedTx.getTxOutputs()) {
                            if (StringUtils.hasText(aggregatedTxOut.getScriptRef())) {
                                return true;
                            }
                        }
                    }
                    return false;
                })
                .collect(Collectors.toMap(AggregatedTx::getHash,
                        aggregatedTx -> aggregatedTx.getTxOutputs().stream()
                                .map(AggregatedTxOut::getScriptRef)
                                .filter(StringUtils::hasText)
                                .collect(Collectors.toList())));

        mTxHashAndScript.forEach((txHash, listScriptCborHex) -> {
            var tx = txMap.get(txHash);
            listScriptCborHex.forEach(scriptCborHex -> {
                var script = getScriptFromScriptRef(scriptCborHex, tx);
                updateTxForScript(script, scriptMap);
            });
        });
    }

    //script will get id of which tx has id smaller
    private void updateTxForScript(Script newScript, Map<String, Script> scriptMap) {
        var scriptExisted = scriptMap.get(newScript.getHash());
        if (Objects.isNull(scriptExisted)) {
            scriptMap.put(newScript.getHash(), newScript);
        } else {
            if (scriptExisted.getTx().getId() > newScript.getTx().getId()) {
                scriptMap.put(newScript.getHash(), newScript);
            }
        }
    }

    public Script getScriptFromScriptRef(String hexReferScript, Tx tx) {
        try {
            Array scriptArray = (Array) CborSerializationUtil.deserialize(
                    HexUtil.decodeHexString(hexReferScript));
            int type = ((UnsignedInteger) scriptArray.getDataItems().get(0)).getValue().intValue();
            //org.cardanofoundation.ledgersync.common.common.nativescript.Script script = null;
            switch (type) {
                case 0:
                    NativeScript script = NativeScript.deserialize((Array) scriptArray.getDataItems().get(1));
                    return nativeScriptFactory.handle(script, tx);
                case 1:
                    var plutusScriptV1 = PlutusV1Script.deserialize(
                            (ByteString) scriptArray.getDataItems().get(1));
                    return plutusScriptFactory.handle(plutusScriptV1, tx);
                case 2:
                    var plutusScriptV2 = PlutusV2Script.deserialize(
                            (ByteString) scriptArray.getDataItems().get(1));
                    return plutusScriptFactory.handle(plutusScriptV2, tx);
                case 3:
                    var plutusScriptV3 = PlutusV3Script.deserialize(
                            (ByteString) scriptArray.getDataItems().get(1));
                    return plutusScriptFactory.handle(plutusScriptV3, tx);
                default:
                    log.error("Invalid script type {}, hex {}", type, hexReferScript);
                    throw new HashScriptException("Script type invalid " + type);
            }
        } catch (Exception e) {
            log.error("Get hash of script {}, error: {}", hexReferScript, e.getMessage());
            throw new HashScriptException(e);
        }
    }

    @Override
    public Map<String, Script> getScriptsByHashes(Set<String> hashes) {
        if (CollectionUtils.isEmpty(hashes)) { //TODO refactor
            return Collections.emptyMap();
        }
        return scriptRepository.getScriptByHashes(hashes)
                .stream()
                .collect(Collectors.toConcurrentMap(
                        ScriptProjection::getHash,
                        scriptProjection -> Script.builder()
                                .id(scriptProjection.getId())
                                .hash(scriptProjection.getHash())
                                .build())
                );
    }
}
